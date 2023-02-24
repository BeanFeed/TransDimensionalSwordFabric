package com.beanfeed.tdsword.event;

import com.beanfeed.tdsword.PortalUtil;
import com.beanfeed.tdsword.TransDimensionalSword;
import com.beanfeed.tdsword.entity.TemporaryPortal;
import com.beanfeed.tdsword.item.TDSword;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.McHelper;

public class AttackBlockHandler implements AttackBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
        if(player.getMainHandStack().getItem() instanceof TDSword sword) {
            //Do Code
            //TransDimensionalSword.LOGGER.info("Swung Sword");
            Vec3d toGo = sword.getLastWaypoint(player.getMainHandStack());
            //TransDimensionalSword.LOGGER.info(String.valueOf(toGo));
            //toGo != null checks if the sword has a saved waypoint. If not then don't run
            //event.getEntity() != null double checks that the entity isn't null
            //!event.getLevel().isClientSide() makes sure the code is only ran on the server

            if(sword.getGoldAmount(player.getMainHandStack()) == 0) return ActionResult.SUCCESS;
            TransDimensionalSword.LOGGER.info(String.valueOf(!world.isClient()));

            if(!sword.canSpawn()) return ActionResult.FAIL;
            if(toGo != null && player != null && !world.isClient() && direction == Direction.UP) {
                //gets block to spawn portal on top of

                BlockPos orgtoSpawn = pos;
                Vec3d toSpawn = new Vec3d(orgtoSpawn.getX(), orgtoSpawn.getY(), orgtoSpawn.getZ());
                //makes new portal object with dimensions
                TemporaryPortal portal = PortalUtil.makeTempPortal(0.01,2, player);
                //TransDimensionalSword.LOGGER.info("Try Spawn");
                if (portal == null) {
                    //Failed to make portal
                    TransDimensionalSword.LOGGER.info("Portal Null");
                    return ActionResult.PASS;
                };

                //gets the players head rotation when they saved the waypoint

                var pRot = sword.getLastWaypointRotation(player.getMainHandStack());
                //TransDimensionalSword.LOGGER.info(pRot + " Saved Rot");
                //if(pRot == null) return;
                //gets the players current head rotation
                var cRot = player.getHeadYaw();
                toGo = new Vec3d(toGo.x + 0.5, toGo.y, toGo.z + 0.5);
                double deltaY = pRot - cRot;
                //TransDimensionalSword.LOGGER.info(deltaY + " Current Rotation Offset");
                double degrees = Math.round(deltaY / 90) * 90;
                //TransDimensionalSword.LOGGER.info("Degree Rounded: " + degrees);
                Quaternion rot = degrees != 0 ? new Quaternion(
                        new Vec3f(0,1,0),
                        (float)degrees * -1,
                        true
                ) : null;

                portal.rotation = rot;

                portal.setDestination(toGo);
                portal.setDestinationDimension(sword.getLastDimension(player.getMainHandStack()));
                //Spawns the portal on the server side
                //.LOGGER.info("Try Spawn");
                McHelper.spawnServerEntity(portal);
                PortalUtil.removeOverlappedPortals(world, toSpawn, portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)), portal);
                //Creates another portal at the new portals destination
                PortalUtil.completeBiWayPortal(portal);
                var goldAmount = sword.getGoldAmount(player.getMainHandStack());
                sword.setGoldAmount(player.getMainHandStack(), goldAmount - 1);
                sword.startCooldown();

            }
            //Cancels the event, so it doesn't break the block in creative mode
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
}
