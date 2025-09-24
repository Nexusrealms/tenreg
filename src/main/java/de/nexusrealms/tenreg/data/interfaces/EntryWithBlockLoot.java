package de.nexusrealms.tenreg.data.interfaces;

import de.nexusrealms.tenreg.data.providers.TenregBlockLoot;

public interface EntryWithBlockLoot {
    void accept(TenregBlockLoot blockLoot);
}
