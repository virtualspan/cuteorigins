package lol.sylvie.cuteorigins.util;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextUtil {
    public static Text getIdentifierText(Identifier identifier, String prefix, String suffix) {
        if (suffix != null && !suffix.isEmpty()) suffix = "." + suffix;
        return Text.translatable(prefix + "." + identifier.getNamespace() + "." + identifier.getPath() + suffix);
    }
}
