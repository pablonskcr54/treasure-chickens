package com.example.treasurechickens.event;

import com.example.treasurechickens.entity.TreasureChickenEntity;
import com.example.treasurechickens.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Кормление обычной курицы ценным предметом превращает её
 * в соответствующую сокровищную курицу.
 */
public class ChickenFeedHandler {

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCanceled()) {
            return;
        }
        // Работаем только на сервере
        if (!(event.getLevel() instanceof ServerLevel server)) {
            return;
        }
        // Только если кликнули по ОБЫЧНОЙ (не сокровищной) курице
        if (!(event.getTarget() instanceof Chicken chicken)
                || chicken instanceof TreasureChickenEntity) {
            return;
        }
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) {
            return;
        }

        // Определяем, в какую курицу превращается
        TreasureChickenEntity.Definition def = ModEntities.definitionForFeed(stack.getItem());
        if (def == null) {
            return; // не корм — не трогаем
        }

        // Забираем один предмет из руки
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);

        // Превращение курицы
        EntityType<?> targetType = ModEntities.entityTypeFor(def);
        if (targetType == null) {
            return;
        }
        var converted = chicken.convertTo((EntityType) targetType, true);
        if (converted instanceof TreasureChickenEntity treasure) {
            treasure.setLevel(1);
            // Достижение за получение курицы
            if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                com.example.treasurechickens.advancement.Triggers.CHICKEN_OBTAINED.get().trigger(sp);
            }
        }

        // Эффекты
        server.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                chicken.getX(), chicken.getY() + 0.7, chicken.getZ(),
                20, 0.4, 0.4, 0.4, 0.1);
        server.playSound(null, chicken.blockPosition(), SoundEvents.CHICKEN_EGG,
                SoundSource.NEUTRAL, 1.0F, 1.2F);
    }
}