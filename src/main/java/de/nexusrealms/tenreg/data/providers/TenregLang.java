package de.nexusrealms.tenreg.data.providers;

import de.nexusrealms.tenreg.data.interfaces.EntryWithTranslation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TenregLang extends FabricLanguageProvider {

    private final List<EntryWithTranslation> generators;

    public TenregLang(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, List<EntryWithTranslation> generators) {
        super(dataOutput, registryLookup);
        this.generators = generators;
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        generators.forEach(entryWithTranslation -> entryWithTranslation.accept(translationBuilder));
    }
}
