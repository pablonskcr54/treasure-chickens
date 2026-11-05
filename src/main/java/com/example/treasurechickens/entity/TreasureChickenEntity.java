package com.example.treasurechickens.entity;

import com.example.treasurechickens.init.ModEntities;
import com.example.treasurechickens.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TreasureChickenEntity extends Chicken {

    public static final int MAX_LEVEL = 10;

    private static final EntityDataAccessor<Integer> DATA_LEVEL =
            SynchedEntityData.defineId(TreasureChickenEntity.class, EntityDataSerializers.INT);

    private final Definition def;
    private int treasureTimer;

    public TreasureChickenEntity(EntityType<? extends Chicken> type, Level level) {
        super(type, level);
        this.def = ModEntities.definitionFor(type);
        this.treasureTimer = this.def.eggTime + this.random.nextInt(600);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static class Definition {
        public final String id;
        public final Item drop;
        public final int maxDrop;
        public final int eggTime;
        public final int costMultiplier;

        public Definition(String id, Item drop, int maxDrop, int eggTime, int costMultiplier) {
            this.id = id;
            this.drop = drop;
            this.maxDrop = maxDrop;
            this.eggTime = eggTime;
            this.costMultiplier = costMultiplier;
        }
    }

    public Definition getDef() {
        return this.def;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LEVEL, 1);
    }

    public int getLevel() {
        return this.entityData.get(DATA_LEVEL);
    }

    public void setLevel(int level) {
        this.entityData.set(DATA_LEVEL, Math.max(1, Math.min(MAX_LEVEL, level)));
    }

    public boolean isMaxLevel() {
        return getLevel() >= MAX_LEVEL;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ChickenLevel")) {
            setLevel(tag.getInt("ChickenLevel"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ChickenLevel", getLevel());
    }

    // Эффективное количество дропа: растёт с уровнем.
    // Уровень 10 (MAX): способность «двойной улов» — дроп x2.
    public int effectiveMaxDrop() {
        int base = this.def.maxDrop + (getLevel() - 1);
        return getLevel() >= MAX_LEVEL ? base * 2 : base;
    }

    // Эффективная скорость (тики): с каждым уровнем быстрее на ~10%.
    // Уровень 5: способность «быстрый улов» — интервал делится на 1.5.
    public int effectiveEggTime() {
        return effectiveEggTime(getLevel());
    }

    public int effectiveEggTime(int level) {
        int time = Math.max(60, (int) (this.def.eggTime * (10.0 / (10 + level))));
        if (level >= 5) {
            time = Math.max(40, time / 2);
        }
        return time;
    }

    // Стоимость апгрейда с текущего уровня до следующего.
    public int upgradeCost() {
        int level = getLevel();
        return this.def.costMultiplier * level;
    }

    // Прогноз после апгрейда (для экрана): сколько предметов и через сколько тиков.
    public int maxDropAtLevel(int level) {
        int base = this.def.maxDrop + (level - 1);
        return level >= MAX_LEVEL ? base * 2 : base;
    }

    public int eggTimeAtLevel(int level) {
        int time = Math.max(60, (int) (this.def.eggTime * (10.0 / (10 + level))));
        if (level >= 5) {
            time = Math.max(40, time / 2);
        }
        return time;
    }

    // Уникальные способности для подсказки на экране.
    public boolean hasFastLay() {
        return getLevel() >= 5;
    }

    public boolean hasDoubleDrop() {
        return getLevel() >= MAX_LEVEL;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.treasureTimer <= 0) {
            this.layTreasure();
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.treasureTimer = this.effectiveEggTime() + this.random.nextInt(600);
        }
    }

    private void layTreasure() {
        if (!(this.level() instanceof ServerLevel server)) {
            return;
        }
        if (this.def.drop == null) {
            // Курица опыта: выдаём сферы опыта вместо предмета.
            // Количество сфер и их величина растут с уровнем.
            int xpBase = 4 + (getLevel() - 1) * 3;
            int orbs = 2 + this.random.nextInt(2);
            for (int i = 0; i < orbs; i++) {
                int amount = Math.max(1, (xpBase + this.random.nextInt(4)) / orbs);
                var orb = new net.minecraft.world.entity.ExperienceOrb(server,
                        this.getX(), this.getY() + 0.5, this.getZ(), amount);
                server.addFreshEntity(orb);
            }
            return;
        }
        int count = 2 + this.random.nextInt(this.effectiveMaxDrop());
        ItemStack drop = new ItemStack(this.def.drop, count);
        // Если рядом есть курятник — кладём предмет в него, иначе выкидываем на землю
        if (!tryDepositIntoCoop(drop)) {
            this.spawnAtLocation(drop);
        }
    }

    // Ищем ближайший курятник в радиусе и кладём туда предмет. true — ушло в курятник.
    private boolean tryDepositIntoCoop(ItemStack stack) {
        if (!(this.level() instanceof ServerLevel server) || stack.isEmpty()) {
            return false;
        }
        var coopBlock = com.example.treasurechickens.init.ModBlocks.COOP.get();
        double closest = Double.MAX_VALUE;
        com.example.treasurechickens.block.entity.CoopBlockEntity coop = null;
        BlockPos center = this.blockPosition();
        int r = 8;
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos p = center.offset(dx, dy, dz);
                    if (server.getBlockState(p).is(coopBlock)
                            && server.getBlockEntity(p) instanceof com.example.treasurechickens.block.entity.CoopBlockEntity c) {
                        double d = p.distSqr(center);
                        if (d < closest) {
                            closest = d;
                            coop = c;
                        }
                    }
                }
            }
        }
        if (coop == null) {
            return false;
        }
        // Радиус: ~8 блоков
        if (closest > 8.0 * 8.0) {
            return false;
        }
        ItemStack remaining = coop.tryInsert(stack);
        return remaining.isEmpty();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.sendOpenScreen(serverPlayer, this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }
}