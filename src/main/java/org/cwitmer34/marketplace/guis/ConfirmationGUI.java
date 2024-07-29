package org.cwitmer34.marketplace.guis;

import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.items.guiItems.ListedItem;
import org.cwitmer34.marketplace.items.guiItems.ListingsItem;
import org.cwitmer34.marketplace.items.guiItems.MiscItems;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;

public class ConfirmationGUI {

	public static Gui create(Item itemToSell) {
		return Gui.normal()
						.setStructure(
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >")
						.addIngredient('<', MiscItems.cancel)
						.addIngredient('o', ListingsItem::new)
						.addIngredient('>', MiscItems.confirm)
						.build();

	}
}
