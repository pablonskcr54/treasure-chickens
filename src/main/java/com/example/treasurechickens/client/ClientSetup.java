package com.example.treasurechickens.client;

import com.example.treasurechickens.init.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientSetup {
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ClientSetup::registerEntityRenderers);
    }

    private static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.COAL_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.IRON_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLD_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.EMERALD_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.DIAMOND_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.NETHERITE_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.QUARTZ_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.PRISMARINE_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.SLIME_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDER_PEARL_CHICKEN.get(), TreasureChickenRenderer::new);
        event.registerEntityRenderer(ModEntities.XP_CHICKEN.get(), TreasureChickenRenderer::new);
    }
}