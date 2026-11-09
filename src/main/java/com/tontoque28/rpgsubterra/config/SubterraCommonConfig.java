package com.tontoque28.rpgsubterra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SubterraCommonConfig {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "rpgsubterra-common.json";
    private static final long CHECK_INTERVAL_MS = 1000L;

    private static final String KEY_LIGHT = "enableDirectionalLight";
    private static final String KEY_RELIQUARY = "enableReliquaryCharmUnlock";
    private static final String KEY_SPEED = "globalMiningSpeedMultiplier";
    private static final String KEY_LOOT = "chestLootChanceMultiplier";

    private static volatile boolean enableDirectionalLight = true;
    private static volatile boolean enableReliquaryCharmUnlock = true;
    private static volatile double globalMiningSpeedMultiplier = 1.0;
    private static volatile double chestLootChanceMultiplier = 1.0;

    private static volatile long lastModified = -1L;
    private static volatile long lastCheck = 0L;

    private SubterraCommonConfig() {
    }

    public static boolean enableDirectionalLight() {
        refresh();
        return enableDirectionalLight;
    }

    public static boolean enableReliquaryCharmUnlock() {
        refresh();
        return enableReliquaryCharmUnlock;
    }

    public static double globalMiningSpeedMultiplier() {
        refresh();
        return globalMiningSpeedMultiplier;
    }

    public static double chestLootChanceMultiplier() {
        refresh();
        return chestLootChanceMultiplier;
    }

    public static void forceRefresh() {
        lastCheck = 0L;
        refresh();
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static boolean readBoolean(JsonObject root, String key, boolean fallback) {
        JsonElement element = root.get(key);
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsBoolean();
        }
        return fallback;
    }

    private static double readDouble(JsonObject root, String key, double fallback, double min, double max) {
        JsonElement element = root.get(key);
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return clamp(element.getAsDouble(), min, max);
        }
        return fallback;
    }

    private static void refresh() {
        long now = System.currentTimeMillis();
        if (now - lastCheck < CHECK_INTERVAL_MS) {
            return;
        }
        lastCheck = now;
        Path path = path();
        try {
            if (!Files.exists(path)) {
                return;
            }
            long modified = Files.getLastModifiedTime(path).toMillis();
            if (modified != lastModified) {
                read(path);
            }
        } catch (IOException exception) {
            LOGGER.warn("RPGCore: Subterra no pudo comprobar la configuracion.", exception);
        }
    }

    public static synchronized void load() {
        Path path = path();
        if (!Files.exists(path)) {
            enableDirectionalLight = true;
            enableReliquaryCharmUnlock = true;
            globalMiningSpeedMultiplier = 1.0;
            chestLootChanceMultiplier = 1.0;
            save();
            return;
        }
        try {
            read(path);
        } catch (IOException exception) {
            LOGGER.warn("RPGCore: Subterra no pudo leer la configuracion, se usan los valores por defecto.", exception);
        }
        save();
    }

    private static synchronized void read(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement parsed = new JsonParser().parse(reader);
            if (!parsed.isJsonObject()) {
                LOGGER.warn("RPGCore: Subterra encontro una configuracion invalida, se usan los valores por defecto.");
                lastModified = Files.getLastModifiedTime(path).toMillis();
                return;
            }
            JsonObject root = parsed.getAsJsonObject();
            enableDirectionalLight = readBoolean(root, KEY_LIGHT, true);
            enableReliquaryCharmUnlock = readBoolean(root, KEY_RELIQUARY, true);
            globalMiningSpeedMultiplier = readDouble(root, KEY_SPEED, 1.0, 0.1, 5.0);
            chestLootChanceMultiplier = readDouble(root, KEY_LOOT, 1.0, 0.0, 5.0);
            lastModified = Files.getLastModifiedTime(path).toMillis();
        } catch (RuntimeException exception) {
            LOGGER.warn("RPGCore: Subterra no pudo interpretar la configuracion, se usan los valores por defecto.", exception);
            lastModified = Files.getLastModifiedTime(path).toMillis();
        }
    }

    private static synchronized void save() {
        Path path = path();
        JsonObject root = new JsonObject();
        root.addProperty(KEY_LIGHT, enableDirectionalLight);
        root.addProperty(KEY_RELIQUARY, enableReliquaryCharmUnlock);
        root.addProperty(KEY_SPEED, globalMiningSpeedMultiplier);
        root.addProperty(KEY_LOOT, chestLootChanceMultiplier);
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
            lastModified = Files.getLastModifiedTime(path).toMillis();
        } catch (IOException exception) {
            LOGGER.warn("RPGCore: Subterra no pudo guardar la configuracion.", exception);
        }
    }
}
