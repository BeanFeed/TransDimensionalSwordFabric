package com.beanfeed.tdsword;

import com.beanfeed.tdsword.entity.TDEntityTypes;
import com.beanfeed.tdsword.entity.TemporaryPortal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.IPMcHelper;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.MiscHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static qouteall.imm_ptl.core.portal.PortalManipulation.getPortalCluster;

public class PortalUtil {
    public static TemporaryPortal makeTempPortal(double width, double height, Entity entity) {
        Vec3d playerLook = entity.getRotationVector();
        Pair<BlockHitResult, List<Portal>> rayTrace = IPMcHelper.rayTrace(entity.world, new RaycastContext(entity.getEyePos(), entity.getEyePos().add(playerLook.multiply(100.0)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity), true);
        BlockHitResult hitResult = (BlockHitResult)rayTrace.getLeft();
        List<Portal> hitPortals = (List)rayTrace.getRight();
        if (IPMcHelper.hitResultIsMissedOrNull(hitResult)) {
            return null;
        } else {
            Portal hitPortal;
            for(Iterator var9 = hitPortals.iterator(); var9.hasNext(); playerLook = hitPortal.transformLocalVecNonScale(playerLook)) {
                hitPortal = (Portal)var9.next();
            }

            Direction lookingDirection = Helper.getFacingExcludingAxis(playerLook, hitResult.getSide().getAxis());
            if (lookingDirection == null) {
                return null;
            } else {
                Vec3d axisH = Vec3d.of(hitResult.getSide().getVector());
                Vec3d axisW = axisH.crossProduct(Vec3d.of(lookingDirection.getOpposite().getVector()));
                Vec3d pos = Vec3d.ofCenter(hitResult.getBlockPos()).add(axisH.multiply(0.5 + height / 2.0));
                World world = hitPortals.isEmpty() ? entity.world : ((Portal)hitPortals.get(hitPortals.size() - 1)).getDestinationWorld();
                TemporaryPortal portal = new TemporaryPortal((EntityType) TDEntityTypes.TEMP_PORTAL, world);
                portal.setPosition(pos.x, pos.y, pos.z);
                portal.axisW = axisW;
                portal.axisH = axisH;
                portal.width = 1.0;
                portal.height = height;
                return portal;
            }
        }
    }

    public static void completeBiWayPortal(
            Portal portal
    ) {
        removeOverlappedPortals(
                MiscHelper.getServer().getWorld(portal.dimensionTo),
                portal.getDestPos(),
                portal.transformLocalVecNonScale(portal.getNormal().multiply(-1)),
                p -> Objects.equals(portal.specificPlayerId, p.specificPlayerId)

        );

        TemporaryPortal result = (TemporaryPortal) PortalManipulation.completeBiWayPortal(
                portal,
                TDEntityTypes.TEMP_PORTAL
        );
    }
    public static void removeOverlappedPortals(World world, Vec3d pos, Vec3d normal, Predicate<Portal> predicate) {
        getPortalCluster(world, pos, normal, predicate).forEach((e) -> {
            e.remove(Entity.RemovalReason.KILLED);
            //informer.accept(e);
        });
    }
}
