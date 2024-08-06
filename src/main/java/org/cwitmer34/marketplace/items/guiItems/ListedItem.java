package org.cwitmer34.marketplace.items.guiItems;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class ListedItem extends AbstractItem {

	String itemUuid;
	String sellerName;
	ItemStack originalItem;
	int price;
	@Getter
	@Setter
	String duration;

	public ListedItem(ItemStack item, String sellerName, String itemUuid, int price, String duration) {
		this.itemUuid = itemUuid;
		this.sellerName = sellerName;
		this.originalItem = item;
		this.price = price;
		this.duration = duration;
		updateDuration();
	}

	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(originalItem.clone()).addLegacyLoreLines(Objects.requireNonNull(ItemUtil.initListingLore(originalItem.clone().lore(), price, duration)));
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
//		if (!clickType.isLeftClick() || player.getName().equalsIgnoreCase(sellerName)) return;
		Window.single()
						.setGui(ConfirmationGUI.create(new SimpleItem(getItemProvider().get()), sellerName, itemUuid, price, originalItem, event.getSlot()))
						.open(player);
		notifyWindows();
	}

	public void updateDuration() {
		PlayerListing listing = TrialMarketplace.getListingsHandler().getListing(itemUuid);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!TrialMarketplace.getMarketplaceGUI().getItems().containsKey(itemUuid)) {
					cancel();
					return;
				}
				if (duration.equals("0d0h0m0s")) {
					try {
						TrialMarketplace.getMarketplaceGUI().removeListing(listing.getItemUuid());
						TrialMarketplace.getListingsHandler().deleteListing(listing.getItemUuid());
						TrialMarketplace.getCollectHandler().addItem(listing.getPlayerUuid(), listing.getSerializedItem());
						try {
							Player player = Bukkit.getPlayer(UUID.fromString(listing.getPlayerUuid()));
							if (player != null) {
								player.sendMessage("§cYour listing has expired and has been returned to your inventory.");
							}
						} catch (Exception e) {
							ConsoleUtil.warning("Could not send message to player.");
						}
					} catch (IOException e) {
						ConsoleUtil.severe("Could not remove listing from GUI.");
						throw new RuntimeException(e);
					}
					cancel();
					return;
				}
				setDuration(GeneralUtil.decrementDuration(duration));
				listing.setDuration(duration);
				listing.setPlayerListing();
				ConsoleUtil.info(listing.getDuration());
				TrialMarketplace.getMarketplaceGUI().updateListings();
			}
		}.runTaskTimerAsynchronously(TrialMarketplace.getPlugin(), 0, 20);
	}
}
