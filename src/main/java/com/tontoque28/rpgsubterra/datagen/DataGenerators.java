package com.tontoque28.rpgsubterra.datagen;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = RpgSubterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
        }

        if (event.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
        }
    }

    public static class ModItemModelProvider extends ItemModelProvider {
        public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, RpgSubterra.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            for (RegistryObject<Item> item : ModItems.ITEMS.getEntries()) {
                String name = item.getId().getPath();
                ResourceLocation texLoc = new ResourceLocation(RpgSubterra.MOD_ID, "item/" + name);

                existingFileHelper.trackGenerated(texLoc, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");

                getBuilder(name)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", texLoc);
            }
        }
    }

    public static class ModRecipeProvider extends RecipeProvider {
        public ModRecipeProvider(DataGenerator generator) {
            super(generator);
        }

        private static ITag.INamedTag<Item> modTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(RpgSubterra.MOD_ID, name));
        }

        @Override
        protected void buildShapelessRecipes(Consumer<IFinishedRecipe> writer) {
            ITag.INamedTag<Item> tier1Mat = modTag("tier_1_repair");
            ITag.INamedTag<Item> tier2Mat = modTag("tier_2_repair");
            ITag.INamedTag<Item> tier3Mat = modTag("tier_3_repair");
            ITag.INamedTag<Item> tier4Mat = modTag("tier_4_repair");
            ITag.INamedTag<Item> tier5Mat = modTag("tier_5_repair");

            ShapedRecipeBuilder.shaped(ModItems.MINER_TIER_1_HELMET.get())
                    .pattern(" C ")
                    .pattern("MLM")
                    .define('C', Items.TORCH)
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_HELMET)
                    .unlockedBy("has_copper", has(tier1Mat))
                    .save(writer);

            ShapedRecipeBuilder.shaped(ModItems.MINER_TIER_1_CHESTPLATE.get())
                    .pattern("M M")
                    .pattern("MLM")
                    .pattern("MMM")
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_CHESTPLATE)
                    .unlockedBy("has_copper", has(tier1Mat))
                    .save(writer);

            ShapedRecipeBuilder.shaped(ModItems.MINER_TIER_1_LEGGINGS.get())
                    .pattern("MMM")
                    .pattern("MLM")
                    .pattern("M M")
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_LEGGINGS)
                    .unlockedBy("has_copper", has(tier1Mat))
                    .save(writer);

            ShapedRecipeBuilder.shaped(ModItems.MINER_TIER_1_BOOTS.get())
                    .pattern("M M")
                    .pattern("MLM")
                    .define('M', tier1Mat)
                    .define('L', Items.LEATHER_BOOTS)
                    .unlockedBy("has_copper", has(tier1Mat))
                    .save(writer);

            upgradeSet(writer, tier2Mat, Items.GLOWSTONE_DUST,
                    ModItems.MINER_TIER_1_HELMET.get(), ModItems.MINER_TIER_1_CHESTPLATE.get(),
                    ModItems.MINER_TIER_1_LEGGINGS.get(), ModItems.MINER_TIER_1_BOOTS.get(),
                    ModItems.MINER_TIER_2_HELMET.get(), ModItems.MINER_TIER_2_CHESTPLATE.get(),
                    ModItems.MINER_TIER_2_LEGGINGS.get(), ModItems.MINER_TIER_2_BOOTS.get());

            upgradeSet(writer, tier3Mat, Items.QUARTZ,
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

            ShapedRecipeBuilder.shaped(ModItems.BELT_CANDLE.get())
                    .pattern(" C ")
                    .pattern("LIL")
                    .define('C', Items.TORCH)
                    .define('L', Items.LEATHER)
                    .define('I', tier1Mat)
                    .unlockedBy("has_candle", has(Items.TORCH))
                    .save(writer);

            ShapedRecipeBuilder.shaped(ModItems.MINER_BELT_LANTERN.get())
                    .pattern(" G ")
                    .pattern("MBM")
                    .define('G', Items.GLASS_PANE)
                    .define('M', tier2Mat)
                    .define('B', ModItems.BELT_CANDLE.get())
                    .unlockedBy("has_belt_candle", has(ModItems.BELT_CANDLE.get()))
                    .save(writer);
        }

        private void upgradeSet(Consumer<IFinishedRecipe> writer, ITag.INamedTag<Item> material, Item coreItem,
                                Item prevHelmet, Item prevChest, Item prevLegs, Item prevBoots,
                                Item nextHelmet, Item nextChest, Item nextLegs, Item nextBoots) {
            ShapedRecipeBuilder.shaped(nextHelmet)
                    .pattern("MCM").pattern("MPM")
                    .define('M', material).define('C', coreItem).define('P', prevHelmet)
                    .unlockedBy("has_prev_helmet", has(prevHelmet)).save(writer);

            ShapedRecipeBuilder.shaped(nextChest)
                    .pattern("M M").pattern("MPM").pattern("MMM")
                    .define('M', material).define('P', prevChest)
                    .unlockedBy("has_prev_chest", has(prevChest)).save(writer);

            ShapedRecipeBuilder.shaped(nextLegs)
                    .pattern("MMM").pattern("MPM").pattern("M M")
                    .define('M', material).define('P', prevLegs)
                    .unlockedBy("has_prev_legs", has(prevLegs)).save(writer);

            ShapedRecipeBuilder.shaped(nextBoots)
                    .pattern("M M").pattern("MPM")
                    .define('M', material).define('P', prevBoots)
                    .unlockedBy("has_prev_boots", has(prevBoots)).save(writer);
        }
    }
}