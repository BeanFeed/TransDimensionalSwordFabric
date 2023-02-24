package com.beanfeed.tdsword;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Utils {
    public static Vec3d BlockPosToVec3(BlockPos blockPos) {
        return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    public static BlockPos Vec3ToBlockPos(Vec3d vec) {
        return new BlockPos(vec.getX(), vec.getY(), vec.getZ());
    }
}
