package com.beanfeed.tdsword.client;

import com.beanfeed.tdsword.entity.TDEntityTypes;
import com.beanfeed.tdsword.screen.TDScreenHandlers;
import com.beanfeed.tdsword.screen.TDSscreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

@Environment(EnvType.CLIENT)
public class TransDimensionalSwordClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(TDEntityTypes.TEMP_PORTAL, PortalEntityRenderer::new);

        HandledScreens.register(TDScreenHandlers.TD_SWORD_MENU, TDSscreen::new);
    }

    /*
    public void initPortalRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Arrays.stream(new EntityType<?>[]{
                TDEntityTypes.TEMP_PORTAL.get()
        }).peek(
                Validate::notNull
        ).forEach(
                entityType -> event.registerEntityRenderer(entityType, (EntityRendererProvider) PortalEntityRenderer::new
                ));
    }

     */

}
