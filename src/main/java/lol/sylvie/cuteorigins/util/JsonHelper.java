package lol.sylvie.cuteorigins.util;

import com.google.gson.*;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHelper {
    public static Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Identifier jsonStringToIdentifier(JsonElement element) {
        return Identifier.of(element.getAsString());
    }

    public static Vec3d jsonListToVec3d(JsonArray element) {
        return new Vec3d(element.get(0).getAsDouble(), element.get(1).getAsDouble(), element.get(2).getAsDouble());
    }

    public static JsonObject readResource(Resource resource) {
        try (InputStream stream = resource.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(stream);
            return GSON.fromJson(reader, JsonObject.class);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load resource!", exception);
        }
    }
}
