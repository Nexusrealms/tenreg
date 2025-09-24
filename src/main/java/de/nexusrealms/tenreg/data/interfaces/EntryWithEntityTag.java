package de.nexusrealms.tenreg.data.interfaces;

import de.nexusrealms.tenreg.data.providers.TenregEntityTags;

public interface EntryWithEntityTag {
    void accept(TenregEntityTags generator);
}
