package de.nexusrealms.tenreg.data.interfaces;

import de.nexusrealms.tenreg.data.providers.TenregBlockTags;

public interface EntryWithBlockTag {
    void accept(TenregBlockTags generator);
}
