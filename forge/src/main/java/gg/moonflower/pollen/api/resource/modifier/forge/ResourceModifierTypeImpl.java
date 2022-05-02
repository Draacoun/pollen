package gg.moonflower.pollen.api.resource.modifier.forge;

import gg.moonflower.pollen.api.resource.modifier.ResourceModifierType;
import gg.moonflower.pollen.api.resource.modifier.serializer.DataModifierSerializer;
import gg.moonflower.pollen.api.resource.modifier.serializer.ModifierSerializer;
import gg.moonflower.pollen.api.resource.modifier.serializer.ResourceModifierSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ResourceModifierTypeImpl extends ForgeRegistryEntry<ResourceModifierTypeImpl> implements ResourceModifierType {

    private final ModifierSerializer serializer;

    private ResourceModifierTypeImpl(ModifierSerializer serializer) {
        this.serializer = serializer;
    }

    public static ResourceModifierType create(DataModifierSerializer serializer) {
        return new ResourceModifierTypeImpl(serializer);
    }

    public static ResourceModifierType create(ResourceModifierSerializer serializer) {
        return new ResourceModifierTypeImpl(serializer);
    }

    @Override
    public ModifierSerializer getSerializer() {
        return serializer;
    }
}
