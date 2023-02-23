package com.beanfeed.tdsword.client;

import com.beanfeed.tdsword.client.renderers.TemporaryPortalRenderer;
import com.beanfeed.tdsword.entity.TDEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import org.apache.commons.lang3.Validate;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;
import qouteall.imm_ptl.core.render.PortalRenderer;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class TdswordClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(TDEntityTypes.TEMP_PORTAL, PortalEntityRenderer::new);
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
