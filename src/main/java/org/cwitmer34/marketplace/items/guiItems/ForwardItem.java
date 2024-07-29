package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class ForwardItem extends PageItem {

	public ForwardItem() {
		super(true);
	}

	@Override
	public ItemProvider getItemProvider(PagedGui<?> gui) {
		ItemBuilder builder = new ItemBuilder(Material.LIME_CONCRETE);
		builder.setDisplayName("§aNext Page").addLoreLines(gui.hasNextPage() ? "§8Go to page §7" + gui.getCurrentPage() + "/" + gui.getPageAmount() : "§7You are on the last page");

		return builder;
	}

}