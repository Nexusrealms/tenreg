package de.nexusrealms.tenreg.item;

import de.nexusrealms.tenreg.RegEntry;
import de.nexusrealms.tenreg.SubReg;
import de.nexusrealms.tenreg.data.TranslationMaker;
import de.nexusrealms.tenreg.data.RecipeMaker;
import de.nexusrealms.tenreg.data.TranslationKeyProvider;
import de.nexusrealms.tenreg.data.interfaces.EntryWithItemTag;
import de.nexusrealms.tenreg.data.interfaces.EntryWithItemModel;
import de.nexusrealms.tenreg.data.interfaces.EntryWithTranslation;
import de.nexusrealms.tenreg.data.interfaces.EntryWithRecipe;
import de.nexusrealms.tenreg.data.providers.TenregItemTags;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemEntry<T extends Item> extends RegEntry<T> implements ItemConvertible, TranslationKeyProvider, EntryWithItemTag, EntryWithItemModel, EntryWithRecipe, EntryWithTranslation {
    @Nullable
    private final BiConsumer<ItemEntry<T>, ItemModelGenerator> model;
    @Nullable
    private final TranslationMaker translation;
    @Nullable
    private final RecipeMaker<ItemEntry<T>> recipeMaker;
    @Getter
    private final Consumer<ItemEntry<T>> callback;
    @Getter
    private final Consumer<ItemEntry<T>> clientCallback;
    @Getter
    private final List<TagKey<Item>> tags;
    protected ItemEntry(SubReg<T> owner, RegistryKey<T> key, T value, List<TagKey<Item>> tags, Consumer<ItemEntry<T>> callback, Consumer<ItemEntry<T>> clientCallback, @Nullable BiConsumer<ItemEntry<T>, ItemModelGenerator> model, @Nullable TranslationMaker translation, @Nullable RecipeMaker<ItemEntry<T>> recipeMaker) {
        super(owner, key, value);
        this.model = model;
        this.translation = translation;
        this.recipeMaker = recipeMaker;
        this.callback = callback;
        this.clientCallback = clientCallback;
        this.tags = tags;
    }


    @Override
    public Item asItem() {
        return getValue();
    }

    @Override
    public String getTranslationKey() {
        return getValue().getTranslationKey();
    }

    @Override
    protected void postRegister() {
        callback.accept(this);
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
            clientCallback.accept(this);
        }
    }

    @Override
    public void accept(TenregItemTags generator) {
        getTags().forEach(tTagKey -> generator.addTag(tTagKey, asItem()));
    }

    @Override
    public void accept(ItemModelGenerator generator) {
        if(model != null) model.accept(this, generator);
    }

    @Override
    public void accept(FabricLanguageProvider.TranslationBuilder generator) {
        if(translation != null) translation.accept(this, generator);
    }

    @Override
    public void accept(RecipeGenerator generator) {
        if(recipeMaker != null) recipeMaker.generate(generator, this);
    }

    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    public static final class Builder<T extends Item> {
        private final SubReg<T> owner;
        private Consumer<ItemEntry<T>> callback = item -> {};
        private Consumer<ItemEntry<T>> callbackClient = item -> {};
        private List<TagKey<Item>> tags;
        public Builder(ItemReg owner) {
            this.owner = (SubReg<T>) owner;
            tags = new ArrayList<>();
        }
        @Setter
        private BiConsumer<ItemEntry<T>, ItemModelGenerator> model;
        @Setter
        private TranslationMaker translation;
        @Setter
        private RecipeMaker<ItemEntry<T>> recipe;
        @Setter
        private Function<Item.Settings, T> constructor;
        @Setter
        private Supplier<Item.Settings> settingsSupplier = Item.Settings::new;
        private Consumer<Item.Settings> settingsUpdater = settings1 -> {};
        public ItemEntry.Builder<T> transform(Consumer<ItemEntry.Builder<T>> transformer){
            transformer.accept(this);
            return this;
        }
        public Builder<T> callback(Consumer<ItemEntry<T>> consumer){
            callback = callback.andThen(consumer);
            return this;
        }
        public Builder<T> callbackClient(Consumer<ItemEntry<T>> consumer){
            callbackClient = callbackClient.andThen(consumer);
            return this;
        }
        public Builder<T> settingsUpdater(Consumer<Item.Settings> consumer){
            settingsUpdater = settingsUpdater.andThen(consumer);
            return this;
        }
        @SafeVarargs
        public final Builder<T> group(BiConsumer<FabricItemGroupEntries, ItemEntry<T>> grouper, RegistryKey<ItemGroup>... groups){
            return callback(item -> {
                for (RegistryKey<ItemGroup> group : groups){
                    ItemGroupEvents.modifyEntriesEvent(group).register(fabricItemGroupEntries -> grouper.accept(fabricItemGroupEntries, item));
                }
            });
        }
        @SafeVarargs
        public final Builder<T> group(RegistryKey<ItemGroup>... groups){
            return group(ItemGroup.Entries::add, groups);
        }
        @SafeVarargs
        public final Builder<T> group(ItemConvertible after, RegistryKey<ItemGroup>... groups){
            return group((fabricItemGroupEntries, itemEntry) -> fabricItemGroupEntries.addAfter(after, itemEntry), groups);
        }
        @SafeVarargs
        public final Builder<T> tag(TagKey<Item>... tags){
            Collections.addAll(this.tags, tags);
            return this;
        }
        public Builder<T> simpleModel(){
            return model((itemEntry, itemModelGenerator) -> itemModelGenerator.register(itemEntry.asItem(), Models.GENERATED));
        }
        public Builder<T> noModel(){
            return model((tItemEntry, itemModelGenerator) -> {});
        }
        public Builder<T> name(String name){
            return translation(TranslationMaker.simple(name));
        }
        public Builder<T> settings(Item.Settings settings){
            return settingsSupplier(() -> settings);
        }
        public ItemEntry<T> build(RegistryKey<T> key){
            Item.Settings itemSettings = settingsSupplier.get();
            settingsUpdater.accept(itemSettings);
            itemSettings.registryKey((RegistryKey<Item>) key);
            T item = constructor.apply(itemSettings);
            return new ItemEntry<>(owner, key, item, tags, callback, callbackClient, model, translation, recipe);
        }
        public ItemEntry<T> build(Identifier identifier){
            return build((RegistryKey<T>) RegistryKey.of(RegistryKeys.ITEM, identifier));
        }
        public ItemEntry<T> build(String name){
            return build(owner.id().apply(name));
        }
        public ItemEntry.Builder<T> copy(){
            return new ItemEntry.Builder<T>(owner, callback ,callbackClient, new ArrayList<>(tags), model, translation, recipe, constructor, settingsSupplier, settingsUpdater);
        }
    }
}
