package org.cwitmer34.marketplace.guis;

import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.items.guiItems.*;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;

public class ConfirmationGUI {

	public static Gui create(Item itemToSell, String sellerName, String itemUuid, int price, ItemStack originalItem, int clickedSlot) {
		return Gui.normal()
						.setStructure(
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >",
										"< < < < o > > > >")
						.addIngredient('<', new CancelItem())
						.addIngredient('o', itemToSell)
						.addIngredient('>', new ConfirmItem(itemToSell, sellerName, itemUuid, price, originalItem, clickedSlot))
						.build();

	}
}
