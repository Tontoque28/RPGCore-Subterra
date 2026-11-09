package com.tontoque28.rpgsubterra.event;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.block.ModBlocks;
import com.tontoque28.rpgsubterra.block.SubterraLightBlock;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModArmorMaterials;
import com.tontoque28.rpgsubterra.item.custom.CharmSlotUnlockerItem;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import com.tontoque28.rpgsubterra.item.custom.MinerCurioItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = RpgSubterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiningBonusEvents {

    private static final Map<UUID, Set<BlockPos>> ACTIVE_LIGHTS = new ConcurrentHashMap<>();

    private static final int LIGHT_FLAGS = 2 | 16;

    private static final Field EXHAUSTION_FIELD = ObfuscationReflectionHelper.findField(FoodStats.class, "field_75126_c");

    @Nullable
    public static ModArmorMaterials getEquippedFullSet(PlayerEntity player) {
        ItemStack head = player.getItemBySlot(EquipmentSlotType.HEAD);
        if (!(head.getItem() instanceof MinerArmorItem)) {
            return null;
        }
        ModArmorMaterials targetMaterial = ((MinerArmorItem) head.getItem()).getMinerMaterial();
        return MinerArmorItem.hasFullMinerSet(player, targetMaterial) ? targetMaterial : null;
    }

    private static List<SlotResult> findMinerCurios(PlayerEntity player) {
        return CuriosApi.getCuriosHelper().findCurios(player, stack -> stack.getItem() instanceof MinerCurioItem);
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        float newSpeed = event.getNewSpeed();
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
        for (SlotResult slotResult : findMinerCurios(player)) {
            if (slotResult.getStack().getItem() instanceof MinerCurioItem) {
                curioSpeed += ((MinerCurioItem) slotResult.getStack().getItem()).getMiningSpeedBonus();
            }
        }

        totalSpeedBonus += curioSpeed;
        totalSpeedBonus *= SubterraCommonConfig.GLOBAL_MINING_SPEED_MULTIPLIER.get().floatValue();

        if (totalSpeedBonus > 0) {
            newSpeed *= (1.0f + totalSpeedBonus);
            event.setNewSpeed(newSpeed);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == null || player.isCreative()) return;

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial != null && setMaterial.getTierLevel() >= 2) {
            int tier = setMaterial.getTierLevel();

            float exhaustionSaved = 0.001f * tier;
            FoodStats foodData = player.getFoodData();
            try {
                float currentExhaustion = EXHAUSTION_FIELD.getFloat(foodData);
                EXHAUSTION_FIELD.setFloat(foodData, Math.max(0.0f, currentExhaustion - exhaustionSaved));
            } catch (IllegalAccessException ignored) {
            }

            ItemStack heldTool = player.getMainHandItem();
            if (heldTool.isDamageableItem() && heldTool.getDamageValue() > 0) {
                float saveChance = 0.05f + (tier * 0.10f) - 0.10f;
                if (player.getRandom().nextFloat() < saveChance) {
                    heldTool.setDamageValue(heldTool.getDamageValue() - 1);
                }
            }
        }

        BlockState state = event.getState();
        if (state.is(Tags.Blocks.ORES) && event.getWorld() instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) event.getWorld();
            float fortuneChance = 0.0f;
            for (SlotResult slotResult : findMinerCurios(player)) {
                if (slotResult.getStack().getItem() instanceof MinerCurioItem) {
                    fortuneChance += ((MinerCurioItem) slotResult.getStack().getItem()).getExtraFortuneChance();
                }
            }

            if (fortuneChance > 0 && player.getRandom().nextFloat() < fortuneChance) {
                List<ItemStack> drops = Block.getDrops(state, serverLevel, event.getPos(), null, player, player.getMainHandItem());
                for (ItemStack drop : drops) {
                    if (drop.getItem() != state.getBlock().asItem()) {
                        ItemStack bonusDrop = drop.copy();
                        bonusDrop.setCount(1);
                        Block.popResource(serverLevel, event.getPos(), bonusDrop);
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        PlayerEntity player = event.player;
        World level = player.level;
        if (level.isClientSide()) return;

        UUID uuid = player.getUUID();
        Set<BlockPos> oldPositions = ACTIVE_LIGHTS.getOrDefault(uuid, Collections.emptySet());

        if (!SubterraCommonConfig.ENABLE_DIRECTIONAL_LIGHT.get()) {
            if (!oldPositions.isEmpty()) {
                clearLightSet(level, oldPositions);
                ACTIVE_LIGHTS.remove(uuid);
            }
            return;
        }

        int maxLight = 0;
        int maxDistance = 0;
        int armorTier = 0;

        ItemStack helmetStack = player.getItemBySlot(EquipmentSlotType.HEAD);
        if (helmetStack.getItem() instanceof MinerArmorItem && ((MinerArmorItem) helmetStack.getItem()).getSlot() == EquipmentSlotType.HEAD) {
            MinerArmorItem helmet = (MinerArmorItem) helmetStack.getItem();
            armorTier = helmet.getMinerMaterial().getTierLevel();
            maxLight = helmet.getMinerMaterial().getHelmetLightLevel();
            maxDistance = helmet.getMinerMaterial().getLightBeamDistance();
        }

        int curioLight = 0;
        int curioDist = 0;
        for (SlotResult slotResult : findMinerCurios(player)) {
            if (slotResult.getStack().getItem() instanceof MinerCurioItem) {
                MinerCurioItem curio = (MinerCurioItem) slotResult.getStack().getItem();
                if (curio.getLightLevel() > curioLight) {
                    curioLight = curio.getLightLevel();
                    curioDist = curio.getLightDistance();
                }
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
        Vector3d eyePos = player.getEyePosition(1.0F);
        Vector3d lookVec = player.getLookAngle();

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

    private static void addConeCluster(World level, BlockPos center, int lightLevel, int tier, Set<BlockPos> out) {
        for (Direction dir : Direction.values()) {
            placeLightBlock(level, center.relative(dir, 1), lightLevel, out);
            if (tier >= 5) {
                placeLightBlock(level, center.relative(dir, 2), lightLevel, out);
            }
        }
    }

    private static boolean canPlaceLight(World level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir()
                || state.is(ModBlocks.LIGHT.get())
                || (state.getFluidState().getType() == Fluids.WATER && state.getBlock() == Blocks.WATER);
    }

    private static boolean placeLightBlock(World level, BlockPos pos, int lightLevel, Set<BlockPos> out) {
        if (!canPlaceLight(level, pos)) return false;

        BlockState current = level.getBlockState(pos);
        boolean waterlogged = current.getFluidState().getType() == Fluids.WATER;
        BlockState lightState = ModBlocks.LIGHT.get().defaultBlockState()
                .setValue(SubterraLightBlock.LEVEL, Math.min(15, Math.max(1, lightLevel)))
                .setValue(SubterraLightBlock.WATERLOGGED, waterlogged);

        if (!current.equals(lightState)) {

            level.setBlock(pos, lightState, LIGHT_FLAGS);
        }
        out.add(pos.immutable());
        return true;
    }

    private static void removeSingleLight(World level, @Nullable BlockPos pos) {
        if (pos != null && level.getBlockState(pos).is(ModBlocks.LIGHT.get())) {
            boolean waterlogged = level.getBlockState(pos).getValue(SubterraLightBlock.WATERLOGGED);
            BlockState restore = waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            level.setBlock(pos, restore, LIGHT_FLAGS);
        }
    }

    private static void clearLightSet(World level, Set<BlockPos> positions) {
        for (BlockPos pos : positions) {
            removeSingleLight(level, pos);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial == null) return;

        int tier = setMaterial.getTierLevel();
        DamageSource source = event.getSource();
        float amount = event.getAmount();

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
            event.setAmount(amount * (1.0f - reduction));
            return;
        }

        if (tier >= 4 && source.isFire()) {
            float fireReduction = (tier == 4) ? 0.40f : 0.65f;
            event.setAmount(amount * (1.0f - fireReduction));

            if (player.getRemainingFireTicks() > 40) {
                player.setRemainingFireTicks(40);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            boolean unlocked = event.getOriginal().getPersistentData().getBoolean(CharmSlotUnlockerItem.UNLOCKED_TAG);
            if (unlocked) {
                event.getPlayer().getPersistentData().putBoolean(CharmSlotUnlockerItem.UNLOCKED_TAG, true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        UUID uuid = player.getUUID();
        Set<BlockPos> oldPositions = ACTIVE_LIGHTS.remove(uuid);
        if (oldPositions != null && !player.level.isClientSide()) {
            clearLightSet(player.level, oldPositions);
        }
    }
}
