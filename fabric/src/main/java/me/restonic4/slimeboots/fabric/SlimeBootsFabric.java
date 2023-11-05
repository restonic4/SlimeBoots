package me.restonic4.slimeboots.fabric;

import me.restonic4.slimeboots.SlimeBoots;
import net.fabricmc.api.ModInitializer;

public class SlimeBootsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SlimeBoots.init();
    }
}