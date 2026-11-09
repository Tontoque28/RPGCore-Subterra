package com.tontoque28.rpgsubterra.item.custom;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class CharmSlotUnlockerItem extends Item {

    public static final UUID CHARM_SLOT_MODIFIER_UUID = UUID.fromString("8b4e9c12-5a71-4f88-9c1d-2e5f6a7b8c9d");
    public static final String UNLOCKED_TAG = "rpgsubterra_extra_charm_unlocked";

    public CharmSlotUnlockerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!SubterraCommonConfig.ENABLE_RELIQUARY_CHARM_UNLOCK.get()) {
                return ActionResult.fail(stack);
            }

            CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
                Multimap<String, AttributeModifier> modifiers = LinkedHashMultimap.create();
                modifiers.put("charm", new AttributeModifier(CHARM_SLOT_MODIFIER_UUID, "Subterra Extra Charm", 1.0, AttributeModifier.Operation.ADDITION));
                handler.addPermanentSlotModifiers(modifiers);
                player.getPersistentData().putBoolean(UNLOCKED_TAG, true);

                level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 0.8f, 1.2f);
                player.displayClientMessage(new TranslationTextComponent("message.rpgsubterra.charm_slot_unlocked")
                        .withStyle(TextFormatting.GOLD), true);

                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            });
        }

        return ActionResult.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.charm_unlocker_desc").withStyle(TextFormatting.GOLD));
        tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.charm_unlocker_limit").withStyle(TextFormatting.DARK_GRAY, TextFormatting.ITALIC));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
