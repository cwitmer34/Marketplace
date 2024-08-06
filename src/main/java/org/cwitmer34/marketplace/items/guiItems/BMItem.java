package org.cwitmer34.marketplace.items.guiItems;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.config.BMConfig;
import org.cwitmer34.marketplace.guis.ConfirmationGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.cwitmer34.marketplace.util.ItemUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.Objects;

public class BMItem extends AbstractItem {

	String itemUuid;
	String sellerName;
	ItemStack originalItem;
	int discountedPrice;
	int originalPrice;

	@SneakyThrows
	public BMItem(String serializedItem, String sellerName, String itemUuid, int price) {
		this.itemUuid = itemUuid;
		this.sellerName = sellerName;
		this.originalItem = GeneralUtil.itemStackFromBase64(serializedItem);
		int amountToDivide = 100 / BMConfig.discountAmt;
		this.originalPrice = price;
		this.discountedPrice = price / amountToDivide;
	}

	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(originalItem.clone()).addLegacyLoreLines(ItemUtil.initBMLore(originalItem.clone().lore(), originalPrice, discountedPrice));
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
		if (!clickType.isLeftClick()) return;
		Window.single()
						.setGui(ConfirmationGUI.create(new SimpleItem(getItemProvider().get()), sellerName, itemUuid, originalPrice, originalItem, event.getSlot()))
						.open(player);

		notifyWindows();
	}

}
