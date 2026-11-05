package com.example.treasurechickens.client;

import com.example.treasurechickens.TreasureChickens;
import com.example.treasurechickens.entity.TreasureChickenEntity;
import com.example.treasurechickens.init.ModEntities;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;

public class TreasureChickenRenderer extends ChickenRenderer {

    private static final ResourceLocation COAL =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/coal_chicken.png");
    private static final ResourceLocation IRON =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/iron_chicken.png");
    private static final ResourceLocation GOLD =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/gold_chicken.png");
    private static final ResourceLocation EMERALD =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/emerald_chicken.png");
    private static final ResourceLocation DIAMOND =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/diamond_chicken.png");
    private static final ResourceLocation NETHERITE =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/netherite_chicken.png");
    private static final ResourceLocation QUARTZ =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/quartz_chicken.png");
    private static final ResourceLocation PRISMARINE =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/prismarine_chicken.png");
    private static final ResourceLocation SLIME =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/slime_chicken.png");
    private static final ResourceLocation ENDER_PEARL =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/ender_pearl_chicken.png");
    private static final ResourceLocation XP =
            ResourceLocation.fromNamespaceAndPath(TreasureChickens.MODID, "textures/entity/xp_chicken.png");

    public TreasureChickenRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Chicken chicken) {
        if (!(chicken instanceof TreasureChickenEntity tc)) {
            return super.getTextureLocation(chicken);
        }
        var type = tc.getType();
        if (type == ModEntities.IRON_CHICKEN.get()) return IRON;
        if (type == ModEntities.GOLD_CHICKEN.get()) return GOLD;
        if (type == ModEntities.EMERALD_CHICKEN.get()) return EMERALD;
        if (type == ModEntities.DIAMOND_CHICKEN.get()) return DIAMOND;
        if (type == ModEntities.NETHERITE_CHICKEN.get()) return NETHERITE;
        if (type == ModEntities.QUARTZ_CHICKEN.get()) return QUARTZ;
        if (type == ModEntities.PRISMARINE_CHICKEN.get()) return PRISMARINE;
        if (type == ModEntities.SLIME_CHICKEN.get()) return SLIME;
        if (type == ModEntities.ENDER_PEARL_CHICKEN.get()) return ENDER_PEARL;
        if (type == ModEntities.XP_CHICKEN.get()) return XP;
        return COAL;
    }
}