package lol.sylvie.cuteorigins.power.effect.impl.shulker;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.power.effect.impl.DebugEffect;
import lol.sylvie.cuteorigins.state.StateManager;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ShulkerInventoryEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("shulker_inventory");
    protected ShulkerInventoryEffect() {
        super(IDENTIFIER, true);
    }

    @Override
    public void onAction(ServerPlayerEntity player) {
        player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return Text.translatable("menu.cuteorigins.shulker_inventory");
            }

            @Override
            public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_3X3, syncId, playerInventory, StateManager.getPlayerState(player).shulkerInventory, 1);
            }
        });
    }

    public static Effect fromJson(JsonObject object) {
        return new ShulkerInventoryEffect();
    }
}
