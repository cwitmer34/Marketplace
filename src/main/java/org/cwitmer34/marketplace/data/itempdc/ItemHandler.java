package org.cwitmer34.marketplace.data.itempdc;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemHandler {


	public static void setPrice(ItemStack item, double price) {
		PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
		NamespacedKey key = new NamespacedKey("marketplace", "price");
		pdc.set(key, PersistentDataType.DOUBLE, price);
	}

	public static double getPrice(ItemStack item) {
		PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
		NamespacedKey key = new NamespacedKey("marketplace", "price");
		return pdc.getOrDefault(key, PersistentDataType.DOUBLE, 0d);
	}

	public static void removePrice(ItemStack item) {
		PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
		NamespacedKey key = new NamespacedKey("marketplace", "price");
		pdc.remove(key);
	}

}
