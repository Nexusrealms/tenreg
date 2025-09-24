package de.nexusrealms.tenreg.testmod;

import de.nexusrealms.tenreg.Reg;
import net.fabricmc.api.ModInitializer;

public class Testmod implements ModInitializer {
    public static final Reg REG = Reg.create("testmod");
    @Override
    public void onInitialize() {
        ModItems.init();
        ModBlocks.init();
        REG.register();
    }
}
