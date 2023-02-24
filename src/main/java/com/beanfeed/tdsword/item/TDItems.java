package com.beanfeed.tdsword.item;

import com.beanfeed.tdsword.TransDimensionalSword;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TDItems {
    public static TDSword TDSword = new TDSword(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1));
    public static Rune Rune = new Rune(new Item.Settings().group(ItemGroup.MISC).maxCount(1));
    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(TransDimensionalSword.MODID, "tdsword"), TDSword);
        Registry.register(Registry.ITEM, new Identifier(TransDimensionalSword.MODID, "rune"), Rune);
    }
}
