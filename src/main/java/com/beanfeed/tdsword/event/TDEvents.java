package com.beanfeed.tdsword.event;

import com.beanfeed.tdsword.PortalUtil;
import com.beanfeed.tdsword.TransDimensionalSword;
import com.beanfeed.tdsword.entity.TemporaryPortal;
import com.beanfeed.tdsword.item.TDSword;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.*;
import qouteall.imm_ptl.core.McHelper;

public class TDEvents {

    public static void register() {
        AttackBlockCallback.EVENT.register(new AttackBlockHandler());

    }
}
