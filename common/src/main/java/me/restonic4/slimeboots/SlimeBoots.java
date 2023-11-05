package me.restonic4.slimeboots;

import me.restonic4.restapi.item.ItemRegistry;
import me.restonic4.slimeboots.item.ItemManager;

public class SlimeBoots
{
	public static final String MOD_ID = "slimeboots";

	public static void init() {
		ItemRegistry.CreateRegistry(MOD_ID);

		ItemManager.register();
	}
}
