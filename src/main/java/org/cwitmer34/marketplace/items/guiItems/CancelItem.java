package org.cwitmer34.marketplace.items.guiItems;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.config.Config;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Objects;

public class CancelItem extends AbstractItem {

	private final Item cancel = new SimpleItem(new ItemBuilder(ButtonsConfig.BACK).setDisplayName("§cCancel").addLoreLines("§7Click to return to the marketplace"));

	@Override
	public ItemProvider getItemProvider() {
		return cancel.getItemProvider();
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		if (!clickType.isLeftClick()) return;
		player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.purchaseCancelled));
		Window.single()
						.setTitle("Marketplace")
						.setGui(TrialMarketplace.getMarketplaceGUI().getGui())
						.open(player);
	}
}