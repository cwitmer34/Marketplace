package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class MiscItems {
	public static final Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"));

	public static final Item cancel = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cCancel").addLoreLines("§7Click to cancel your purchase"));

	public static final Item confirm = new SimpleItem(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aConfirm").addLoreLines("§7Click to confirm your purchase"));

}
