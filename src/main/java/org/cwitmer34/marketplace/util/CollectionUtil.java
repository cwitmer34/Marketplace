package org.cwitmer34.marketplace.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CollectionUtil {
	public static void addIfFull(Player player, ItemStack itemStack) {
		Inventory inv = player.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				inv.addItem(itemStack);
				break;
			} else addItem(player, itemStack);

		}
	}

	private static boolean addItem(Player player, ItemStack itemStack) {
		return true;
	}
}
