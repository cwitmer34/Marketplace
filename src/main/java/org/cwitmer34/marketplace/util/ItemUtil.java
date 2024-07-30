package org.cwitmer34.marketplace.util;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.invui.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtil {

	@Getter
	private static Map<Item, Double> priceOf = new HashMap<>();

	public static List<String> initListingLore(List<Component> componentLore, int price, String duration) {
		if (componentLore == null) return null;
		List<String> lore = new ArrayList<>();

		for (Component component : componentLore) {
			lore.add(GeneralUtil.legacyFromComponent(component));
		}

		lore.addAll(List.of(
						"§7----------",
						"§dPrice §a$" + price,
						"§dExpires in §f" + duration
		));

		return lore;
	}

	public static double getPrice(List<String> lore) {
		for (String line : lore) {
			if (line.startsWith("§fPrice    §a$")) {
				return Double.parseDouble(line.replaceAll("[0-9.]", ""));
			}
		}
		return 0;
	}
}

