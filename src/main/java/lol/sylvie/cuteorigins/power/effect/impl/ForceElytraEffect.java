package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ForceElytraEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("force_elytra");

    protected ForceElytraEffect() {
        super(IDENTIFIER, false);
    }

    @Override
    public void onTick(ServerPlayerEntity player) {
        if (!player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)) {
            ItemStack elytra = Items.ELYTRA.getDefaultStack();
            Registry<Enchantment> enchantmentRegistry = player.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
            elytra.addEnchantment(enchantmentRegistry.getEntry(Enchantments.BINDING_CURSE.getValue()).orElseThrow(), 1);
            elytra.addEnchantment(enchantmentRegistry.getEntry(Enchantments.VANISHING_CURSE.getValue()).orElseThrow(), 1);
            elytra.set(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true));
            player.equipStack(EquipmentSlot.CHEST, elytra);
        }
    }

    @Override
    public void onRemoved(ServerPlayerEntity player) {
        ItemStack slot = player.getInventory().armor.get(2);
        if (slot.isOf(Items.ELYTRA)) player.getInventory().armor.set(2, ItemStack.EMPTY);
    }

    public static Effect fromJson(JsonObject object) {
        return new ForceElytraEffect();
    }
}
