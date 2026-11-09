package com.tontoque28.rpgsubterra.client;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.ModItems;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

public class RpgSubterraClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpanishLanguageFallback.register();

        for (Item item : ModItems.all()) {
            if (item instanceof MinerArmorItem armor) {
                ArmorRenderer.register((poseStack, collector, stack, state, slot, light, contextModel) -> {
                    int tier = armor.getMinerMaterial().getTierLevel();
                    HumanoidModel<HumanoidRenderState> model = MinerArmorModel.get(tier, slot);
                    String folder = slot == EquipmentSlot.LEGS ? "humanoid_leggings" : "humanoid";
                    Identifier texture = Identifier.fromNamespaceAndPath(RpgSubterra.MOD_ID,
                            "textures/entity/equipment/" + folder + "/miner_tier_" + tier + ".png");
                    ArmorRenderer.submitTransformCopyingModel(contextModel, state, model, state, false, collector, poseStack,
                            RenderTypes.armorCutoutNoCull(texture), light, OverlayTexture.NO_OVERLAY, -1);
                }, armor);
            }
        }
    }
}
