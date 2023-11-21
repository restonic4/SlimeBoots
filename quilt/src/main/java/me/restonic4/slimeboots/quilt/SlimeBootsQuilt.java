package me.restonic4.slimeboots.quilt;

import me.restonic4.slimeboots.SlimeBoots;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class SlimeBootsQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        SlimeBoots.init();
    }
}