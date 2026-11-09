package com.tontoque28.rpgsubterra.data;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketSlots;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class CharmUnlocks {

    private static final String DATA = "rpgsubterra_charm_unlocks";
    public static final String EXTRA_GROUP = "chest";
    public static final String EXTRA_SLOT = "extracharm";

    public static final class State extends SavedData {
        public final Set<UUID> unlocked = new HashSet<>();

        public State() {
            super(DATA);
        }

        @Override
        public void load(CompoundTag tag) {
            unlocked.clear();
            ListTag list = tag.getList("unlocked", 8);
            for (Tag entry : list) {
                try {
                    unlocked.add(UUID.fromString(entry.getAsString()));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            ListTag list = new ListTag();
            for (UUID uuid : unlocked) {
                list.add(StringTag.valueOf(uuid.toString()));
            }
            tag.put("unlocked", list);
            return tag;
        }
    }

    private CharmUnlocks() {
    }

    private static State state(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(State::new, DATA);
    }

    public static boolean isUnlocked(ServerPlayer player) {
        return state(player.getServer()).unlocked.contains(player.getUUID());
    }

    public static void unlock(ServerPlayer player) {
        State state = state(player.getServer());
        if (state.unlocked.add(player.getUUID())) {
            state.setDirty();
        }
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : new ArrayList<>(server.getPlayerList().getPlayers())) {
                evictIfLocked(server, player);
            }
        });
    }

    private static void evictIfLocked(MinecraftServer server, ServerPlayer player) {
        TrinketComponent component = TrinketsApi.getTrinketComponent(player);
        if (component == null) {
            return;
        }
        ItemStack stack = component.getStack(EXTRA_GROUP, EXTRA_SLOT);
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (state(server).unlocked.contains(player.getUUID())) {
            return;
        }
        int index = TrinketSlots.getAllSlotNames().indexOf(EXTRA_GROUP + ":" + EXTRA_SLOT);
        if (index < 0) {
            return;
        }
        ItemStack removed = stack.copy();
        component.getInventory().setItem(index, ItemStack.EMPTY);
        if (!player.inventory.add(removed)) {
            player.drop(removed, false);
        }
    }
}
