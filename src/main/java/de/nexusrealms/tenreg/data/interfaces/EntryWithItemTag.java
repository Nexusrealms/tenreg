package de.nexusrealms.tenreg.data.interfaces;

import de.nexusrealms.tenreg.data.providers.TenregItemTags;


public interface EntryWithItemTag {
    void accept(TenregItemTags generator);
}
