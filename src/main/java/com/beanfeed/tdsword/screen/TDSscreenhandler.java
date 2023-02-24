package com.beanfeed.tdsword.screen;

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
    private final Inventory inventory;
    private Inventory tdsInv = new SimpleInventory(1);
    private final ItemStack itemStack;
    public final boolean isActivated;
    public TDSscreenhandler(int syncId, PlayerInventory pInv) {
        this(syncId, pInv, new SimpleInventory(3));
    }
    public TDSscreenhandler(int syncId, PlayerInventory pInv, Inventory inv) {
        super(TDScreenHandlers.TD_SWORD_MENU, syncId);
        checkSize(inv, 1);
        this.inventory = inv;

        this.itemStack = pInv.getMainHandStack();
        inv.onOpen(pInv.player);

        this.isActivated = itemStack.getNbt().contains("active") && itemStack.getNbt().getBoolean("active");
        if(this.isActivated) {
            if(!tdsInv.getStack(0).isOf(Items.GHAST_TEAR)) tdsInv.setStack(0,ItemStack.EMPTY);
            tdsInv.setStack(0,inv.getStack(0));
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
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
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
        return this.inventory.canPlayerUse(player);
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
            this.addSlot(new TDSMGoldSlot(inventory, 0,62, 33));
            this.addSlot(new TDSMLapisSlot(inventory, 1,80, 33));
            this.addSlot(new TDSMRuneSlot(inventory, 2, 98, 33));
        }
        else {

            this.addSlot(new TDSMTearSlot(tdsInv, 0,80, 33));

        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        NbtCompound nbt = itemStack.getOrCreateNbt();
        if(isActivated) {
            DefaultedList<ItemStack> items = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
            for(int i = 0; i < inventory.size(); i++) {
                items.set(i,inventory.getStack(i));
            }

            Inventories.writeNbt(nbt, items);
        } else {
            DefaultedList<ItemStack> items = DefaultedList.ofSize(tdsInv.size(), ItemStack.EMPTY);
            for(int i = 0; i < tdsInv.size(); i++) {
                items.set(i,tdsInv.getStack(i));
            }
            Inventories.writeNbt(nbt, items);
        }
    }


}
