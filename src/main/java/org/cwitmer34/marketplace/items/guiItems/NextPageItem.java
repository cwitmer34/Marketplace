package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.Material;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.config.Config;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

import java.util.Objects;

public class NextPageItem extends PageItem {

	public NextPageItem() {
		super(true);
	}

	@Override
	public ItemProvider getItemProvider(PagedGui<?> gui) {
		ItemBuilder builder = new ItemBuilder(ButtonsConfig.NEXT);
		builder.setDisplayName("§aNext Page").addLoreLines(gui.hasNextPage() ? "§8Go to page §7" + gui.getCurrentPage() + "/" + gui.getPageAmount() : "§7You are on the last page");
		return builder;
	}

}