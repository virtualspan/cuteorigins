package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PotionEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("potion_effect");

    private final int potency;
    private final Identifier effect;
    private final Condition condition;

    private final HashMap<UUID, Boolean> conditionLastTick = new HashMap<>();

    protected PotionEffect(int potency, Identifier effect, Condition condition) {
        super(IDENTIFIER, false);
        this.potency = potency;
        this.effect = effect;
        this.condition = condition;
    }

    private RegistryEntry<StatusEffect> getStatusEffect(ServerPlayerEntity player) {
        Registry<StatusEffect> statusEffects = player.getRegistryManager().getOrThrow(RegistryKeys.STATUS_EFFECT);
        return statusEffects.getEntry(effect).orElseThrow();
    }

    @Override
    public void onTick(ServerPlayerEntity player) {
        RegistryEntry<StatusEffect> statusEffect = getStatusEffect(player);
        boolean hasEffect = player.hasStatusEffect(statusEffect);
        boolean conditionResult = condition.test(player);
        UUID playerUuid = player.getUuid();
        if (conditionResult) {
            if (!hasEffect) player.addStatusEffect(new StatusEffectInstance(statusEffect, StatusEffectInstance.INFINITE, potency, false, false, false));
        } else {
            if (hasEffect && conditionLastTick.getOrDefault(playerUuid, false)) player.removeStatusEffect(statusEffect);
        }
        conditionLastTick.put(playerUuid, conditionResult);
    }

    @Override
    public void onRemoved(ServerPlayerEntity player) {
        RegistryEntry<StatusEffect> statusEffect = getStatusEffect(player);
        boolean hasEffect = player.hasStatusEffect(statusEffect);
        if (hasEffect) player.removeStatusEffect(statusEffect);
    }

    public static Effect fromJson(JsonObject object) {
        return new PotionEffect(object.get("potency").getAsInt(), JsonHelper.jsonStringToIdentifier(object.get("status_effect")), Condition.fromJson(object.getAsJsonObject("condition")));
    }
}
