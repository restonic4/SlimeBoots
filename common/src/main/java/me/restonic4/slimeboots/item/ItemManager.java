package me.restonic4.slimeboots.item;

import com.mojang.blaze3d.shaders.Effect;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.hooks.level.biome.EffectsProperties;
import dev.architectury.registry.registries.DeferredSupplier;
import me.restonic4.restapi.RestApi;
import me.restonic4.restapi.item.ItemRegistry;
import me.restonic4.restapi.util.CustomItemProperties;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static me.restonic4.slimeboots.SlimeBoots.MOD_ID;

public class ItemManager {
    public static Object SLIME_BOOTS = ItemRegistry.CreateCustom(
            MOD_ID,
            "slime_boots",
            () -> new ArmorItem(
                    ArmorMaterialSet.SLIME,
                    ArmorItem.Type.BOOTS,
                    new CustomItemProperties()
                            .food(
                                    1,
                                    1,
                                    ItemRegistry.CreateExistingEffect(MobEffects.POISON,20*6,1),
                                    1)
                            .build().arch$tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
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
                            if (itemStack.getItem().getDefaultInstance().getItem() == ((DeferredSupplier<Item>)SLIME_BOOTS).get()) {
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
                                if (itemStack.getItem().getDefaultInstance().getItem() == ((DeferredSupplier<Item>)SLIME_BOOTS).get()) {
                                    hasSlimeBoots.set(true);
                                }
                            });

                            if (hasSlimeBoots.get()) {
                                MobEffectInstance effect = (MobEffectInstance) ItemRegistry.CreateExistingEffect(MobEffects.JUMP, 30, 1);
                                player.addEffect(effect);
                            }
                        }
                    }
                }
        );
    }
}
