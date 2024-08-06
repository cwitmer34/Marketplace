package org.cwitmer34.marketplace.util;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
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

	public static List<String> initBMLore(List<Component> componentLore, int originalPrice, int discountedPrice) {
		List<String> lore = new ArrayList<>();
		if (componentLore != null) {
			for (Component component : componentLore) {
				lore.add(GeneralUtil.legacyFromComponent(component));
			}
		}

		lore.addAll(List.of(
						"§7----------",
						"§dPrice §a$" + discountedPrice + " §c§m$" + originalPrice
		));
		return lore;
	}

	public static List<String> initListingLore(List<Component> componentLore, int price, String duration) {
		List<String> lore = new ArrayList<>();
		if (componentLore != null) {
			for (Component component : componentLore) {
				lore.add(GeneralUtil.legacyFromComponent(component));
			}
		}
		lore.addAll(List.of(
						"§7----------",
						"§dPrice §a$" + price,
						"§dExpires in §f" + duration
		));

		return lore;
	}
}

