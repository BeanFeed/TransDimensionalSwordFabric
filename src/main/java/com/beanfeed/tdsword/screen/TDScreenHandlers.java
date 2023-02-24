package com.beanfeed.tdsword.screen;

import com.beanfeed.tdsword.TransDimensionalSword;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class TDScreenHandlers {
    public static ScreenHandlerType<TDSscreenhandler> TD_SWORD_MENU = new ScreenHandlerType<>(TDSscreenhandler::new);;

    public static void register() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(TransDimensionalSword.MODID,"td_sword_menu"),TD_SWORD_MENU);
    }
}
