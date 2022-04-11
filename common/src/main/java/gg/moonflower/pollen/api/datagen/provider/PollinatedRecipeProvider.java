package gg.moonflower.pollen.api.datagen.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.moonflower.pollen.api.platform.Platform;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Ocelot
 * @since 1.0.0
 */
public abstract class PollinatedRecipeProvider extends SimpleConditionalDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public PollinatedRecipeProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(HashCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = new HashSet<>();
        this.buildRecipes(finishedRecipe -> {
            if (!set.add(finishedRecipe.getId()))
                throw new IllegalStateException("Duplicate recipe " + finishedRecipe.getId());

            try {
                JsonObject json = finishedRecipe.serializeRecipe();
                this.injectConditions(finishedRecipe.getId(), json);
                DataProvider.save(GSON, cache, json, path.resolve("data/" + finishedRecipe.getId().getNamespace() + "/recipes/" + finishedRecipe.getId().getPath() + ".json"));
            } catch (IOException e) {
                LOGGER.error("Couldn't save recipe {}", path, e);
            }

            JsonObject jsonObject = finishedRecipe.serializeAdvancement();
            if (jsonObject != null) {
                try {
                    this.injectConditions(finishedRecipe.getId(), jsonObject);
                    DataProvider.save(GSON, cache, jsonObject, path.resolve("data/" + finishedRecipe.getId().getNamespace() + "/advancements/" + finishedRecipe.getAdvancementId().getPath() + ".json"));
                } catch (IOException e) {
                    LOGGER.error("Couldn't save recipe advancement {}", path, e);
                }
            }
        });
    }

    /**
     * Generates all recipes into the specified consumer.
     *
     * @param consumer The registry for recipes
     */
    protected abstract void buildRecipes(Consumer<FinishedRecipe> consumer);

    @ExpectPlatform
    public static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, @Nullable String string) {
    }

    @ExpectPlatform
    public static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, @Nullable String string, int i) {
    }

    @ExpectPlatform
    public static void oreSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> list, ItemLike itemLike, float f, int i, String string) {
    }

    @ExpectPlatform
    public static void oreBlasting(Consumer<FinishedRecipe> consumer, List<ItemLike> list, ItemLike itemLike, float f, int i, String string) {
    }

    @ExpectPlatform
    public static void oreCooking(Consumer<FinishedRecipe> consumer, SimpleCookingSerializer<?> simpleCookingSerializer, List<ItemLike> list, ItemLike itemLike, float f, int i, String string, String string2) {
    }

    @ExpectPlatform
    public static void netheriteSmithing(Consumer<FinishedRecipe> consumer, Item item, Item item2) {
    }

    @ExpectPlatform
    public static void planksFromLog(Consumer<FinishedRecipe> consumer, ItemLike itemLike, Tag<Item> tag) {
    }

    @ExpectPlatform
    public static void planksFromLogs(Consumer<FinishedRecipe> consumer, ItemLike itemLike, Tag<Item> tag) {
    }

    @ExpectPlatform
    public static void woodFromLogs(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void woodenBoat(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static RecipeBuilder buttonBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapelessRecipeBuilder.shapeless(itemLike).requires(ingredient);
    }

    @ExpectPlatform
    public static RecipeBuilder doorBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(itemLike, 3).define('#', ingredient).pattern("##").pattern("##").pattern("##");
    }

    @ExpectPlatform
    public static RecipeBuilder fenceBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static RecipeBuilder fenceGateBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static void pressurePlate(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static RecipeBuilder pressurePlateBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static void slab(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static RecipeBuilder slabBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static RecipeBuilder stairBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static RecipeBuilder trapdoorBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static RecipeBuilder signBuilder(ItemLike itemLike, Ingredient ingredient) {
        return Platform.error();
    }

    @ExpectPlatform
    public static void coloredWoolFromWhiteWoolAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void carpet(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void coloredCarpetFromWhiteCarpetAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void bedFromPlanksAndWool(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void bedFromWhiteBedAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void banner(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void stainedGlassFromGlassAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void stainedGlassPaneFromStainedGlass(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void stainedGlassPaneFromGlassPaneAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void coloredTerracottaFromTerracottaAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void concretePowder(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    public static void candle(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        RecipeProvider.candle(consumer, itemLike, itemLike2);
    }

    public static void wall(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        RecipeProvider.wall(consumer, itemLike, itemLike2);
    }

    public static RecipeBuilder wallBuilder(ItemLike itemLike, Ingredient ingredient) {
        return RecipeProvider.wallBuilder(itemLike, ingredient);
    }

    public static void polished(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        RecipeProvider.polished(consumer, itemLike, itemLike2);
    }

    public static RecipeBuilder polishedBuilder(ItemLike itemLike, Ingredient ingredient) {
        return RecipeProvider.polishedBuilder(itemLike, ingredient);
    }

    public static void cut(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        RecipeProvider.cut(consumer, itemLike, itemLike2);
    }

    public static ShapedRecipeBuilder cutBuilder(ItemLike itemLike, Ingredient ingredient) {
        return RecipeProvider.cutBuilder(itemLike, ingredient);
    }

    public static void chiseled(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        RecipeProvider.chiseled(consumer, itemLike, itemLike2);
    }

    public static ShapedRecipeBuilder chiseledBuilder(ItemLike itemLike, Ingredient ingredient) {
        return RecipeProvider.chiseledBuilder(itemLike, ingredient);
    }

    @ExpectPlatform
    public static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, int i) {
    }

    @ExpectPlatform
    public static void smeltingResultFromBase(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
    }

    @ExpectPlatform
    public static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, String string, String string2) {
    }

    @ExpectPlatform
    public static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, String string, String string2) {
    }

    @ExpectPlatform
    public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, String string, @Nullable String string2, String string3, @Nullable String string4) {
    }

    @ExpectPlatform
    public static void cookRecipes(Consumer<FinishedRecipe> consumer, String string, SimpleCookingSerializer<?> simpleCookingSerializer, int i) {
    }

    @ExpectPlatform
    public static void simpleCookingRecipe(Consumer<FinishedRecipe> consumer, String string, SimpleCookingSerializer<?> simpleCookingSerializer, int i, ItemLike itemLike, ItemLike itemLike2, float f) {
    }

    @ExpectPlatform
    public static void waxRecipes(Consumer<FinishedRecipe> consumer) {
    }

    @ExpectPlatform
    public static void generateRecipes(Consumer<FinishedRecipe> consumer, BlockFamily blockFamily) {
    }

    @ExpectPlatform
    public static Block getBaseBlock(BlockFamily blockFamily, BlockFamily.Variant variant) {
        return Platform.error();
    }

    @ExpectPlatform
    public static EnterBlockTrigger.TriggerInstance insideOf(Block block) {
        return Platform.error();
    }

    @ExpectPlatform
    public static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints ints, ItemLike itemLike) {
        return Platform.error();
    }

    @ExpectPlatform
    public static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike) {
        return Platform.error();
    }

    @ExpectPlatform
    public static InventoryChangeTrigger.TriggerInstance has(Tag<Item> tag) {
        return Platform.error();
    }

    @ExpectPlatform
    public static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... itemPredicates) {
        return Platform.error();
    }

    @ExpectPlatform
    public static String getHasName(ItemLike itemLike) {
        return Platform.error();
    }

    @ExpectPlatform
    public static String getItemName(ItemLike itemLike) {
        return Platform.error();
    }

    @ExpectPlatform
    public static String getSimpleRecipeName(ItemLike itemLike) {
        return Platform.error();
    }

    @ExpectPlatform
    public static String getConversionRecipeName(ItemLike itemLike, ItemLike itemLike2) {
        return Platform.error();
    }

    @ExpectPlatform
    public static String getSmeltingRecipeName(ItemLike itemLike) {
        return Platform.error();
    }

    @ExpectPlatform
    public static String getBlastingRecipeName(ItemLike itemLike) {
        return Platform.error();
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
