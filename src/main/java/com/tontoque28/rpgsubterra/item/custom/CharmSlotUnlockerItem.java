package com.tontoque28.rpgsubterra.item.custom;

import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.data.CharmUnlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class CharmSlotUnlockerItem extends Item {

    public CharmSlotUnlockerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!SubterraCommonConfig.enableReliquaryCharmUnlock()) {
                return InteractionResultHolder.fail(stack);
            }

            if (player instanceof ServerPlayer) {
                CharmUnlocks.unlock((ServerPlayer) player);

                level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.8f, 1.2f);
                player.displayClientMessage(new TranslatableComponent("message.rpgsubterra.charm_slot_unlocked")
                        .withStyle(ChatFormatting.GOLD), true);

                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.charm_unlocker_desc").withStyle(ChatFormatting.GOLD));
        tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.charm_unlocker_limit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
