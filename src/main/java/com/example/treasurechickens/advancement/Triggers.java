package com.example.treasurechickens.advancement;

import com.example.treasurechickens.TreasureChickens;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Triggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, TreasureChickens.MODID);

    public static final net.neoforged.neoforge.registries.DeferredHolder<CriterionTrigger<?>, ChickenLevelTrigger> LEVEL_UP =
            TRIGGERS.register("chicken_level_up", ChickenLevelTrigger::new);

    public static final net.neoforged.neoforge.registries.DeferredHolder<CriterionTrigger<?>, AllChickensTrigger> CHICKEN_OBTAINED =
            TRIGGERS.register("chicken_obtained", AllChickensTrigger::new);
}