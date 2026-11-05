package com.example.treasurechickens.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

/**
 * Триггер «появилась сокровищная курица» (кормление/призыв).
 * Срабатывает каждый раз при превращении курицы, позволяя достижению
 * «собери всех» отслеживать прогресс через данные в критериях.
 */
public class AllChickensTrigger extends SimpleCriterionTrigger<AllChickensTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> true);
    }

    public static record TriggerInstance(Optional<ContextAwarePredicate> player)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                .apply(inst, TriggerInstance::new));
    }
}