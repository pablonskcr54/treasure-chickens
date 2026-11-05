package com.example.treasurechickens.init;

import com.example.treasurechickens.TreasureChickens;
import com.example.treasurechickens.entity.TreasureChickenEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, TreasureChickens.MODID);

    public static final DeferredHolder<Item, SpawnEggItem> COAL_EGG = registerEgg("coal_chicken_spawn_egg", ModEntities.COAL_CHICKEN, 0x9A9A9A, 0x555555);
    public static final DeferredHolder<Item, SpawnEggItem> IRON_EGG = registerEgg("iron_chicken_spawn_egg", ModEntities.IRON_CHICKEN, 0xE8E8E8, 0x8A8A8A);
    public static final DeferredHolder<Item, SpawnEggItem> GOLD_EGG = registerEgg("gold_chicken_spawn_egg", ModEntities.GOLD_CHICKEN, 0xF7E05A, 0xC39A1B);
    public static final DeferredHolder<Item, SpawnEggItem> EMERALD_EGG = registerEgg("emerald_chicken_spawn_egg", ModEntities.EMERALD_CHICKEN, 0x50E05A, 0x1B8A2C);
    public static final DeferredHolder<Item, SpawnEggItem> DIAMOND_EGG = registerEgg("diamond_chicken_spawn_egg", ModEntities.DIAMOND_CHICKEN, 0x6ADAD8, 0x1B8A9A);
    public static final DeferredHolder<Item, SpawnEggItem> NETHERITE_EGG = registerEgg("netherite_chicken_spawn_egg", ModEntities.NETHERITE_CHICKEN, 0x6E6E6E, 0x383838);
    public static final DeferredHolder<Item, SpawnEggItem> QUARTZ_EGG = registerEgg("quartz_chicken_spawn_egg", ModEntities.QUARTZ_CHICKEN, 0xF5F0E0, 0xB8A98C);
    public static final DeferredHolder<Item, SpawnEggItem> PRISMARINE_EGG = registerEgg("prismarine_chicken_spawn_egg", ModEntities.PRISMARINE_CHICKEN, 0x6DA8A0, 0x4696A0);
    public static final DeferredHolder<Item, SpawnEggItem> SLIME_EGG = registerEgg("slime_chicken_spawn_egg", ModEntities.SLIME_CHICKEN, 0x84D84A, 0x4E9E2E);
    public static final DeferredHolder<Item, SpawnEggItem> ENDER_PEARL_EGG = registerEgg("ender_pearl_chicken_spawn_egg", ModEntities.ENDER_PEARL_CHICKEN, 0x33C3A0, 0x0A6B5E);
    public static final DeferredHolder<Item, SpawnEggItem> XP_EGG = registerEgg("xp_chicken_spawn_egg", ModEntities.XP_CHICKEN, 0xA8E04A, 0x5E9E1B);

    private static DeferredHolder<Item, SpawnEggItem> registerEgg(String id,
            DeferredHolder<EntityType<?>, EntityType<TreasureChickenEntity>> entity, int bg, int fg) {
        return ITEMS.register(id, () ->
                new SpawnEggItem(entity.get(), bg, fg, new Item.Properties()));
    }
}