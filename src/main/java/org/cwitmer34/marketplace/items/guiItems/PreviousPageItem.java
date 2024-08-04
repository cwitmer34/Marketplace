package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.Material;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.config.Config;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

import java.util.Objects;


public class PreviousPageItem extends PageItem {

	public PreviousPageItem() {
		super(false);
	}

	@Override
	public ItemProvider getItemProvider(PagedGui<?> gui) {
		ItemBuilder builder;
		builder = new ItemBuilder(ButtonsConfig.PREVIOUS);
		builder.setDisplayName("§cPrevious Page").addLoreLines(
						gui.hasPreviousPage() ? "§8Back to page §7" + gui.getCurrentPage() + "/" + gui.getPageAmount() : "§7There is no previous page");
		return builder;
	}

}