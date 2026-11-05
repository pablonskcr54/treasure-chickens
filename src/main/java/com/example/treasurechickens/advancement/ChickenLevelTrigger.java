package com.example.treasurechickens.advancement;

import com.example.treasurechickens.entity.TreasureChickenEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

/**
 * Триггер «курица достигла уровня». Срабатывает, когда игрок прокачал
 * любую сокровищную курицу до заданного уровня.
 */
public class ChickenLevelTrigger extends SimpleCriterionTrigger<ChickenLevelTrigger.TriggerInstance> {

    @Override
    public Codec<ChickenLevelTrigger.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int level) {
        this.trigger(player, inst -> inst.matches(level));
    }

    public static record TriggerInstance(
            Optional<ContextAwarePredicate> player, int level)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Codec.INT.fieldOf("level").forGetter(TriggerInstance::level))
                .apply(inst, TriggerInstance::new));

        public boolean matches(int currentLevel) {
            return this.level <= currentLevel;
        }
    }

    public static Criterion<TriggerInstance> instance(int level) {
        return com.example.treasurechickens.advancement.Triggers.LEVEL_UP.get()
                .createCriterion(new TriggerInstance(Optional.empty(), level));
    }
}