package com.beanfeed.tdsword;

import com.beanfeed.tdsword.entity.TDEntityTypes;
import com.beanfeed.tdsword.event.TDEvents;
import com.beanfeed.tdsword.item.TDItems;
import com.beanfeed.tdsword.screen.TDScreenHandlers;
import com.beanfeed.tdsword.sound.TDSounds;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import org.slf4j.Logger;

public class TransDimensionalSword implements ModInitializer {
    public static final String MODID = "tdsword";
    public static final Logger LOGGER = LogUtils.getLogger();
    @Override
    public void onInitialize() {
        TDScreenHandlers.register();
        TDEvents.register();
        TDItems.register();
        TDSounds.register();

    }
}
