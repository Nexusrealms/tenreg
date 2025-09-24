package de.nexusrealms.tenreg.block;

import de.nexusrealms.tenreg.RegEntry;
import de.nexusrealms.tenreg.SubReg;
import de.nexusrealms.tenreg.data.RecipeMaker;
import de.nexusrealms.tenreg.data.TranslationMaker;
import de.nexusrealms.tenreg.data.TranslationKeyProvider;
import de.nexusrealms.tenreg.data.functions.BlockModeler;
import de.nexusrealms.tenreg.data.interfaces.*;
import de.nexusrealms.tenreg.data.providers.TenregBlockLoot;
import de.nexusrealms.tenreg.data.providers.TenregBlockTags;
import de.nexusrealms.tenreg.item.ItemEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.*;

public class BlockEntry<T extends Block> extends RegEntry<T> implements ItemConvertible, TranslationKeyProvider, EntryWithBlockTag, EntryWithBlockModel, EntryWithTranslation, EntryWithBlockLoot {
    @Nullable
    private final BiConsumer<BlockEntry<T>, BlockStateModelGenerator> model;
    @Nullable @Getter
    private final TranslationMaker translation;
    @Nullable
    private final BiConsumer<BlockEntry<T>, TenregBlockLoot> loot;
    @Nullable @Getter
    private ItemEntry<? extends Item> item;
    @Getter
    private final Consumer<BlockEntry<T>> callback;
    @Getter
    private final Consumer<BlockEntry<T>> clientCallback;
    @Getter
    private final List<TagKey<Block>> tags;
    protected BlockEntry(SubReg<T> owner, RegistryKey<T> key, T value, List<TagKey<Block>> tags, Consumer<BlockEntry<T>> callback, Consumer<BlockEntry<T>> clientCallback, @Nullable BiConsumer<BlockEntry<T>, BlockStateModelGenerator> model, @Nullable TranslationMaker translation, @Nullable BiConsumer<BlockEntry<T>, TenregBlockLoot> loot) {
        super(owner, key, value);
        this.model = model;
        this.translation = translation;
        this.callback = callback;
        this.clientCallback = clientCallback;
        this.tags = tags;
        this.loot = loot;
    }


    @Override
    public Item asItem() {
        if(item != null){
            return item.asItem();
        }
        return null;
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
    public void accept(TenregBlockTags generator) {
        getTags().forEach(tTagKey -> generator.addTag(tTagKey, getValue()));
    }

    @Override
    public void accept(BlockStateModelGenerator generator) {
        if(model != null) model.accept(this, generator);
    }

    @Override
    public void accept(FabricLanguageProvider.TranslationBuilder generator) {
        if(translation != null) translation.accept(this, generator);
    }

    @Override
    public void accept(TenregBlockLoot blockLoot) {
        if(loot != null) loot.accept(this, blockLoot);
    }
    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    public static final class Builder<T extends Block> {
        private final SubReg<T> owner;
        private Consumer<BlockEntry<T>> callback = item -> {
        };
        private Consumer<BlockEntry<T>> callbackClient = item -> {
        };
        private List<TagKey<Block>> tags;

        public Builder(BlockReg owner) {
            this.owner = (SubReg<T>) owner;
            tags = new ArrayList<>();
        }

        @Setter
        private BiConsumer<BlockEntry<T>, BlockStateModelGenerator> model;
        @Setter @Nullable
        private TranslationMaker translation;
        @Setter
        private BiConsumer<BlockEntry<T>, TenregBlockLoot> loot;
        @Setter
        private Function<AbstractBlock.Settings, T> constructor;
        @Setter
        private Supplier<AbstractBlock.Settings> settingsSupplier = AbstractBlock.Settings::create;
        private Consumer<AbstractBlock.Settings> settingsUpdater = settings1 -> {
        };
        private BiConsumer<ItemEntry.Builder<Item>, BlockEntry<T>> itemEntryUpdater = (builder, tBlockEntry) -> {
        };
        @Nullable
        private BiFunction<ItemEntry.Builder<Item>, BlockEntry<T>, ItemEntry<Item>> itemEntryFinisher = null;

        public Builder<T> transform(Consumer<BlockEntry.Builder<T>> transformer) {
            transformer.accept(this);
            return this;
        }

        public Builder<T> callback(Consumer<BlockEntry<T>> consumer) {
            callback = callback.andThen(consumer);
            return this;
        }

        public Builder<T> callbackClient(Consumer<BlockEntry<T>> consumer) {
            callbackClient = callbackClient.andThen(consumer);
            return this;
        }

        public Builder<T> settingsUpdater(Consumer<AbstractBlock.Settings> consumer) {
            settingsUpdater = settingsUpdater.andThen(consumer);
            return this;
        }

        @SafeVarargs
        public final Builder<T> tag(TagKey<Block>... tags) {
            Collections.addAll(this.tags, tags);
            return this;
        }

        public Builder<T> simpleCubeAll() {
            return model(BlockModeler.simpleCubeAll());
        }

        public Builder<T> name(String name) {
            return translation(TranslationMaker.simple(name));
        }

        public Builder<T> settings(AbstractBlock.Settings settings) {
            return settingsSupplier(() -> settings);
        }

        public Builder<T> dropsSelf() {
            return loot((tBlockEntry, blockLoot) -> blockLoot.addDrop(tBlockEntry.getValue()));
        }

        public Builder<T> drops(ItemConvertible itemConvertible) {
            return loot((tBlockEntry, blockLoot) -> blockLoot.addDrop(tBlockEntry.getValue(), itemConvertible));
        }

        public Builder<T> drops(LootTable.Builder builder) {
            return loot((tBlockEntry, blockLoot) -> blockLoot.addDrop(tBlockEntry.getValue(), builder));
        }

        public Builder<T> drops(Function<BlockEntry<T>, LootTable.Builder> function) {
            return loot((tBlockEntry, blockLoot) -> blockLoot.addDrop(tBlockEntry.getValue(), function.apply(tBlockEntry)));
        }

        public Builder<T> item(BiConsumer<ItemEntry.Builder<Item>, BlockEntry<T>> itemUpdater) {
            itemEntryUpdater = itemEntryUpdater.andThen(itemUpdater);
            return this;
        }

        public Builder<T> finishItem(BiFunction<ItemEntry.Builder<Item>, BlockEntry<T>, ItemEntry<Item>> itemFinisher) {
            this.itemEntryFinisher = itemFinisher;
            return this;
        }

        public Builder<T> simpleItem() {
            return item((builder, tBlockEntry) -> builder
                    .constructor(settings -> new BlockItem(tBlockEntry.getValue(), settings))
                    .translation(tBlockEntry.getTranslation())
                    .model((itemItemEntry, itemModelGenerator) -> itemModelGenerator.output.accept(itemItemEntry.asItem(), ItemModels.basic(tBlockEntry.getKey().getValue().withPrefixedPath("block/")))));

        }

        @SafeVarargs
        public final BlockEntry.Builder<T> group(BiConsumer<FabricItemGroupEntries, ItemEntry<Item>> grouper, RegistryKey<ItemGroup>... groups) {
            return item((itemBuilder, tBlockEntry) -> itemBuilder.group(grouper, groups));
        }

        @SafeVarargs
        public final BlockEntry.Builder<T> group(RegistryKey<ItemGroup>... groups) {
            return item((itemBuilder, tBlockEntry) -> itemBuilder.group(groups));
        }

        @SafeVarargs
        public final BlockEntry.Builder<T> group(ItemConvertible after, RegistryKey<ItemGroup>... groups) {
            return item((itemBuilder, tBlockEntry) -> itemBuilder.group(after, groups));
        }
        public final BlockEntry.Builder<T> recipe(RecipeMaker<ItemEntry<Item>> recipeMaker) {
            return item((itemBuilder, tBlockEntry) -> itemBuilder.recipe(recipeMaker));
        }
        @SafeVarargs
        public final BlockEntry.Builder<T> itemTag(TagKey<Item>... tags) {
            return item((itemBuilder, tBlockEntry) -> itemBuilder.tag(tags));
        }

        public BlockEntry<T> build(RegistryKey<T> key) {
            AbstractBlock.Settings blockSettings = settingsSupplier.get();
            settingsUpdater.accept(blockSettings);
            blockSettings.registryKey((RegistryKey<Block>) key);
            T block = constructor.apply(blockSettings);
            BlockEntry<T> entry = new BlockEntry<>(owner, key, block, tags, callback, callbackClient, model, translation, loot);
            if (itemEntryUpdater != null || itemEntryFinisher != null) {
                ItemEntry.Builder<Item> itemEntryBuilder = owner.getRoot().getItemReg().builder();
                if (itemEntryFinisher == null) {
                    finishItem((itemBuilder, tBlockEntry) -> itemBuilder.build(tBlockEntry.getKey().getValue()));
                }
                itemEntryUpdater.accept(itemEntryBuilder, entry);
                entry.item = itemEntryFinisher.apply(itemEntryBuilder, entry);
            }
            return entry;
        }

        public BlockEntry<T> build(Identifier identifier) {
            return build((RegistryKey<T>) RegistryKey.of(RegistryKeys.BLOCK, identifier));
        }

        public BlockEntry<T> build(String name) {
            return build(owner.id().apply(name));
        }

        public Builder<T> copy() {
            return new Builder<T>(owner, callback, callbackClient, new ArrayList<>(tags), model, translation, loot, constructor, settingsSupplier, settingsUpdater, itemEntryUpdater, itemEntryFinisher);
        }
    }
}
