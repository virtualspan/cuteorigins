package lol.sylvie.cuteorigins.gui.binds;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.gui.SimpleGui;
import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.state.StateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class JavaOriginBindMenu extends SimpleGui {
    public JavaOriginBindMenu(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
    }
    public static void open(ServerPlayerEntity player) {
        JavaOriginBindMenu gui = new JavaOriginBindMenu(player);
        gui.open();
        gui.updateGui();
    }

    protected void updateGui() {
        this.setTitle(Text.translatable("menu.cuteorigins.binds"));

        Origin origin = StateManager.getPlayerState(this.player).getOrigin();
        if (origin == null) {
            this.close();
            return;
        }

        int i = 0;
        for (ItemStack item : origin.getKeybinds()) {
            GuiElement keybind = new GuiElement(item, (i1, clickType, slotActionType, slotGuiInterface) -> {
                player.giveItemStack(item);
                this.updateGui();
            });
            this.setSlot(i, keybind);
            i++;
        }
    }
}
