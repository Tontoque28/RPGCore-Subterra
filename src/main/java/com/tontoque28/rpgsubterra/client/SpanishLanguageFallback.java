package com.tontoque28.rpgsubterra.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tontoque28.rpgsubterra.RpgSubterra;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = RpgSubterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpanishLanguageFallback {

    private static final Gson GSON = new Gson();

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        if (manager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) manager).registerReloadListener((ISelectiveResourceReloadListener) (resourceManager, predicate) -> {
                String currentLang = Minecraft.getInstance().getLanguageManager().getSelected().getCode();

                if (currentLang.startsWith("es_") && !currentLang.equals("es_es")) {
                    loadEsEsFallback(resourceManager);
                }
            });
        }
    }

    private static void loadEsEsFallback(IResourceManager resourceManager) {
        ResourceLocation esEsLoc = new ResourceLocation(RpgSubterra.MOD_ID, "lang/es_es.json");

        try (IResource resource = resourceManager.getResource(esEsLoc);
             InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            Map<String, String> fallbackMap = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                fallbackMap.put(entry.getKey(), entry.getValue().getAsString());
            }

            LanguageMap original = LanguageMap.getInstance();

            LanguageMap.inject(new LanguageMap() {
                @Override
                public String getOrDefault(String key) {
                    if (fallbackMap.containsKey(key)) {
                        return fallbackMap.get(key);
                    }
                    return original.getOrDefault(key);
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
                public IReorderingProcessor getVisualOrder(ITextProperties text) {
                    return original.getVisualOrder(text);
                }
            });

            RpgSubterra.LOGGER.info("RPGCore: Subterra aplicó es_es.json automáticamente para: "
                    + Minecraft.getInstance().getLanguageManager().getSelected().getCode());
        } catch (Exception e) {
            RpgSubterra.LOGGER.error("Error cargando fallback es_es.json", e);
        }
    }
}
