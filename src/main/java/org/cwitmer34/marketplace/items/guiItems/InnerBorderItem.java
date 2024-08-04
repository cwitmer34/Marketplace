package org.cwitmer34.marketplace.items.guiItems;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.config.Config;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Objects;

public class InnerBorderItem extends AbstractItem {

	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(ButtonsConfig.INNER_BORDER).setDisplayName("§r");
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
	}
}