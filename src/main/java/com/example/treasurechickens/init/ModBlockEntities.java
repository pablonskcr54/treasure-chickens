package com.example.treasurechickens.init;

import com.example.treasurechickens.TreasureChickens;
import com.example.treasurechickens.block.entity.CoopBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TreasureChickens.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoopBlockEntity>> COOP =
            BLOCK_ENTITY_TYPES.register("coop", () ->
                    BlockEntityType.Builder.of(CoopBlockEntity::new, ModBlocks.COOP.get())
                            .build(null));
}