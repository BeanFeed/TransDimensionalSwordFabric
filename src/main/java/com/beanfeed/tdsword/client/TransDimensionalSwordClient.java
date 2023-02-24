package com.beanfeed.tdsword.client;

import com.beanfeed.tdsword.TransDimensionalSword;
import com.beanfeed.tdsword.entity.TDEntityTypes;
import com.beanfeed.tdsword.item.TDItems;
import com.beanfeed.tdsword.screen.TDScreenHandlers;
import com.beanfeed.tdsword.screen.TDSscreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;

@Environment(EnvType.CLIENT)
public class TransDimensionalSwordClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(TDEntityTypes.TEMP_PORTAL, PortalEntityRenderer::new);

        HandledScreens.register(TDScreenHandlers.TD_SWORD_MENU, TDSscreen::new);

        ModelPredicateProviderRegistry.register(TDItems.Rune, new Identifier("tdsword:nblank"), (itemStack, clientWorld, livingEntity, seed) -> {
            NbtCompound nbt = itemStack.getOrCreateNbt();
            if(nbt.contains("waypoint")) return 1;
            return 0;
        });

        ModelPredicateProviderRegistry.register(TDItems.TDSword, new Identifier("tdsword:tdsunlit"), (itemStack, clientWorld, livingEntity, seed) -> {
            NbtCompound nbt = itemStack.getOrCreateNbt();
            if(nbt.contains("active") && nbt.getBoolean("active")) return 0;
            return 1;
        });
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
