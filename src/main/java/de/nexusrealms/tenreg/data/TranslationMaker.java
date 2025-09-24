package de.nexusrealms.tenreg.data;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

@FunctionalInterface
public interface TranslationMaker {
    void accept(TranslationKeyProvider t, FabricLanguageProvider.TranslationBuilder translationBuilder);
    static TranslationMaker simple(String name){
        return (t, translationBuilder) -> translationBuilder.add(t.getTranslationKey(), name);
    }
}
