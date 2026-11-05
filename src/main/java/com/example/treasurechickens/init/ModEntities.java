package com.example.treasurechickens.init;

import com.example.treasurechickens.TreasureChickens;
import com.example.treasurechickens.entity.TreasureChickenEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, TreasureChickens.MODID);

    public static final TreasureChickenEntity.Definition COAL =
            new TreasureChickenEntity.Definition("coal", Items.COAL, 3, 400, 8);
    public static final TreasureChickenEntity.Definition IRON =
            new TreasureChickenEntity.Definition("iron", Items.IRON_INGOT, 2, 900, 6);
    public static final TreasureChickenEntity.Definition GOLD =
            new TreasureChickenEntity.Definition("gold", Items.GOLD_INGOT, 2, 1600, 5);
    public static final TreasureChickenEntity.Definition EMERALD =
            new TreasureChickenEntity.Definition("emerald", Items.EMERALD, 2, 2200, 3);
    public static final TreasureChickenEntity.Definition DIAMOND =
            new TreasureChickenEntity.Definition("diamond", Items.DIAMOND, 2, 3000, 2);
    public static final TreasureChickenEntity.Definition NETHERITE =
            new TreasureChickenEntity.Definition("netherite", Items.NETHERITE_INGOT, 1, 4200, 1);
    public static final TreasureChickenEntity.Definition QUARTZ =
            new TreasureChickenEntity.Definition("quartz", Items.QUARTZ, 3, 500, 9);
    public static final TreasureChickenEntity.Definition PRISMARINE =
            new TreasureChickenEntity.Definition("prismarine", Items.PRISMARINE_SHARD, 2, 1200, 7);
    public static final TreasureChickenEntity.Definition SLIME =
            new TreasureChickenEntity.Definition("slime", Items.SLIME_BALL, 2, 1300, 7);
    public static final TreasureChickenEntity.Definition ENDER_PEARL =
            new TreasureChickenEntity.Definition("ender_pearl", Items.ENDER_PEARL, 2, 2600, 4);
    public static final TreasureChickenEntity.Definition XP =
            new TreasureChickenEntity.Definition("xp", Items.EXPERIENCE_BOTTLE, 0, 700, 5);

    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> COAL_CHICKEN =
            register("coal_chicken", COAL);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> IRON_CHICKEN =
            register("iron_chicken", IRON);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> GOLD_CHICKEN =
            register("gold_chicken", GOLD);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> EMERALD_CHICKEN =
            register("emerald_chicken", EMERALD);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> DIAMOND_CHICKEN =
            register("diamond_chicken", DIAMOND);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> NETHERITE_CHICKEN =
            register("netherite_chicken", NETHERITE);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> QUARTZ_CHICKEN =
            register("quartz_chicken", QUARTZ);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> PRISMARINE_CHICKEN =
            register("prismarine_chicken", PRISMARINE);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> SLIME_CHICKEN =
            register("slime_chicken", SLIME);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> ENDER_PEARL_CHICKEN =
            register("ender_pearl_chicken", ENDER_PEARL);
    public static final DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> XP_CHICKEN =
            register("xp_chicken", XP);

    public static TreasureChickenEntity.Definition definitionFor(EntityType<?> type) {
        if (type == IRON_CHICKEN.get()) return IRON;
        if (type == GOLD_CHICKEN.get()) return GOLD;
        if (type == EMERALD_CHICKEN.get()) return EMERALD;
        if (type == DIAMOND_CHICKEN.get()) return DIAMOND;
        if (type == NETHERITE_CHICKEN.get()) return NETHERITE;
        if (type == QUARTZ_CHICKEN.get()) return QUARTZ;
        if (type == PRISMARINE_CHICKEN.get()) return PRISMARINE;
        if (type == SLIME_CHICKEN.get()) return SLIME;
        if (type == ENDER_PEARL_CHICKEN.get()) return ENDER_PEARL;
        if (type == XP_CHICKEN.get()) return XP;
        return COAL;
    }

    // По предмету, которым накормили обычную курицу, определяем,
    // в какую сокровищную курицу она превратится. null — не корм.
    public static TreasureChickenEntity.Definition definitionForFeed(Item item) {
        if (item == Items.COAL) return COAL;
        if (item == Items.IRON_INGOT) return IRON;
        if (item == Items.GOLD_INGOT) return GOLD;
        if (item == Items.EMERALD) return EMERALD;
        if (item == Items.DIAMOND) return DIAMOND;
        if (item == Items.NETHERITE_INGOT) return NETHERITE;
        if (item == Items.QUARTZ) return QUARTZ;
        if (item == Items.PRISMARINE_SHARD) return PRISMARINE;
        if (item == Items.SLIME_BALL) return SLIME;
        if (item == Items.ENDER_PEARL) return ENDER_PEARL;
        if (item == Items.EXPERIENCE_BOTTLE) return XP;
        return null;
    }

    // EntityType сокровищной курицы по её определению.
    public static EntityType<?> entityTypeFor(TreasureChickenEntity.Definition def) {
        if (def == COAL) return COAL_CHICKEN.get();
        if (def == IRON) return IRON_CHICKEN.get();
        if (def == GOLD) return GOLD_CHICKEN.get();
        if (def == EMERALD) return EMERALD_CHICKEN.get();
        if (def == DIAMOND) return DIAMOND_CHICKEN.get();
        if (def == NETHERITE) return NETHERITE_CHICKEN.get();
        if (def == QUARTZ) return QUARTZ_CHICKEN.get();
        if (def == PRISMARINE) return PRISMARINE_CHICKEN.get();
        if (def == SLIME) return SLIME_CHICKEN.get();
        if (def == ENDER_PEARL) return ENDER_PEARL_CHICKEN.get();
        if (def == XP) return XP_CHICKEN.get();
        return null;
    }

    private static DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> register(
            String id, TreasureChickenEntity.Definition def) {
        return ENTITY_TYPES.register(id, () ->
                EntityType.Builder.of(TreasureChickenEntity::new, MobCategory.CREATURE)
                        .sized(0.4f, 0.7f)
                        .clientTrackingRange(10)
                        .build(ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, id).toString()));
    }
}