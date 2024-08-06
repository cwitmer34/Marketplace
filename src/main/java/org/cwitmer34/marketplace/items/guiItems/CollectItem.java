package org.cwitmer34.marketplace.items.guiItems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.cwitmer34.marketplace.util.InvUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class CollectItem extends AbstractItem {
	Item item;
	ItemStack originalItem;

	public CollectItem(Item item, ItemStack originalItem) {
		this.item = item;
		this.originalItem = originalItem;
	}

	@Override
	public ItemProvider getItemProvider() {
		return item.getItemProvider();
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		if (!clickType.isLeftClick()) return;
		if (player.getInventory().firstEmpty() == -1) {
			player.sendMessage(Component.text(MessageConfig.prefix + "Your inventory is full! Item has been added to your collect.").color(NamedTextColor.RED));
			return;
		}
		try {
			TrialMarketplace.getCollectHandler().removeItem(player.getUniqueId().toString(), GeneralUtil.itemStackToBase64(originalItem));
			player.getInventory().addItem(originalItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
