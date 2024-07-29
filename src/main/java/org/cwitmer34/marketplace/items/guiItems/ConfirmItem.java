package org.cwitmer34.marketplace.items.guiItems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.util.CollectionUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.cwitmer34.marketplace.util.ItemUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Objects;

public class ConfirmItem extends AbstractItem {
	Item itemToSell;
	Item item = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName("§aConfirm").addLoreLines("§7Click to confirm your purchase"));
	ItemStack itemStack;
	ItemStack originalItem;

	double price;

	public ConfirmItem(Item itemToSell, ItemStack originalItem) {
		this.itemToSell = itemToSell;
		this.itemStack = itemToSell.getItemProvider().get();
		this.originalItem = originalItem;
		this.price = ItemUtil.getPrice(Objects.requireNonNull(itemStack.getItemMeta().getLore()));
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
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You just purchased").color(NamedTextColor.LIGHT_PURPLE)).append(itemStack.displayName()).append(Component.text(" for $" + price).color(NamedTextColor.GREEN)));

			CollectionUtil.addIfFull(player, originalItem);
		} else {
			player.sendMessage(GeneralUtil.prefix.append(Component.text(response.errorMessage).color(NamedTextColor.RED)));
		}
		Window.single().setGui(MarketplaceGUI.getGui()).open(player);
		notifyWindows();
	}
}
