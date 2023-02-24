package com.beanfeed.tdsword.screen;

import com.beanfeed.tdsword.TransDimensionalSword;
import com.beanfeed.tdsword.screen.GuiElements.TDSMGoldSlot;
import com.beanfeed.tdsword.screen.GuiElements.TDSMLapisSlot;
import com.beanfeed.tdsword.screen.GuiElements.TDSMRuneSlot;
import com.beanfeed.tdsword.screen.GuiElements.TDSMTearSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class TDSscreenhandler extends ScreenHandler {
    private final SimpleInventory itemHandler;
    private SimpleInventory tdsitemHandler = new SimpleInventory(3);
    private final ItemStack itemStack;
    public final boolean isActivated;
    public TDSscreenhandler(int syncId, PlayerInventory pInv) {
        this(syncId, pInv, new SimpleInventory(3), pInv.player.getMainHandStack());
    }
    public TDSscreenhandler(int syncId, PlayerInventory pInv, SimpleInventory inv, ItemStack stack) {
        super(TDScreenHandlers.TD_SWORD_MENU, syncId);
        checkSize(inv, 1);
        this.itemHandler = inv;

        this.itemStack = stack;
        //TransDimensionalSword.LOGGER.info("Menu: " + String.valueOf(stack.getOrCreateNbt().getList("Items", 10)));
        inv.onOpen(pInv.player);
        TransDimensionalSword.LOGGER.info(String.valueOf(itemHandler.getStack(2)));
        this.isActivated = itemStack.getOrCreateNbt().contains("active") && itemStack.getOrCreateNbt().getBoolean("active");
        if(!this.isActivated && !pInv.player.world.isClient()) {
            if(!tdsitemHandler.getStack(0).isOf(Items.GHAST_TEAR)) tdsitemHandler.setStack(0,ItemStack.EMPTY);
            inv.getStack(0).setCount(inv.getStack(0).getCount() / 2);
            tdsitemHandler.setStack(0,inv.getStack(0));

        }
        addPlayerInventory(pInv);
        addPlayerHotbar(pInv);
        addSwordSlots();
    }


    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.itemHandler.size()) {
                if (!this.insertItem(originalStack, this.itemHandler.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.itemHandler.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.itemHandler.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 93 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 151));
        }
    }

    private void addSwordSlots() {
        if(this.isActivated)
        {
            this.addSlot(new TDSMGoldSlot(itemHandler, 0,62, 33));
            this.addSlot(new TDSMLapisSlot(itemHandler, 1,80, 33));
            this.addSlot(new TDSMRuneSlot(itemHandler, 2, 98, 33));
        }
        else {

            this.addSlot(new TDSMTearSlot(tdsitemHandler, 0,80, 33));

        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if(!player.world.isClient()) {
            NbtCompound nbt = itemStack.getOrCreateNbt();
            if(isActivated) nbt.put("Items", itemHandler.toNbtList());
            else nbt.put("Items", tdsitemHandler.toNbtList());

            //TransDimensionalSword.LOGGER.info("Menu2: " + String.valueOf(itemStack.getOrCreateNbt().getList("Items", 10)));
        }

    }


}
