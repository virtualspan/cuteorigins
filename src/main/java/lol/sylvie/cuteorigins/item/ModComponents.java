package lol.sylvie.cuteorigins.item;

import com.mojang.serialization.Codec;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import eu.pb4.polymer.core.api.utils.PolymerUtils;
import lol.sylvie.cuteorigins.CuteOrigins;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<String> POWER_KEYBIND = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            CuteOrigins.identifier("power_keybind"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static final ComponentType<Boolean> ON_COOLDOWN = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            CuteOrigins.identifier("power_on_cooldown"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static void initialize() {
        CuteOrigins.LOGGER.info("Registering {} components", CuteOrigins.MOD_ID);

        PolymerComponent.registerDataComponent(POWER_KEYBIND);
        PolymerComponent.registerDataComponent(ON_COOLDOWN);
    }
}
