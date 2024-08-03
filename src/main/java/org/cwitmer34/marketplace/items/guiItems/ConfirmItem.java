package org.cwitmer34.marketplace.items.guiItems;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.util.CollectUtil;
import org.cwitmer34.marketplace.config.Config;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class ConfirmItem extends AbstractItem {
	Item itemToSell;
	int clickedSlot;
	String itemUuid;
	String sellerName;
	Item item = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§aConfirm").addLoreLines("§7Click to confirm your purchase"));
	ItemStack itemStack;
	ItemStack originalItem;

	double price;

	public ConfirmItem(Item itemToSell, String sellerName, String itemUuid, int price, ItemStack originalItem, int clickedSlot) {
		this.clickedSlot = clickedSlot;
		this.itemToSell = itemToSell;
		this.itemUuid = itemUuid;
		this.sellerName = sellerName;
		this.itemStack = itemToSell.getItemProvider().get();
		this.originalItem = originalItem;
		this.price = price;
	}

	@Override
	public ItemProvider getItemProvider() {
		return item.getItemProvider();
	}

	@Override
	@SneakyThrows
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		if (!clickType.isLeftClick()) return;

		PlayerListing listing;

		try {
			listing = TrialMarketplace.getListingsHandler().getListing(itemUuid);
		} catch (Exception e) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.alreadyPurchased));
			Window.single().setGui(TrialMarketplace.getMarketplaceGUI().getGui()).open(player);
			return;
		}

		EconomyResponse response = TrialMarketplace.getEconomy().withdrawPlayer(player, price);
		if (response.transactionSuccess()) {
			PurchaseItemEvent event = new PurchaseItemEvent(player, originalItem, listing);
			Bukkit.getPluginManager().callEvent(event);

			TrialMarketplace.getTransactionsHandler().addTransaction(player.getUniqueId().toString(), GeneralUtil.parsePurchasePlaceholders(player.getName(), itemStack, listing, MessageConfig.itemPurchased));
			TrialMarketplace.getTransactionsHandler().addTransaction(listing.getPlayerUuid(), GeneralUtil.parsePurchasePlaceholders(player.getName(), itemStack, listing, MessageConfig.itemSold));

			TrialMarketplace.getListingsHandler().deleteListing(itemUuid);
			TrialMarketplace.getMarketplaceGUI().removeListing(itemUuid);

			if (Config.addToInvIfOnline) {
				CollectUtil.giveUnlessFullInv(player, originalItem);
			} else {
				CollectUtil.add(player.getUniqueId().toString(), originalItem);
			}

		} else if (response.balance == 0) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.balanceTooLow));
		} else {
			player.sendMessage(MessageConfig.prefix + response.errorMessage);
		}
		Window.single().setGui(TrialMarketplace.getMarketplaceGUI().getGui()).open(player);
		notifyWindows();
	}
}
