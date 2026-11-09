package com.tontoque28.rpgsubterra.event;

import com.tontoque28.rpgsubterra.block.ModBlocks;
import com.tontoque28.rpgsubterra.block.SubterraLightBlock;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModArmorMaterials;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import com.tontoque28.rpgsubterra.item.custom.MinerCurioItem;
import com.tontoque28.rpgsubterra.mixin.FoodDataAccessor;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MiningBonusEvents {

    private static final Map<UUID, Set<BlockPos>> ACTIVE_LIGHTS = new ConcurrentHashMap<>();

    private static final int LIGHT_FLAGS = 2 | 16;

    private static final Tag<Block> ORES = TagRegistry.block(new ResourceLocation("c", "ores"));

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            onBlockBreak(level, player, pos, state);
            return true;
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : new ArrayList<>(server.getPlayerList().getPlayers())) {
                onPlayerTick(player);
            }
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> onPlayerLogout(handler.player));
    }

    @Nullable
    public static ModArmorMaterials getEquippedFullSet(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!(head.getItem() instanceof MinerArmorItem)) {
            return null;
        }
        ModArmorMaterials targetMaterial = ((MinerArmorItem) head.getItem()).getMinerMaterial();
        return MinerArmorItem.hasFullMinerSet(player, targetMaterial) ? targetMaterial : null;
    }

    private static List<MinerCurioItem> equippedCurios(Player player) {
        List<MinerCurioItem> result = new ArrayList<>();
        TrinketComponent component = TrinketsApi.getTrinketComponent(player);
        if (component == null) {
            return result;
        }
        Container inventory = component.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof MinerCurioItem) {
                result.add((MinerCurioItem) stack.getItem());
            }
        }
        return result;
    }

    public static float modifyBreakSpeed(Player player, float speed) {
        float newSpeed = speed;
        float totalSpeedBonus = 0.0f;

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial != null) {
            totalSpeedBonus += setMaterial.getSetMiningSpeedBonus();

            int tier = setMaterial.getTierLevel();
            if (tier >= 3 && player.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
                float waterMultiplier;
                switch (tier) {
                    case 3:
                        waterMultiplier = 2.5f;
                        break;
                    case 4:
                        waterMultiplier = 3.5f;
                        break;
                    default:
                        waterMultiplier = 4.5f;
                        break;
                }
                newSpeed *= waterMultiplier;
            }
        }

        float curioSpeed = 0.0f;
        for (MinerCurioItem curio : equippedCurios(player)) {
            curioSpeed += curio.getMiningSpeedBonus();
        }

        totalSpeedBonus += curioSpeed;
        totalSpeedBonus *= (float) SubterraCommonConfig.globalMiningSpeedMultiplier();

        if (totalSpeedBonus > 0) {
            newSpeed *= (1.0f + totalSpeedBonus);
            return newSpeed;
        }
        return speed;
    }

    public static void onBlockBreak(Level level, Player player, BlockPos pos, BlockState state) {
        if (player == null || player.isCreative()) return;

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial != null && setMaterial.getTierLevel() >= 2) {
            int tier = setMaterial.getTierLevel();

            float exhaustionSaved = 0.001f * tier;
            FoodData foodData = player.getFoodData();
            FoodDataAccessor accessor = (FoodDataAccessor) foodData;
            accessor.rpgsubterra$setExhaustion(Math.max(0.0f, accessor.rpgsubterra$getExhaustion() - exhaustionSaved));

            ItemStack heldTool = player.getMainHandItem();
            if (heldTool.isDamageableItem() && heldTool.getDamageValue() > 0) {
                float saveChance = 0.05f + (tier * 0.10f) - 0.10f;
                if (player.getRandom().nextFloat() < saveChance) {
                    heldTool.setDamageValue(heldTool.getDamageValue() - 1);
                }
            }
        }

        if (state.is(ORES) && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level;
            float fortuneChance = 0.0f;
            for (MinerCurioItem curio : equippedCurios(player)) {
                fortuneChance += curio.getExtraFortuneChance();
            }

            if (fortuneChance > 0 && player.getRandom().nextFloat() < fortuneChance) {
                List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, null, player, player.getMainHandItem());
                for (ItemStack drop : drops) {
                    if (drop.getItem() != state.getBlock().asItem()) {
                        ItemStack bonusDrop = drop.copy();
                        bonusDrop.setCount(1);
                        Block.popResource(serverLevel, pos, bonusDrop);
                        break;
                    }
                }
            }
        }
    }

    public static void onPlayerTick(ServerPlayer player) {
        Level level = player.level;
        if (level.isClientSide()) return;

        UUID uuid = player.getUUID();
        Set<BlockPos> oldPositions = ACTIVE_LIGHTS.getOrDefault(uuid, Collections.<BlockPos>emptySet());

        if (!SubterraCommonConfig.enableDirectionalLight()) {
            if (!oldPositions.isEmpty()) {
                clearLightSet(level, oldPositions);
                ACTIVE_LIGHTS.remove(uuid);
            }
            return;
        }

        int maxLight = 0;
        int maxDistance = 0;
        int armorTier = 0;

        ItemStack helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmetStack.getItem() instanceof MinerArmorItem && ((MinerArmorItem) helmetStack.getItem()).getSlot() == EquipmentSlot.HEAD) {
            MinerArmorItem helmet = (MinerArmorItem) helmetStack.getItem();
            armorTier = helmet.getMinerMaterial().getTierLevel();
            maxLight = helmet.getMinerMaterial().getHelmetLightLevel();
            maxDistance = helmet.getMinerMaterial().getLightBeamDistance();
        }

        int curioLight = 0;
        int curioDist = 0;
        for (MinerCurioItem curio : equippedCurios(player)) {
            if (curio.getLightLevel() > curioLight) {
                curioLight = curio.getLightLevel();
                curioDist = curio.getLightDistance();
            }
        }

        if (curioLight > maxLight) {
            maxLight = curioLight;
            maxDistance = curioDist;
        }

        if (armorTier >= 3) {
            maxLight = 15;
            int tierDistance;
            switch (armorTier) {
                case 3:
                    tierDistance = 20;
                    break;
                case 4:
                    tierDistance = 26;
                    break;
                default:
                    tierDistance = 32;
                    break;
            }
            maxDistance = Math.max(maxDistance, tierDistance);
        } else if (armorTier == 2) {
            maxLight = Math.max(maxLight, 14);
            maxDistance = Math.max(maxDistance, 15);
        } else if (armorTier == 1) {
            maxLight = Math.max(maxLight, 12);
            maxDistance = Math.max(maxDistance, 11);
        }

        if (maxLight <= 0 || !player.isAlive() || player.isSpectator()) {
            if (!oldPositions.isEmpty()) {
                clearLightSet(level, oldPositions);
                ACTIVE_LIGHTS.remove(uuid);
            }
            return;
        }

        Set<BlockPos> newPositions = new HashSet<>();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();

        BlockPos headPos = new BlockPos(eyePos);
        if (!placeLightBlock(level, headPos, Math.max(12, maxLight - 1), newPositions)) {
            placeLightBlock(level, player.blockPosition().above(), Math.max(12, maxLight - 1), newPositions);
        }

        BlockPos lastValidPos = headPos;
        for (double d = 2.0; d <= maxDistance; d += 2.0) {
            BlockPos samplePos = new BlockPos(eyePos.add(lookVec.scale(d)));

            if (!canPlaceLight(level, samplePos)) {

                placeLightBlock(level, lastValidPos, maxLight, newPositions);
                addConeCluster(level, lastValidPos, maxLight, armorTier, newPositions);
                break;
            }

            lastValidPos = samplePos;

            if (((int) d) % 4 == 0 || d >= maxDistance - 2.0) {
                placeLightBlock(level, samplePos, maxLight, newPositions);
                if (armorTier >= 4 && d >= 8.0) {
                    addConeCluster(level, samplePos, maxLight, armorTier, newPositions);
                }
            }
        }

        for (BlockPos oldPos : oldPositions) {
            if (!newPositions.contains(oldPos)) {
                removeSingleLight(level, oldPos);
            }
        }

        ACTIVE_LIGHTS.put(uuid, newPositions);
    }

    private static void addConeCluster(Level level, BlockPos center, int lightLevel, int tier, Set<BlockPos> out) {
        for (Direction dir : Direction.values()) {
            placeLightBlock(level, center.relative(dir, 1), lightLevel, out);
            if (tier >= 5) {
                placeLightBlock(level, center.relative(dir, 2), lightLevel, out);
            }
        }
    }

    private static boolean canPlaceLight(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir()
                || state.is(ModBlocks.LIGHT)
                || (state.getFluidState().getType() == Fluids.WATER && state.getBlock() == Blocks.WATER);
    }

    private static boolean placeLightBlock(Level level, BlockPos pos, int lightLevel, Set<BlockPos> out) {
        if (!canPlaceLight(level, pos)) return false;

        BlockState current = level.getBlockState(pos);
        boolean waterlogged = current.getFluidState().getType() == Fluids.WATER;
        BlockState lightState = ModBlocks.LIGHT.defaultBlockState()
                .setValue(SubterraLightBlock.LEVEL, Math.min(15, Math.max(1, lightLevel)))
                .setValue(SubterraLightBlock.WATERLOGGED, waterlogged);

        if (!current.equals(lightState)) {

            level.setBlock(pos, lightState, LIGHT_FLAGS);
        }
        out.add(pos.immutable());
        return true;
    }

    private static void removeSingleLight(Level level, @Nullable BlockPos pos) {
        if (pos != null && level.getBlockState(pos).is(ModBlocks.LIGHT)) {
            boolean waterlogged = level.getBlockState(pos).getValue(SubterraLightBlock.WATERLOGGED);
            BlockState restore = waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            level.setBlock(pos, restore, LIGHT_FLAGS);
        }
    }

    private static void clearLightSet(Level level, Set<BlockPos> positions) {
        for (BlockPos pos : positions) {
            removeSingleLight(level, pos);
        }
    }

    public static float modifyHurtAmount(Player player, DamageSource source, float amount) {
        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial == null) return amount;

        int tier = setMaterial.getTierLevel();

        if (tier >= 3 && (source == DamageSource.FALL || source.isExplosion())) {
            float reduction;
            switch (tier) {
                case 3:
                    reduction = 0.30f;
                    break;
                case 4:
                    reduction = 0.40f;
                    break;
                default:
                    reduction = 0.50f;
                    break;
            }
            return amount * (1.0f - reduction);
        }

        if (tier >= 4 && source.isFire()) {
            float fireReduction = (tier == 4) ? 0.40f : 0.65f;

            if (player.getRemainingFireTicks() > 40) {
                player.setRemainingFireTicks(40);
            }
            return amount * (1.0f - fireReduction);
        }

        return amount;
    }

    public static void onPlayerLogout(ServerPlayer player) {
        UUID uuid = player.getUUID();
        Set<BlockPos> oldPositions = ACTIVE_LIGHTS.remove(uuid);
        if (oldPositions != null && !player.level.isClientSide()) {
            clearLightSet(player.level, oldPositions);
        }
    }
}
