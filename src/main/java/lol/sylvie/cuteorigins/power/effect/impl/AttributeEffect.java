package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttributeEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("attribute_modifier");
    private final Map<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers;

    protected AttributeEffect(Map<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers) {
        super(IDENTIFIER, false);
        this.modifiers = modifiers;
    }


    protected EntityAttributeInstance getEntityAttributeInstance(ServerPlayerEntity player, RegistryEntry<EntityAttribute> attribute) {
        EntityAttributeInstance instance = player.getAttributes().getCustomInstance(attribute);
        if (instance == null) throw new IllegalStateException("Attribute from " + this.getClass().getSimpleName() + " does not exist!");
        return instance;
    }

    @Override
    public void onRespawn(ServerPlayerEntity player) {
        for (Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry : modifiers.entrySet()) {
            RegistryEntry<EntityAttribute> attribute = entry.getKey();
            EntityAttributeModifier modifier = entry.getValue();
            EntityAttributeInstance instance = getEntityAttributeInstance(player, attribute);
            if (!instance.hasModifier(modifier.id())) {
                instance.addPersistentModifier(entry.getValue());
            }
        }
    }

    @Override
    public void onChosen(ServerPlayerEntity player) {
        this.onRespawn(player);
    }

    @Override
    public void onRemoved(ServerPlayerEntity player) {
        for (Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry : modifiers.entrySet()) {
            RegistryEntry<EntityAttribute> attribute = entry.getKey();
            EntityAttributeModifier modifier = entry.getValue();
            EntityAttributeInstance instance = getEntityAttributeInstance(player, attribute);

            instance.removeModifier(modifier);
        }
    }

    public static Effect fromJson(JsonObject object) {
        List<JsonElement> modifiers = object.getAsJsonArray("modifiers").asList();
        HashMap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifierMap = new HashMap<>();
        for (JsonElement element : modifiers) {
            if (!(element instanceof JsonObject modifier)) throw new IllegalArgumentException("Modifier is not a JSON object");

            Identifier modifierId = JsonHelper.jsonStringToIdentifier(modifier.get("id"));

            Identifier attributeId = JsonHelper.jsonStringToIdentifier(modifier.get("attribute"));
            if (!Registries.ATTRIBUTE.containsId(attributeId)) {
                throw new RuntimeException("Attribute " + attributeId + " does not exist!");
            }
            RegistryEntry<EntityAttribute> attribute = Registries.ATTRIBUTE.getEntry(attributeId).orElseThrow();

            String operation = modifier.get("operation").getAsString();
            double value = modifier.get("value").getAsDouble();

            EntityAttributeModifier attributeModifier = new EntityAttributeModifier(modifierId, value, operationFromString(operation));

            modifierMap.put(attribute, attributeModifier);
        }

        return new AttributeEffect(modifierMap);
    }

    private static EntityAttributeModifier.Operation operationFromString(String value) {
        return EntityAttributeModifier.Operation.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
