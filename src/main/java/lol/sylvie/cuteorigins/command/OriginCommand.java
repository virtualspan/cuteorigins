package lol.sylvie.cuteorigins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.gui.OriginGui;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.literal;

public class OriginCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal(CuteOrigins.MOD_ID)
                .then(literal("gui")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(OriginCommand::executeGuiCommand))
                .then(literal("binds")
                        .executes(OriginCommand::executeBindsCommand)));
    }

    private static int executeGuiCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        OriginGui.openPicker(context.getSource().getPlayerOrThrow());
        return 1;
    }

    private static int executeBindsCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            OriginGui.openBinds(context.getSource().getPlayerOrThrow());
        } catch (Exception e) {
            context.getSource().sendFeedback(() -> Text.literal(e.getMessage()).formatted(Formatting.RED), false);
        }

        return 1;
    }
}
