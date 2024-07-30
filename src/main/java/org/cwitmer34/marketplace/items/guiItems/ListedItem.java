package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.guis.ConfirmationGUI;
import org.cwitmer34.marketplace.util.ItemUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Objects;

public class ListedItem extends AbstractItem {

	Item item;
	ItemStack originalItem;
	int price;
	String duration;

	public ListedItem(ItemStack item, int price, String duration) {
		this.item = new SimpleItem(new ItemBuilder(item).addLegacyLoreLines(Objects.requireNonNull(ItemUtil.initListingLore(item.lore(), price, duration))));
		this.originalItem = item;
		this.price = price;
		this.duration = duration;
		updateDuration();
	}

	@Override
	public ItemProvider getItemProvider() {
		return item.getItemProvider();
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
		if (!clickType.isLeftClick()) return;
		Window.single()
						.setGui(ConfirmationGUI.create(item, price, originalItem))
						.open(player);
		notifyWindows();
	}

	public void updateDuration() {
		// TODO
		item.notifyWindows();
	}
}
