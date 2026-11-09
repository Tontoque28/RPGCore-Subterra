package com.tontoque28.rpgsubterra.client;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.ModItems;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

public class RpgSubterraClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpanishLanguageFallback.register();

        for (Item item : ModItems.all()) {
            if (item instanceof MinerArmorItem armor) {
                ArmorRenderer.register((matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
                    int tier = armor.getMinerMaterial().getTierLevel();
                    HumanoidModel<?> model = MinerArmorModel.get(tier, slot, contextModel);
                    int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
                    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(RpgSubterra.MOD_ID,
                            "textures/models/armor/miner_tier_" + tier + "_layer_" + layer + ".png");
                    ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, texture);
                }, armor);
            }
        }
    }
}
