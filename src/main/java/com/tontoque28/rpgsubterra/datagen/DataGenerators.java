package com.tontoque28.rpgsubterra.datagen;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = RpgSubterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
    }

    public static class ModItemModelProvider extends ItemModelProvider {
        public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, RpgSubterra.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            for (RegistryObject<Item> item : ModItems.ITEMS.getEntries()) {
                String name = item.getId().getPath();
                ResourceLocation texLoc = ResourceLocation.fromNamespaceAndPath(RpgSubterra.MOD_ID, "item/" + name);

                existingFileHelper.trackGenerated(texLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                getBuilder(name)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", texLoc);
            }
        }
    }

    public static class ModRecipeProvider extends RecipeProvider {
        public ModRecipeProvider(PackOutput output) {
            super(output);
        }

        private static TagKey<Item> modTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(RpgSubterra.MOD_ID, name));
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> writer) {
            TagKey<Item> tier1Mat = modTag("tier_1_repair");
            TagKey<Item> tier2Mat = modTag("tier_2_repair");
            TagKey<Item> tier3Mat = modTag("tier_3_repair");
            TagKey<Item> tier4Mat = modTag("tier_4_repair");
            TagKey<Item> tier5Mat = modTag("tier_5_repair");

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MINER_TIER_1_HELMET.get())
                    .pattern(" C ")
                    .pattern("MLM")
                    .define('C', Items.CANDLE)
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_HELMET)
                    .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MINER_TIER_1_CHESTPLATE.get())
                    .pattern("M M")
                    .pattern("MLM")
                    .pattern("MMM")
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_CHESTPLATE)
                    .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MINER_TIER_1_LEGGINGS.get())
                    .pattern("MMM")
                    .pattern("MLM")
                    .pattern("M M")
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_LEGGINGS)
                    .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MINER_TIER_1_BOOTS.get())
                    .pattern("M M")
                    .pattern("MLM")
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_BOOTS)
                    .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                    .save(writer);

            upgradeSet(writer, tier2Mat, Items.GLOWSTONE_DUST,
                    ModItems.MINER_TIER_1_HELMET.get(), ModItems.MINER_TIER_1_CHESTPLATE.get(),
                    ModItems.MINER_TIER_1_LEGGINGS.get(), ModItems.MINER_TIER_1_BOOTS.get(),
                    ModItems.MINER_TIER_2_HELMET.get(), ModItems.MINER_TIER_2_CHESTPLATE.get(),
                    ModItems.MINER_TIER_2_LEGGINGS.get(), ModItems.MINER_TIER_2_BOOTS.get());

            upgradeSet(writer, tier3Mat, Items.AMETHYST_SHARD,
                    ModItems.MINER_TIER_2_HELMET.get(), ModItems.MINER_TIER_2_CHESTPLATE.get(),
                    ModItems.MINER_TIER_2_LEGGINGS.get(), ModItems.MINER_TIER_2_BOOTS.get(),
                    ModItems.MINER_TIER_3_HELMET.get(), ModItems.MINER_TIER_3_CHESTPLATE.get(),
                    ModItems.MINER_TIER_3_LEGGINGS.get(), ModItems.MINER_TIER_3_BOOTS.get());

            upgradeSet(writer, tier4Mat, Items.MAGMA_CREAM,
                    ModItems.MINER_TIER_3_HELMET.get(), ModItems.MINER_TIER_3_CHESTPLATE.get(),
                    ModItems.MINER_TIER_3_LEGGINGS.get(), ModItems.MINER_TIER_3_BOOTS.get(),
                    ModItems.MINER_TIER_4_HELMET.get(), ModItems.MINER_TIER_4_CHESTPLATE.get(),
                    ModItems.MINER_TIER_4_LEGGINGS.get(), ModItems.MINER_TIER_4_BOOTS.get());

            upgradeSet(writer, tier5Mat, Items.NETHER_STAR,
                    ModItems.MINER_TIER_4_HELMET.get(), ModItems.MINER_TIER_4_CHESTPLATE.get(),
                    ModItems.MINER_TIER_4_LEGGINGS.get(), ModItems.MINER_TIER_4_BOOTS.get(),
                    ModItems.MINER_TIER_5_HELMET.get(), ModItems.MINER_TIER_5_CHESTPLATE.get(),
                    ModItems.MINER_TIER_5_LEGGINGS.get(), ModItems.MINER_TIER_5_BOOTS.get());

            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BELT_CANDLE.get())
                    .pattern(" C ")
                    .pattern("LIL")
                    .define('C', Items.CANDLE)
                    .define('L', Items.LEATHER)
                    .define('I', tier1Mat)
                    .unlockedBy("has_candle", has(Items.CANDLE))
                    .save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MINER_BELT_LANTERN.get())
                    .pattern(" G ")
                    .pattern("MBM")
                    .define('G', Items.GLASS_PANE)
                    .define('M', tier2Mat)
                    .define('B', ModItems.BELT_CANDLE.get())
                    .unlockedBy("has_belt_candle", has(ModItems.BELT_CANDLE.get()))
                    .save(writer);
        }

        private void upgradeSet(Consumer<FinishedRecipe> writer, TagKey<Item> material, Item coreItem,
                                Item prevHelmet, Item prevChest, Item prevLegs, Item prevBoots,
                                Item nextHelmet, Item nextChest, Item nextLegs, Item nextBoots) {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, nextHelmet)
                    .pattern("MCM").pattern("MPM")
                    .define('M', material).define('C', coreItem).define('P', prevHelmet)
                    .unlockedBy("has_prev_helmet", has(prevHelmet)).save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, nextChest)
                    .pattern("M M").pattern("MPM").pattern("MMM")
                    .define('M', material).define('P', prevChest)
                    .unlockedBy("has_prev_chest", has(prevChest)).save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, nextLegs)
                    .pattern("MMM").pattern("MPM").pattern("M M")
                    .define('M', material).define('P', prevLegs)
                    .unlockedBy("has_prev_legs", has(prevLegs)).save(writer);

            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, nextBoots)
                    .pattern("M M").pattern("MPM")
                    .define('M', material).define('P', prevBoots)
                    .unlockedBy("has_prev_boots", has(prevBoots)).save(writer);
        }
    }
}