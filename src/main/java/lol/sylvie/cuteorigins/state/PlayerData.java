package lol.sylvie.cuteorigins.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lol.sylvie.cuteorigins.item.ModItems;
import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.power.effect.impl.shulker.ShulkerInventory;
import lol.sylvie.cuteorigins.util.OriginRegistries;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class PlayerData {
    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Identifier.CODEC.fieldOf("origin").forGetter(PlayerData::getSelectedOrigin),
                    ShulkerInventory.CODEC.fieldOf("shulker_inventory").forGetter(PlayerData::getShulkerInventory)
            )
            .apply(instance, PlayerData::new));

    public Identifier selectedOrigin = null;
    public ShulkerInventory shulkerInventory = new ShulkerInventory();

    // Constructors
    public PlayerData() {}

    public PlayerData(Identifier identifier, ShulkerInventory inventory) {
        this.selectedOrigin = identifier;
        this.shulkerInventory = inventory;
    }

    // Getters
    public Identifier getSelectedOrigin() {
        return selectedOrigin;
    }

    public @Nullable Origin getOrigin() {
        return OriginRegistries.ORIGIN_REGISTRY.getOrigin(this.getSelectedOrigin());
    }

    public ShulkerInventory getShulkerInventory() {
        return shulkerInventory;
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
