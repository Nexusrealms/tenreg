package de.nexusrealms.tenreg.data.interfaces;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public interface EntryWithTranslation {
    void accept(FabricLanguageProvider.TranslationBuilder generator);
}
