package com.tontoque28.rpgsubterra.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tontoque28.rpgsubterra.RpgSubterra;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FormattedCharSequence;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpanishLanguageFallback implements SimpleSynchronousResourceReloadListener {

    private static final Gson GSON = new Gson();
    private static final ResourceLocation ID = new ResourceLocation(RpgSubterra.MOD_ID, "spanish_language_fallback");

    public static void register() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SpanishLanguageFallback());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        String currentLang = Minecraft.getInstance().getLanguageManager().getSelected();

        if (currentLang.startsWith("es_") && !currentLang.equals("es_es")) {
            loadEsEsFallback(resourceManager);
        }
    }

    private static void loadEsEsFallback(ResourceManager resourceManager) {
        ResourceLocation esEsLoc = new ResourceLocation(RpgSubterra.MOD_ID, "lang/es_es.json");
        Optional<Resource> resourceOpt = resourceManager.getResource(esEsLoc);

        if (resourceOpt.isPresent()) {
            try (InputStreamReader reader = new InputStreamReader(resourceOpt.get().open(), StandardCharsets.UTF_8)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                Map<String, String> fallbackMap = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    fallbackMap.put(entry.getKey(), entry.getValue().getAsString());
                }

                Language original = Language.getInstance();

                Language.inject(new Language() {
                    @Override
                    public String getOrDefault(String key, String defaultValue) {
                        if (fallbackMap.containsKey(key)) {
                            return fallbackMap.get(key);
                        }
                        return original.getOrDefault(key, defaultValue);
                    }

                    @Override
                    public boolean has(String key) {
                        return fallbackMap.containsKey(key) || original.has(key);
                    }

                    @Override
                    public boolean isDefaultRightToLeft() {
                        return original.isDefaultRightToLeft();
                    }

                    @Override
                    public FormattedCharSequence getVisualOrder(FormattedText text) {
                        return original.getVisualOrder(text);
                    }
                });

                RpgSubterra.LOGGER.info("RPGCore: Subterra aplicó es_es.json automáticamente para: "
                        + Minecraft.getInstance().getLanguageManager().getSelected());
            } catch (Exception e) {
                RpgSubterra.LOGGER.error("Error cargando fallback es_es.json", e);
            }
        }
    }
}
