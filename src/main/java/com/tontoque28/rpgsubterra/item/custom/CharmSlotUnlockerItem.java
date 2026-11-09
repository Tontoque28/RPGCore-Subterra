package com.tontoque28.rpgsubterra.item.custom;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import eu.pb4.trinkets.api.TrinketInventory;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CharmSlotUnlockerItem extends Item {

    public static final Identifier CHARM_SLOT_MODIFIER_ID = Identifier.fromNamespaceAndPath(RpgSubterra.MOD_ID, "charm_slot_unlock");
    public static final String CHARM_SLOT_ID = "charm/charm";

    public CharmSlotUnlockerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Nullable
    public static TrinketInventory findCharmInventory(Player player) {
        return TrinketsApi.getAttachment(player).getInventory(CHARM_SLOT_ID);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!SubterraCommonConfig.enableReliquaryCharmUnlock()) {
                return InteractionResult.FAIL;
            }

            TrinketInventory inventory = findCharmInventory(player);
            if (inventory != null) {
                inventory.addSlotCountModifier(new AttributeModifier(CHARM_SLOT_MODIFIER_ID, 1.0, AttributeModifier.Operation.ADD_VALUE));

                level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.8f, 1.2f);
                player.sendOverlayMessage(Component.translatable("message.rpgsubterra.charm_slot_unlocked")
                        .withStyle(ChatFormatting.GOLD));

                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
        }

        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.rpgsubterra.charm_unlocker_desc").withStyle(ChatFormatting.GOLD));
        tooltip.accept(Component.translatable("tooltip.rpgsubterra.charm_unlocker_limit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, display, tooltip, flag);
    }
}
