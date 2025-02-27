package lol.sylvie.cuteorigins;

import lol.sylvie.cuteorigins.data.OriginResourceReloadListener;
import lol.sylvie.cuteorigins.event.EventRegistry;
import lol.sylvie.cuteorigins.item.ModComponents;
import lol.sylvie.cuteorigins.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CuteOrigins implements ModInitializer {
    public static String MOD_ID = "cuteorigins";
    public static Logger LOGGER = LoggerFactory.getLogger("Sylvie's Origins");

    @Override
    public void onInitialize() {
        EventRegistry.register();
        ModComponents.initialize();
        ModItems.initialize();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new OriginResourceReloadListener());
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
