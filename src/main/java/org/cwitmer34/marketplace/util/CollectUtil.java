package org.cwitmer34.marketplace.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;

public class CollectUtil {
	public static void giveUnlessFullInv(final Player player, final ItemStack itemStack) {
		Inventory inv = player.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				inv.addItem(itemStack);
				break;
			} else player.sendMessage("Your inventory is full!");

		}
	}

	public static void add(final String playerUuid, final ItemStack originalItem) {
		final String serializedItem = GeneralUtil.itemStackToBase64(originalItem);
		TrialMarketplace.getCollectHandler().addItem(playerUuid, serializedItem);
	}
}
