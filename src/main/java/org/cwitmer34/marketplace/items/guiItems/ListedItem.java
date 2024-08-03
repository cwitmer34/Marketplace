package org.cwitmer34.marketplace.items.guiItems;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.guis.ConfirmationGUI;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.cwitmer34.marketplace.util.ItemUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class ListedItem extends AbstractItem {

	Item item;
	String itemUuid;
	String sellerName;
	ItemStack originalItem;
	int price;
	@Getter
	@Setter
	String duration;

	public ListedItem(ItemStack item, String sellerName, String itemUuid, int price, String duration) {
		this.item = new SimpleItem(new ItemBuilder(item).addLegacyLoreLines(Objects.requireNonNull(ItemUtil.initListingLore(item.lore(), price, duration))));
		this.itemUuid = itemUuid;
		this.sellerName = sellerName;
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
						.setGui(ConfirmationGUI.create(item, sellerName, itemUuid, price, originalItem, event.getSlot()))
						.open(player);
		notifyWindows();
	}

	public void updateDuration() {
		new BukkitRunnable() {
			@Override
			public void run() {
				// Update the duration field
				duration = GeneralUtil.updateDuration(duration);

				// Update the item's lore with the new duration
				ItemMeta meta = originalItem.getItemMeta();
				List<String> lore = meta.getLore();
				lore = ItemUtil.initListingLore(meta.lore(), price, duration);
				meta.setLore(lore);
				item.getItemProvider().get().setItemMeta(meta);

				// Notify the windows to refresh the GUI
				item.notifyWindows();
			}
		}.runTaskTimer(TrialMarketplace.getPlugin(), 0, 100); // 100 ticks = 5 seconds
	}
}
