package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.cwitmer34.marketplace.guis.ConfirmationGUI;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Objects;

public class ListingsItem extends AbstractItem {

	private int count;

	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(Material.BLACK_CONCRETE).setDisplayName("§eView Listings").addLoreLines("§7Click to view your current listings");
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
		if (!clickType.isLeftClick()) return;

		notifyWindows();
	}

}

