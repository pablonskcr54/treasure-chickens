package com.example.treasurechickens;

import com.example.treasurechickens.client.ClientSetup;
import com.example.treasurechickens.entity.TreasureChickenEntity;
import com.example.treasurechickens.event.ChickenFeedHandler;
import com.example.treasurechickens.init.ModBlocks;
import com.example.treasurechickens.init.ModBlockEntities;
import com.example.treasurechickens.init.ModEntities;
import com.example.treasurechickens.init.ModItems;
import com.example.treasurechickens.network.NetworkHandler;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TreasureChickens.MODID)
public class TreasureChickens {
    public static final String MODID = "treasure_chickens";
    public static final Logger LOGGER = LoggerFactory.getLogger(TreasureChickens.class);

    public TreasureChickens(IEventBus modEventBus) {
        LOGGER.info("Treasure Chickens mod loading...");

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModBlocks.ITEMS.register(modEventBus);
        com.example.treasurechickens.advancement.Triggers.TRIGGERS.register(modEventBus);

        modEventBus.addListener(TreasureChickens::addToCreativeTab);
        modEventBus.addListener(TreasureChickens::registerEntityAttributes);

        NetworkHandler.register(modEventBus);

        // События на глобальной шине NeoForge (взаимодействия с сущностями)
        NeoForge.EVENT_BUS.register(new ChickenFeedHandler());

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetup.register(modEventBus);
        }
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.COAL_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.IRON_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.GOLD_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.EMERALD_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.DIAMOND_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.NETHERITE_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.QUARTZ_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.PRISMARINE_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.SLIME_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.ENDER_PEARL_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
        event.put(ModEntities.XP_CHICKEN.get(), TreasureChickenEntity.createAttributes().build());
    }

    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.COOP_ITEM.get());
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.COAL_EGG.get());
            event.accept(ModItems.IRON_EGG.get());
            event.accept(ModItems.GOLD_EGG.get());
            event.accept(ModItems.EMERALD_EGG.get());
            event.accept(ModItems.DIAMOND_EGG.get());
            event.accept(ModItems.NETHERITE_EGG.get());
            event.accept(ModItems.QUARTZ_EGG.get());
            event.accept(ModItems.PRISMARINE_EGG.get());
            event.accept(ModItems.SLIME_EGG.get());
            event.accept(ModItems.ENDER_PEARL_EGG.get());
            event.accept(ModItems.XP_EGG.get());
        }
    }
}