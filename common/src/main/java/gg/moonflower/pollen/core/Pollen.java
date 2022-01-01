package gg.moonflower.pollen.core;

import gg.moonflower.pollen.api.advancement.AdvancementModifierManager;
import gg.moonflower.pollen.api.command.PollenSuggestionProviders;
import gg.moonflower.pollen.api.command.argument.ColorArgumentType;
import gg.moonflower.pollen.api.command.argument.EnumArgument;
import gg.moonflower.pollen.api.command.argument.TimeArgumentType;
import gg.moonflower.pollen.api.crafting.brewing.PollenBrewingRecipe;
import gg.moonflower.pollen.api.datagen.SoundDefinitionBuilder;
import gg.moonflower.pollen.api.datagen.provider.PollinatedSoundProvider;
import gg.moonflower.pollen.api.event.events.client.render.InitRendererEvent;
import gg.moonflower.pollen.api.event.events.lifecycle.ServerLifecycleEvents;
import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import gg.moonflower.pollen.api.registry.StrippingRegistry;
import gg.moonflower.pollen.api.sync.SyncedDataManager;
import gg.moonflower.pollen.core.client.render.PollenShaderTypes;
import gg.moonflower.pollen.core.network.PollenMessages;
import gg.moonflower.pollen.pinwheel.api.client.animation.AnimationManager;
import gg.moonflower.pollen.pinwheel.api.client.geometry.GeometryModelManager;
import gg.moonflower.pollen.pinwheel.api.client.render.BlockRendererRegistry;
import gg.moonflower.pollen.pinwheel.api.client.shader.ShaderConst;
import gg.moonflower.pollen.pinwheel.api.client.shader.ShaderLoader;
import gg.moonflower.pollen.pinwheel.api.client.texture.GeometryTextureManager;
import gg.moonflower.pollen.pinwheel.core.client.render.ChainedBlockRenderer;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
public class Pollen {

    public static final String MOD_ID = "pollen";
    public static final RecipeType<PollenBrewingRecipe> BREWING = RecipeType.register(MOD_ID + ":brewing");

    private static final PollinatedRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = PollinatedRegistry.create(Registry.RECIPE_SERIALIZER, MOD_ID);
    public static final Supplier<RecipeSerializer<PollenBrewingRecipe>> BREWING_SERIALIZER = RECIPE_SERIALIZERS.register("brewing", PollenBrewingRecipe::createSerializer);
    public static final Platform PLATFORM = Platform.builder(Pollen.MOD_ID)
            .commonInit(Pollen::onCommon)
            .clientInit(Pollen::onClient)
            .commonPostInit(Pollen::onCommonPost)
            .clientPostInit(Pollen::onClientPost)
            .dataInit(Pollen::onDataInit)
            .build();

    private static MinecraftServer server;

    public static void init() {
        PollenSuggestionProviders.init();
    }

    private static void onClient() {
        SyncedDataManager.initClient();
        GeometryModelManager.init();
        GeometryTextureManager.init();
        AnimationManager.init();
        AdvancementModifierManager.init();
        ShaderLoader.init();
        PollenShaderTypes.init();
        DebugInputs.init();
        InitRendererEvent.EVENT.register(ShaderConst::init);
    }

    private static void onCommon() {
        SyncedDataManager.init();
        StrippingRegistry.register(Blocks.LANTERN, Blocks.SOUL_LANTERN);
        RECIPE_SERIALIZERS.register(PLATFORM);
    }

    private static void onClientPost(Platform.ModSetupContext context) {
        // Block renderer API example
        if (!Platform.isProduction()) {
            ChainedBlockRenderer chainedBlockRenderer = new ChainedBlockRenderer();
            BlockRendererRegistry.register(Blocks.CHAIN, chainedBlockRenderer);
            BlockRendererRegistry.register(Blocks.LANTERN, chainedBlockRenderer);
            BlockRendererRegistry.register(Blocks.SOUL_LANTERN, chainedBlockRenderer);
        }
    }

    private static void onCommonPost(Platform.ModSetupContext context) {
        ArgumentTypes.register(MOD_ID + ":color", ColorArgumentType.class, new EmptyArgumentSerializer<>(ColorArgumentType::new));
        ArgumentTypes.register(MOD_ID + ":time", TimeArgumentType.class, new TimeArgumentType.Serializer());
        ArgumentTypes.register(MOD_ID + ":enum", EnumArgument.class, new EnumArgument.Serializer());
        ServerLifecycleEvents.PRE_STARTING.register(server -> {
            Pollen.server = server;
            return true;
        });
        ServerLifecycleEvents.STOPPED.register(server -> Pollen.server = null);
        PollenMessages.init();
    }

    private static void onDataInit(Platform.DataSetupContext context) {
        context.getGenerator().addProvider(new PollinatedSoundProvider(context.getGenerator(), context.getMod()) {
            @Override
            protected void registerSounds(Consumer<SoundDefinitionBuilder> registry) {
                registry.accept(SoundDefinitionBuilder.forSound(() -> SoundEvents.AMBIENT_CAVE));
            }
        });
    }

    @Nullable
    public static MinecraftServer getRunningServer() {
        return server;
    }
}
