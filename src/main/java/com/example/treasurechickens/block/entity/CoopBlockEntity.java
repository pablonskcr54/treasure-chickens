package com.example.treasurechickens.block.entity;

import com.example.treasurechickens.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Курятник: контейнер на 3 ряда (27 слотов), куда сокровищные курицы
 * несут предметы. Открывается как сундук.
 */
public class CoopBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container {

    public static final int SLOT_COUNT = 27;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public CoopBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COOP.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }

    // ==== Container ====
    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(this.items, slot, amount);
        this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return player.canInteractWithBlock(this.worldPosition, 8.0) && this.level.getBlockEntity(this.worldPosition) == this;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.treasure_chickens.coop");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return ChestMenu.threeRows(id, inv, this);
    }

    // ==== Хелпер: положить предмет, вернуть остаток ====
    public ItemStack tryInsert(ItemStack stack) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (stack.isEmpty()) break;
            ItemStack slotItem = this.getItem(i);
            if (slotItem.isEmpty()) {
                this.setItem(i, stack);
                return ItemStack.EMPTY;
            } else if (ItemStack.isSameItemSameComponents(slotItem, stack)
                    && slotItem.getCount() < this.getMaxStackSize()) {
                int space = this.getMaxStackSize() - slotItem.getCount();
                int move = Math.min(space, stack.getCount());
                slotItem.grow(move);
                stack.shrink(move);
            }
        }
        return stack;
    }

    public void dropContents() {
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, this);
        }
    }
}