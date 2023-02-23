package com.beanfeed.tdsword.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

public class TemporaryPortal extends Portal {
    public double targetWidth = 1;
    public TemporaryPortal(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onEntityTeleportedOnServer(Entity entity) {
        super.onEntityTeleportedOnServer(entity);
        String name = "Entity";
        if(entity instanceof ServerPlayerEntity sp) name = sp.getDisplayName().getString();
        //TransDimensionalSword.LOGGER.info(name + " has used a portal");
        PortalManipulation.removeConnectedPortals(this, p -> blank());
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.width != targetWidth  && !this.world.isClient()) {
            this.width = targetWidth;
            reloadPortal();
        }
    }
    private void blank(){}
    /*
    @Override
    public void tick() {
        if(!this.level.isClientSide()) {
            if(timeTillDelete > 0) {
                timeTillDelete--;
                //TransDimensionalSword.LOGGER.info("Tick");
            }
            else {
                this.remove(RemovalReason.KILLED);
            }
        }
        super.tick();
    }
     */
    private void reloadPortal() {
        this.updateCache();
        this.rectifyClusterPortals();
        this.reloadAndSyncToClient();
    }
}
