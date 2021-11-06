package gg.moonflower.pollen.api.registry.forge;

import gg.moonflower.pollen.api.platform.Platform;
import gg.moonflower.pollen.api.platform.forge.ForgePlatform;
import gg.moonflower.pollen.api.registry.PollinatedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@ApiStatus.Internal
public class PollinatedRegistryImpl<T extends IForgeRegistryEntry<T>> extends PollinatedRegistry<T> {
    private final DeferredRegister<T> registry;

    private PollinatedRegistryImpl(ForgeRegistry<T> registry, String modId) {
        super(modId);
        this.registry = DeferredRegister.create(registry, modId);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> PollinatedRegistry<T> create(Registry<T> registry, String modId) {
        ForgeRegistry forgeRegistry = getForgeRegistry((ResourceKey) registry.key());
        if (forgeRegistry == null)
            return PollinatedRegistry.createVanilla(registry, modId);
        return new PollinatedRegistryImpl(forgeRegistry, modId);
    }

    @Nullable
    private static <T extends IForgeRegistryEntry<T>> ForgeRegistry<T> getForgeRegistry(ResourceKey<Registry<T>> registryKey) {
        return RegistryManager.ACTIVE.getRegistry(registryKey);
    }

    @Override
    public <I extends T> Supplier<I> register(String id, Supplier<I> object) {
        return this.registry.register(id, object);
    }

    @Override
    protected void onRegister(Platform mod) {
        this.registry.register(((ForgePlatform) mod).getEventBus());
    }
}