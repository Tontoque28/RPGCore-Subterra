package com.tontoque28.rpgsubterra.item.custom;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.List;
import java.util.Map;

public class CharmSlotUnlockerItem extends Item {

    public static final ResourceLocation CHARM_SLOT_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(RpgSubterra.MOD_ID, "charm_slot_unlock");
    public static final String CHARM_GROUP = "charm";
    public static final String CHARM_SLOT = "charm";

    public CharmSlotUnlockerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Nullable
    public static TrinketInventory findCharmInventory(Player player) {
        return TrinketsApi.getTrinketComponent(player).map(component -> {
            Map<String, TrinketInventory> group = component.getInventory().get(CHARM_GROUP);
            return group == null ? null : group.get(CHARM_SLOT);
        }).orElse(null);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!SubterraCommonConfig.enableReliquaryCharmUnlock()) {
                return InteractionResultHolder.fail(stack);
            }

            TrinketInventory inventory = findCharmInventory(player);
            if (inventory != null) {
                inventory.addPersistentModifier(new AttributeModifier(CHARM_SLOT_MODIFIER_ID, 1.0, AttributeModifier.Operation.ADD_VALUE));

                level.playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 0.8f, 1.2f);
                player.displayClientMessage(Component.translatable("message.rpgsubterra.charm_slot_unlocked")
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.rpgsubterra.charm_unlocker_desc").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("tooltip.rpgsubterra.charm_unlocker_limit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
