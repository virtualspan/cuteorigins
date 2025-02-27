package lol.sylvie.cuteorigins.state;

import lol.sylvie.cuteorigins.item.ModItems;
import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.power.effect.impl.shulker.ShulkerInventory;
import lol.sylvie.cuteorigins.util.OriginRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class PlayerData {
    public Identifier selectedOrigin = null;
    public ShulkerInventory shulkerInventory = new ShulkerInventory();

    public @Nullable Origin getOrigin() {
        return OriginRegistries.ORIGIN_REGISTRY.getOrigin(selectedOrigin);
    }

    public void setOrigin(ServerPlayerEntity player, Origin origin) {
        Origin oldOrigin = getOrigin();
        if (oldOrigin != null) oldOrigin.onRemoved(player);
        this.selectedOrigin = origin.identifier();
        origin.onChosen(player);

        player.getInventory().remove(i -> i.isOf(ModItems.KEYBIND_ITEM), -1, player.playerScreenHandler.getCraftingInput());
        for (ItemStack item : origin.getKeybinds()) {
            player.giveItemStack(item);
        }
    }

    public void resetOrigin(ServerPlayerEntity player) {
        Origin oldOrigin = getOrigin();
        if (oldOrigin != null) oldOrigin.onRemoved(player);
        this.selectedOrigin = null;
    }
}
