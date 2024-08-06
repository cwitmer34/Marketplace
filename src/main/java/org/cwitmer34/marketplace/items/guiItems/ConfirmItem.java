package org.cwitmer34.marketplace.items.guiItems;

import lombok.SneakyThrows;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.BMConfig;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;
import org.cwitmer34.marketplace.guis.BlackmarketGUI;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.InvUtil;
import org.cwitmer34.marketplace.config.Config;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.UUID;

public class ConfirmItem extends AbstractItem {
	Item itemToSell;
	int clickedSlot;
	String itemUuid;
	String sellerName;
	Item item = new SimpleItem(new ItemBuilder(ButtonsConfig.CONFIRM).setDisplayName("§aConfirm").addLoreLines("§7Click to confirm your purchase"));
	ItemStack itemStack;
	ItemStack originalItem;

	int price;

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
			listing = MarketplaceMain.getListingsHandler().getListing(itemUuid);
		} catch (Exception e) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.parseCommandPlaceholders(player.getUniqueId().toString(), MessageConfig.noLongerAvailable));
			Window.single().setGui(MarketplaceMain.getMarketplaceGUI().getGui()).open(player);
			return;
		}
		boolean isBMItem = BlackmarketGUI.getItems().containsKey(itemUuid);
		int bmMultiplier = 100 / BMConfig.discountAmt;
		double priceToCharge = price;
		double priceToCredit = price;

		if (isBMItem) {
			priceToCharge /= bmMultiplier;
			priceToCredit *= bmMultiplier;
		}

		EconomyResponse response = MarketplaceMain.economy.withdrawPlayer(player, priceToCharge);
		if (response.transactionSuccess() ) {
			OfflinePlayer playerToCredit = Bukkit.getOfflinePlayer(UUID.fromString(listing.getPlayerUuid()));
			EconomyResponse dep = MarketplaceMain.economy.depositPlayer(playerToCredit, priceToCredit);
			if (dep.transactionSuccess()) {
				ConsoleUtil.info("Deposited " + priceToCredit + " to " + playerToCredit.getName());
			} else {
				ConsoleUtil.info("Failed to deposit " + priceToCredit + " to " + playerToCredit.getName());
			}

			PurchaseItemEvent event = new PurchaseItemEvent(player, originalItem, listing);
			Bukkit.getPluginManager().callEvent(event);

			MarketplaceMain.getTransactionsHandler().addTransaction(player.getUniqueId().toString(), GeneralUtil.parsePurchasePlaceholders(player.getName(), itemStack, listing, MessageConfig.itemPurchased));
			MarketplaceMain.getTransactionsHandler().addTransaction(listing.getPlayerUuid(), GeneralUtil.parsePurchasePlaceholders(player.getName(), itemStack, listing, MessageConfig.itemSold));

			MarketplaceMain.getListingsHandler().deleteListing(itemUuid);
			MarketplaceMain.getMarketplaceGUI().removeListing(itemUuid);

			if (Config.addToInvIfOnline) {
				InvUtil.giveUnlessFullInv(player, originalItem);
			} else {
				InvUtil.addToCollect(player.getUniqueId().toString(), originalItem);
			}

		} else if (response.balance == 0) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.balanceTooLow));
		} else {
			player.sendMessage(MessageConfig.prefix + response.errorMessage);
		}
		Window.single().setGui(MarketplaceMain.getMarketplaceGUI().getGui()).open(player);
		notifyWindows();
	}
}
