package com.example.treasurechickens.network;

import com.example.treasurechickens.TreasureChickens;
import com.example.treasurechickens.client.TreasureChickenScreen;
import com.example.treasurechickens.entity.TreasureChickenEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    public static final String CHANNEL_VERSION = "1";

    public static void sendToServer(Object payload) {
        net.neoforged.neoforge.network.PacketDistributor.sendToServer((net.minecraft.network.protocol.common.custom.CustomPacketPayload) payload);
    }

    public static void sendOpenScreen(ServerPlayer player, TreasureChickenEntity chicken) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                new OpenTreasureScreenPayload(chicken.getId(), chicken.getLevel()));
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            PayloadRegistrar registrar = event.registrar(CHANNEL_VERSION);
            registrar.playToClient(OpenTreasureScreenPayload.TYPE, OpenTreasureScreenPayload.STREAM_CODEC,
                    NetworkHandler::handleOpenScreen);
            registrar.playToServer(UpgradeTreasurePayload.TYPE, UpgradeTreasurePayload.STREAM_CODEC,
                    NetworkHandler::handleUpgrade);
        });
    }

    private static void handleOpenScreen(OpenTreasureScreenPayload payload, IPayloadContext context) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> {
                var player = Minecraft.getInstance().player;
                var level = Minecraft.getInstance().level;
                if (player != null && level != null && level.getEntity(payload.entityId()) instanceof TreasureChickenEntity entity) {
                    Minecraft.getInstance().setScreen(new TreasureChickenScreen(entity));
                }
            });
        }
    }

    private static void handleUpgrade(UpgradeTreasurePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var sender = context.player();
            if (!(sender.level() instanceof ServerLevel server)) {
                return;
            }
            if (!(sender instanceof ServerPlayer serverPlayer)) {
                return;
            }
            var entity = server.getEntity(payload.entityId());
            if (!(entity instanceof TreasureChickenEntity chicken)) {
                return;
            }
            // Проверяем дистанцию, чтобы игрок не мог апгрейдить через пропасть
            if (serverPlayer.distanceToSqr(chicken) > 64) {
                return;
            }

            upgradeChicken(serverPlayer, chicken);
        });
    }

    private static void upgradeChicken(ServerPlayer player, TreasureChickenEntity chicken) {
        if (chicken.isMaxLevel()) {
            return;
        }
        int cost = chicken.upgradeCost();

        // Списываем ресурс из инвентаря игрока
        if (payResource(player, chicken, cost)) {
            chicken.setLevel(chicken.getLevel() + 1);
            // Достижение за уровень
            com.example.treasurechickens.advancement.Triggers.LEVEL_UP.get().trigger(player, chicken.getLevel());
            player.displayClientMessage(
                    Component.translatable("message.treasure_chickens.upgraded",
                            chicken.getDef().drop.getDescription(), chicken.getLevel()), true);
        }
    }

    private static boolean payResource(ServerPlayer player, TreasureChickenEntity chicken, int cost) {
        var resource = chicken.getDef().drop;

        // Сначала проверяем, хватает ли ресурсов во всём инвентаре
        int total = 0;
        for (net.minecraft.world.item.ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.is(resource)) {
                total += stack.getCount();
            }
        }
        if (total < cost) {
            player.displayClientMessage(Component.translatable("message.treasure_chickens.not_enough",
                    resource.getDescription(), cost), true);
            return false;
        }

        // Теперь списываем
        int toRemove = cost;
        for (net.minecraft.world.item.ItemStack stack : player.getInventory().items) {
            if (toRemove <= 0) break;
            if (!stack.isEmpty() && stack.is(resource)) {
                int remove = Math.min(toRemove, stack.getCount());
                stack.shrink(remove);
                toRemove -= remove;
            }
        }
        return toRemove <= 0;
    }
}