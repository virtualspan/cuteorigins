package lol.sylvie.cuteorigins.item;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.item.impl.KeybindItem;
import lol.sylvie.cuteorigins.item.impl.OrbOfOriginItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
public class ModItems {
    public static Item ORB_OF_ORIGIN = register(new OrbOfOriginItem(), OrbOfOriginItem.IDENTIFIER);
    public static Item KEYBIND_ITEM = register(new KeybindItem(), KeybindItem.IDENTIFIER);


    public static final ItemGroup ITEM_GROUP = PolymerItemGroupUtils.builder()
            .displayName(Text.translatable("itemGroup.cuteorigins.item_group"))
            .icon(Items.SLIME_BALL::getDefaultStack).entries((context, entries) -> {
                entries.add(ORB_OF_ORIGIN);
            }).build();

    public static Item register(Item item, Identifier identifier) {
        return Registry.register(Registries.ITEM, identifier, item);
    }

    public static void initialize() {
        PolymerItemGroupUtils.registerPolymerItemGroup(CuteOrigins.identifier("item_group"), ITEM_GROUP);
    }
}
