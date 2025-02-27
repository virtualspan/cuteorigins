package lol.sylvie.cuteorigins.item.impl;

import eu.pb4.polymer.core.api.item.PolymerItemUtils;
import eu.pb4.polymer.core.api.item.SimplePolymerItem;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.gui.OriginGui;
import lol.sylvie.cuteorigins.state.StateManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

public class OrbOfOriginItem extends SimplePolymerItem {
    public static Identifier IDENTIFIER = CuteOrigins.identifier("orb_of_origin");
    private static final Item ITEM = Items.SLIME_BALL;

    public OrbOfOriginItem() {
        super(new Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, IDENTIFIER))
                .maxCount(1)
                .rarity(Rarity.EPIC),
                ITEM);
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, TooltipType tooltipType, PacketContext context) {
        ItemStack out = PolymerItemUtils.createItemStack(itemStack, tooltipType, context);
        out.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        out.set(DataComponentTypes.ITEM_MODEL, Identifier.ofVanilla("slime_ball"));
        return out;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        itemStack.decrementUnlessCreative(1, user);
        if (user instanceof ServerPlayerEntity player) {
            StateManager.getPlayerState(player).resetOrigin(player);
            OriginGui.openPicker(player);
        }
        return ActionResult.SUCCESS;
    }
}
