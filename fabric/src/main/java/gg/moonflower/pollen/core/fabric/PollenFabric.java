package gg.moonflower.pollen.core.fabric;

import gg.moonflower.pollen.core.Pollen;
import net.fabricmc.api.ModInitializer;

public class PollenFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Pollen.PLATFORM.setup();
    }
}