package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class CollectItem extends AbstractItem {
	Item item;

	public CollectItem(Item item) {
		this.item = item;
	}

	@Override
	public ItemProvider getItemProvider() {
		return item.getItemProvider();
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		if (!clickType.isLeftClick()) return;
		player.getInventory().addItem(item.getItemProvider().get());
		TrialMarketplace.getCollectHandler().removeItem(player.getUniqueId().toString(), GeneralUtil.itemStackToBase64(item.getItemProvider().get()));
	}
}
