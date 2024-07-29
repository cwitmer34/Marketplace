package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;


public class BackItem extends PageItem {

	public BackItem() {
		super(false);
	}

	@Override
	public ItemProvider getItemProvider(PagedGui<?> gui) {
		ItemBuilder builder = new ItemBuilder(Material.RED_CONCRETE);
		builder.setDisplayName("§cPrevious Page").addLoreLines(
						gui.hasPreviousPage() ? "§8Back to page §7" + gui.getCurrentPage() + "/" + gui.getPageAmount() : "§7There is no previous page");
		return builder;
	}

}