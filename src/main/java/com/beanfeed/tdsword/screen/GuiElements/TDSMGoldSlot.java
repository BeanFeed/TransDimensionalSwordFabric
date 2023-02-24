package com.beanfeed.tdsword.screen.GuiElements;

import com.beanfeed.tdsword.item.TDItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class TDSMGoldSlot extends Slot {
    public TDSMGoldSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(Items.GOLD_INGOT);
    }
}
