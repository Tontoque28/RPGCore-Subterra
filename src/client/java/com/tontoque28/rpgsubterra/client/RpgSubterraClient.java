package com.tontoque28.rpgsubterra.client;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.ModItems;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

public class RpgSubterraClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpanishLanguageFallback.register();

        for (Item item : ModItems.all()) {
            if (item instanceof MinerArmorItem armor) {
                ArmorRenderer.register((matrices, vertexConsumers, stack, state, slot, light, contextModel) -> {
                    int tier = armor.getMinerMaterial().getTierLevel();
                    HumanoidModel<HumanoidRenderState> model = MinerArmorModel.get(tier, slot, contextModel);
                    String folder = slot == EquipmentSlot.LEGS ? "humanoid_leggings" : "humanoid";
                    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(RpgSubterra.MOD_ID,
                            "textures/entity/equipment/" + folder + "/miner_tier_" + tier + ".png");
                    ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, texture);
                }, armor);
            }
        }
    }
}
