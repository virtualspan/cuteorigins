package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DamageMultiplierEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("damage_multiplier");
    private final float multiplier;
    private final Identifier damageType;
    private final Condition condition;

    public DamageMultiplierEffect(float multiplier, Identifier damageType, Condition condition) {
        super(IDENTIFIER, false);
        this.multiplier = multiplier;
        this.damageType = damageType;
        this.condition = condition;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public RegistryEntry<DamageType> getDamageType(ServerPlayerEntity player) {
        Registry<DamageType> damageTypeRegistry = player.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE);
        return damageTypeRegistry.getEntry(damageType).orElse(null);
    }

    public Condition getCondition() {
        return condition;
    }

    public static Effect fromJson(JsonObject object) {
        return new DamageMultiplierEffect(object.get("multiplier").getAsFloat(), JsonHelper.jsonStringToIdentifier(object.get("damage_type")), Condition.fromJson(object.getAsJsonObject("condition")));
    }
}
