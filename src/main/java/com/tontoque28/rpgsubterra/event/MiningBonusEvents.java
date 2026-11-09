package com.tontoque28.rpgsubterra.event;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModArmorMaterials;
import com.tontoque28.rpgsubterra.item.custom.CharmSlotUnlockerItem;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import com.tontoque28.rpgsubterra.item.custom.MinerCurioItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings({"deprecation", "removal"})
@Mod.EventBusSubscriber(modid = RpgSubterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiningBonusEvents {

    private static final Map<UUID, Set<BlockPos>> ACTIVE_LIGHTS = new ConcurrentHashMap<>();

    private static final int LIGHT_FLAGS = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;

    @Nullable
    public static ModArmorMaterials getEquippedFullSet(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (!(head.getItem() instanceof MinerArmorItem headArmor)) {
            return null;
        }
        ModArmorMaterials targetMaterial = headArmor.getMinerMaterial();
        return MinerArmorItem.hasFullMinerSet(player, targetMaterial) ? targetMaterial : null;
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        float newSpeed = event.getNewSpeed();
        float totalSpeedBonus = 0.0f;

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial != null) {
            totalSpeedBonus += setMaterial.getSetMiningSpeedBonus();

            int tier = setMaterial.getTierLevel();
            if (tier >= 3 && player.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
                float waterMultiplier = switch (tier) {
                    case 3 -> 2.5f;
                    case 4 -> 3.5f;
                    default -> 4.5f;
                };
                newSpeed *= waterMultiplier;
            }
        }

        AtomicReference<Float> curioSpeed = new AtomicReference<>(0.0f);
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findCurios(stack -> stack.getItem() instanceof MinerCurioItem).forEach(slotResult -> {
                if (slotResult.stack().getItem() instanceof MinerCurioItem curio) {
                    curioSpeed.updateAndGet(v -> v + curio.getMiningSpeedBonus());
                }
            });
        });

        totalSpeedBonus += curioSpeed.get();
        totalSpeedBonus *= SubterraCommonConfig.GLOBAL_MINING_SPEED_MULTIPLIER.get().floatValue();

        if (totalSpeedBonus > 0) {
            newSpeed *= (1.0f + totalSpeedBonus);
            event.setNewSpeed(newSpeed);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.isCreative()) return;

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial != null && setMaterial.getTierLevel() >= 2) {
            int tier = setMaterial.getTierLevel();

            float exhaustionSaved = 0.001f * tier;
            player.getFoodData().setExhaustion(Math.max(0.0f, player.getFoodData().getExhaustionLevel() - exhaustionSaved));

            ItemStack heldTool = player.getMainHandItem();
            if (heldTool.isDamageableItem() && heldTool.getDamageValue() > 0) {
                float saveChance = 0.05f + (tier * 0.10f) - 0.10f;
                if (player.getRandom().nextFloat() < saveChance) {
                    heldTool.setDamageValue(heldTool.getDamageValue() - 1);
                }
            }
        }

        BlockState state = event.getState();
        if (state.is(Tags.Blocks.ORES) && event.getLevel() instanceof ServerLevel serverLevel) {
            AtomicReference<Float> fortuneChance = new AtomicReference<>(0.0f);
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                handler.findCurios(stack -> stack.getItem() instanceof MinerCurioItem).forEach(slotResult -> {
                    if (slotResult.stack().getItem() instanceof MinerCurioItem curio) {
                        fortuneChance.updateAndGet(v -> v + curio.getExtraFortuneChance());
                    }
                });
            });

            if (fortuneChance.get() > 0 && player.getRandom().nextFloat() < fortuneChance.get()) {
                List<ItemStack> drops = Block.getDrops(state, serverLevel, event.getPos(), null, player, player.getMainHandItem());
                for (ItemStack drop : drops) {
                    if (!drop.is(state.getBlock().asItem())) {
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
        Player player = event.player;
        Level level = player.level();
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

        ItemStack helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmetStack.getItem() instanceof MinerArmorItem helmet && helmet.getType() == MinerArmorItem.Type.HELMET) {
            armorTier = helmet.getMinerMaterial().getTierLevel();
            maxLight = helmet.getMinerMaterial().getHelmetLightLevel();
            maxDistance = helmet.getMinerMaterial().getLightBeamDistance();
        }

        AtomicInteger curioLight = new AtomicInteger(0);
        AtomicInteger curioDist = new AtomicInteger(0);
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findCurios(stack -> stack.getItem() instanceof MinerCurioItem).forEach(slotResult -> {
                if (slotResult.stack().getItem() instanceof MinerCurioItem curio && curio.getLightLevel() > curioLight.get()) {
                    curioLight.set(curio.getLightLevel());
                    curioDist.set(curio.getLightDistance());
                }
            });
        });

        if (curioLight.get() > maxLight) {
            maxLight = curioLight.get();
            maxDistance = curioDist.get();
        }

        if (armorTier >= 3) {
            maxLight = 15;
            maxDistance = Math.max(maxDistance, switch (armorTier) {
                case 3 -> 20;
                case 4 -> 26;
                default -> 32;
            });
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
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();

        BlockPos headPos = BlockPos.containing(eyePos);
        if (!placeLightBlock(level, headPos, Math.max(12, maxLight - 1), newPositions)) {
            placeLightBlock(level, player.blockPosition().above(), Math.max(12, maxLight - 1), newPositions);
        }

        BlockPos lastValidPos = headPos;
        for (double d = 2.0; d <= maxDistance; d += 2.0) {
            BlockPos samplePos = BlockPos.containing(eyePos.add(lookVec.scale(d)));

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
                || state.is(Blocks.LIGHT)
                || (state.getFluidState().getType() == Fluids.WATER && state.getBlock() == Blocks.WATER);
    }

    private static boolean placeLightBlock(Level level, BlockPos pos, int lightLevel, Set<BlockPos> out) {
        if (!canPlaceLight(level, pos)) return false;

        BlockState current = level.getBlockState(pos);
        boolean waterlogged = current.getFluidState().getType() == Fluids.WATER;
        BlockState lightState = Blocks.LIGHT.defaultBlockState()
                .setValue(LightBlock.LEVEL, Math.min(15, Math.max(1, lightLevel)))
                .setValue(LightBlock.WATERLOGGED, waterlogged);

        if (!current.equals(lightState)) {

            level.setBlock(pos, lightState, LIGHT_FLAGS);
        }
        out.add(pos.immutable());
        return true;
    }

    private static void removeSingleLight(Level level, @Nullable BlockPos pos) {
        if (pos != null && level.getBlockState(pos).is(Blocks.LIGHT)) {
            boolean waterlogged = level.getBlockState(pos).getValue(LightBlock.WATERLOGGED);
            BlockState restore = waterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            level.setBlock(pos, restore, LIGHT_FLAGS);
        }
    }

    private static void clearLightSet(Level level, Set<BlockPos> positions) {
        for (BlockPos pos : positions) {
            removeSingleLight(level, pos);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ModArmorMaterials setMaterial = getEquippedFullSet(player);
        if (setMaterial == null) return;

        int tier = setMaterial.getTierLevel();
        DamageSource source = event.getSource();
        float amount = event.getAmount();

        if (tier >= 3 && (source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypeTags.IS_EXPLOSION))) {
            float reduction = switch (tier) {
                case 3 -> 0.30f;
                case 4 -> 0.40f;
                default -> 0.50f;
            };
            event.setAmount(amount * (1.0f - reduction));
            return;
        }

        if (tier >= 4 && source.is(DamageTypeTags.IS_FIRE)) {
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
            event.getOriginal().reviveCaps();
            boolean unlocked = event.getOriginal().getPersistentData().getBoolean(CharmSlotUnlockerItem.UNLOCKED_TAG);
            if (unlocked) {
                event.getEntity().getPersistentData().putBoolean(CharmSlotUnlockerItem.UNLOCKED_TAG, true);
            }
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUUID();
        Set<BlockPos> oldPositions = ACTIVE_LIGHTS.remove(uuid);
        if (oldPositions != null && !player.level().isClientSide()) {
            clearLightSet(player.level(), oldPositions);
        }
    }
}