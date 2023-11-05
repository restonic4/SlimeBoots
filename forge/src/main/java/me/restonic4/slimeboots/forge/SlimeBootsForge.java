package me.restonic4.slimeboots.forge;

import dev.architectury.platform.forge.EventBuses;
import me.restonic4.slimeboots.SlimeBoots;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SlimeBoots.MOD_ID)
public class SlimeBootsForge {
    public SlimeBootsForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SlimeBoots.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SlimeBoots.init();
    }
}