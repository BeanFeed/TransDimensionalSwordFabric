package com.beanfeed.tdsword.screen.GuiElements;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class TDSMLapisSlot extends Slot {
    public TDSMLapisSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(Items.LAPIS_LAZULI);
    }
}
