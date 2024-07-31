package org.cwitmer34.marketplace.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;

import java.util.UUID;

public class CollectUtil {
	public static void giveUnlessFullInv(Player player, ItemStack itemStack) {
		Inventory inv = player.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				inv.addItem(itemStack);
				break;
			} else player.sendMessage("Your inventory is full!");

		}
	}

	public static void add(String playerUuid, ItemStack originalItem) {
		String serializedItem = GeneralUtil.itemStackToBase64(originalItem);
		String collectUuid = UUID.randomUUID().toString();
		TrialMarketplace.getCollectHandler().addItem(playerUuid, serializedItem);
	}
}
