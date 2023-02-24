package com.beanfeed.tdsword.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

public class Rune extends Item {
    public Rune(Settings settings) {
        super(settings);
    }
    public static NbtCompound getWaypointNBT(ItemStack stack, PlayerEntity pPlayer) {
        NbtCompound nbt = stack.getOrCreateNbt();

        if(nbt.contains("waypoint")) { return nbt;}
        var position = pPlayer.getPos();
        var pPos = new BlockPos(((int)position.x), position.y + 1, ((int)position.z) - 1);
        //TransDimensionalSword.LOGGER.info(String.valueOf(pPos));
        NbtCompound waypoint = NbtHelper.fromBlockPos(pPos);
        //CompoundTag rotation = new CompoundTag();
        nbt.put("waypoint", waypoint);
        nbt.putString("dimension", pPlayer.world.getDimensionKey().getValue().toString());
        nbt.putFloat("rotation", pPlayer.getHeadYaw());
        return nbt;
    }
}
