package com.beanfeed.tdsword.sound;

import com.beanfeed.tdsword.TransDimensionalSword;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TDSounds {
    public static final Identifier tdig = new Identifier(TransDimensionalSword.MODID, "tdignite");
    public static SoundEvent TD_IGNITE = new SoundEvent(tdig);

    public static void register() {
        Registry.register(Registry.SOUND_EVENT, tdig, TD_IGNITE);
    }
}
