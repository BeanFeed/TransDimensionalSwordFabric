package com.beanfeed.tdsword.entity;

import com.beanfeed.tdsword.TransDimensionalSword;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TDEntityTypes {
    public static final EntityType<TemporaryPortal> TEMP_PORTAL = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(TransDimensionalSword.MODID, "tportal"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, TemporaryPortal::new).dimensions(EntityDimensions.fixed(1.0f, 1.0f)).build()
    );

}
