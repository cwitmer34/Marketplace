package org.cwitmer34.marketplace.items.guiItems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.items.DummyItems;
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

public class ConfirmItem extends AbstractItem {
	Item itemToSell;
	String itemUuid;
	String sellerName;
	Item item = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§aConfirm").addLoreLines("§7Click to confirm your purchase"));
	ItemStack itemStack;
	ItemStack originalItem;

	double price;

	public ConfirmItem(Item itemToSell, String sellerName, String itemUuid, int price, ItemStack originalItem) {
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
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		if (!clickType.isLeftClick()) return;
		EconomyResponse response = TrialMarketplace.getEconomy().withdrawPlayer(player, price);
		if (response.transactionSuccess()) {
			// Handle listing & transaction
			String itemName = itemStack.getItemMeta().getDisplayName().length() > 0 ? itemStack.getItemMeta().getDisplayName() : itemStack.getType().name();
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You just purchased ").color(NamedTextColor.LIGHT_PURPLE)).append(Component.text(itemName)).append(Component.text(" for $" + price).color(NamedTextColor.GREEN)).append(Component.text(" from " + sellerName).color(NamedTextColor.LIGHT_PURPLE)));
			TrialMarketplace.getListingsHandler().deleteListing(itemUuid);
			TrialMarketplace.getTransactionsHandler().addTransaction(player.getUniqueId().toString(), itemName + " for §a$§f" + price + " §dfrom §e" + sellerName);
			MarketplaceGUI.setItemsToDisplay(DummyItems.get());
			PurchaseItemEvent event = new PurchaseItemEvent(player, originalItem, (int) price);
			Bukkit.getPluginManager().callEvent(event);
			if (!Config.addToInvIfOnline) {
				CollectUtil.giveUnlessFullInv(player, originalItem);
			} else {
				CollectUtil.add(player.getUniqueId().toString(),  originalItem);
			}

		} else if (response.balance == 0) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You do not have enough money to purchase this item").color(NamedTextColor.RED)));
		} else {
			player.sendMessage(GeneralUtil.prefix.append(Component.text(response.errorMessage).color(NamedTextColor.RED)));
		}
		Window.single().setGui(MarketplaceGUI.getGui()).open(player);
		notifyWindows();
	}
}
