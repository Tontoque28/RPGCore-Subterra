package com.tontoque28.rpgsubterra.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.UUID;

public class CharmSlotUnlockerItem extends Item {

    public static final UUID CHARM_SLOT_MODIFIER_UUID = UUID.fromString("8b4e9c12-5a71-4f88-9c1d-2e5f6a7b8c9d");
    public static final String UNLOCKED_TAG = "rpgsubterra_extra_charm_unlocked";

    public CharmSlotUnlockerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!com.tontoque28.rpgsubterra.config.SubterraCommonConfig.ENABLE_RELIQUARY_CHARM_UNLOCK.get()) {
                return InteractionResultHolder.fail(stack);
            }

            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.addPermanentSlotModifier("charm", CHARM_SLOT_MODIFIER_UUID, "Subterra Extra Charm", 1.0, AttributeModifier.Operation.ADDITION);
                player.getPersistentData().putBoolean(UNLOCKED_TAG, true);

                level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.8f, 1.2f);
                player.displayClientMessage(Component.translatable("message.rpgsubterra.charm_slot_unlocked")
                        .withStyle(ChatFormatting.GOLD), true);

                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            });
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.rpgsubterra.charm_unlocker_desc").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.rpgsubterra.charm_unlocker_limit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}