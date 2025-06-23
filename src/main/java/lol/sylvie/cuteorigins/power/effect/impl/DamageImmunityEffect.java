package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class DamageImmunityEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("damage_immunity");
    private final List<Identifier> damageTypes;
    private final Condition condition;

    public DamageImmunityEffect(List<Identifier> damageTypes, Condition condition) {
        super(IDENTIFIER, false);
        this.damageTypes = damageTypes;
        this.condition = condition;
    }

    public DamageType getDamageType(PlayerEntity player, Identifier identifier) {
        Registry<DamageType> damageTypeRegistry = player.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE);
        return damageTypeRegistry.get(identifier);
    }

    public boolean isImmuneTo(PlayerEntity player, DamageType type) {
        return damageTypes.stream().anyMatch(identifier -> getDamageType(player, identifier).equals(type)) && this.condition.test(player);
    }

    public static Effect fromJson(JsonObject object) {
        return new DamageImmunityEffect(object.getAsJsonArray("damage_types").asList().stream().map(JsonHelper::jsonStringToIdentifier).toList(), Condition.fromJson(object.getAsJsonObject("condition")));
    }
}