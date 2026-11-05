package com.example.treasurechickens.init;

import com.example.treasurechickens.TreasureChickens;
import com.example.treasurechickens.block.CoopBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, TreasureChickens.MODID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, TreasureChickens.MODID);

    public static final DeferredHolder<Block, CoopBlock> COOP =
            BLOCKS.register("coop", () ->
                    new CoopBlock(BlockBehaviour.Properties.of()
                            .strength(2.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()));

    public static final DeferredHolder<Item, BlockItem> COOP_ITEM =
            ITEMS.register("coop", () -> new BlockItem(COOP.get(), new Item.Properties()));

    private static <T extends Block> DeferredHolder<Block, T> block(String id, T block) {
        return BLOCKS.register(id, () -> block);
    }

    private static DeferredHolder<Item, BlockItem> item(String id, Block block) {
        return ITEMS.register(id, () -> new BlockItem(block, new Item.Properties()));
    }
}