package org.cwitmer34.marketplace.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.List;

public class CreateItem {



	public static ItemMeta hideAttributes(ItemMeta meta) {
		AttributeModifier modifier = new AttributeModifier(
						"generic.attackDamage",
						7.0,
						AttributeModifier.Operation.ADD_NUMBER
		);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		return meta;
	}

	public static ItemStack createItem(Material material, Component name, List<Component> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.displayName(name);
		meta.lore(lore);
		item.setItemMeta(hideAttributes(meta));
		return item;
	}
}
