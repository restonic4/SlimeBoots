package me.restonic4.slimeboots.item;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import me.restonic4.restapi.holder.RestItem;
import me.restonic4.restapi.item.ItemRegistry;
import me.restonic4.restapi.util.CustomArmorMaterial;
import me.restonic4.restapi.util.CustomItemProperties;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static me.restonic4.slimeboots.SlimeBoots.MOD_ID;

public class ItemManager {
    public static CustomArmorMaterial SLIME_MATERIAL = new CustomArmorMaterial(
            "slime",
            26,
            new int[]{ 5, 7, 5, 4 },
            25,
            SoundEvents.SLIME_SQUISH,
            1f,
            0f,
            () -> Ingredient.of(Items.SLIME_BALL)
    );

    public static RestItem SLIME_BOOTS = ItemRegistry.CreateCustom(
            MOD_ID,
            "slime_boots",
            () -> new ArmorItem(
                    SLIME_MATERIAL,
                    SLIME_MATERIAL.boots(),
                    new CustomItemProperties().tab(CreativeModeTabs.TOOLS_AND_UTILITIES).build()
            )
    );

    public static void register() {
        ItemRegistry.Register(MOD_ID);

        AtomicReference<UUID> uuid = new AtomicReference<UUID>();

        EntityEvent.LIVING_HURT.register(
                (entity, damageSource, damage) -> {
                    if (damageSource.type().msgId().toLowerCase().contains("fall")) {
                        AtomicBoolean hasSlimeBoots = new AtomicBoolean(false);

                        entity.getArmorSlots().forEach(itemStack -> {
                            if (itemStack.getItem().getDefaultInstance().getItem() == SLIME_BOOTS.get().get()) {
                                hasSlimeBoots.set(true);
                            }
                        });

                        if (hasSlimeBoots.get()) {
                            return EventResult.interrupt(false);
                        }
                    }

                    return EventResult.pass();
                }
        );

        TickEvent.PLAYER_PRE.register(
                (player) -> {
                    uuid.set(player.getUUID());
                }
        );

        TickEvent.SERVER_POST.register(
                (server) -> {
                    if (uuid.get() != null) {
                        ServerPlayer player = server.getPlayerList().getPlayer(uuid.get());

                        if (player != null) {
                            AtomicBoolean hasSlimeBoots = new AtomicBoolean(false);

                            player.getArmorSlots().forEach(itemStack -> {
                                if (itemStack.getItem().getDefaultInstance().getItem() == SLIME_BOOTS.get().get()) {
                                    hasSlimeBoots.set(true);
                                }
                            });

                            if (hasSlimeBoots.get()) {
                                MobEffectInstance effect = ItemRegistry.CreateExistingEffect(MobEffects.JUMP, 30, 1);
                                player.addEffect(effect);
                            }
                        }
                    }
                }
        );
    }
}
