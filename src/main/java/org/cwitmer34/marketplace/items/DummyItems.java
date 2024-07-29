package org.cwitmer34.marketplace.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.ArrayList;
import java.util.List;

public class DummyItems {
	public static List<Item> get() {
		List<ItemStack> itemStacks = List.of(new ItemStack(Material.DIAMOND, 14), new ItemStack(Material.EMERALD, 2), new ItemStack(Material.GOLD_INGOT, 64), new ItemStack(Material.IRON_INGOT, 32), new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.EMERALD_BLOCK, 4));
		List<Item> items = new ArrayList<>();
		for (ItemStack itemStack : itemStacks) {
			items.add(new SimpleItem(itemStack));
		}
		return items;
	}
}
