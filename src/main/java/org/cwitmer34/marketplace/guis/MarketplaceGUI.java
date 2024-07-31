package org.cwitmer34.marketplace.guis;

import lombok.Getter;
import lombok.Setter;
import org.cwitmer34.marketplace.items.guiItems.*;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceGUI {
	@Getter
	@Setter
	private static List<Item> itemsToDisplay = new ArrayList<>();
	@Getter
	private static Gui gui = PagedGui.items().setStructure(
									"# # # # # # # # #",
									"# x x x x x x x #",
									"# x x x x x x x #",
									"# x x x x x x x #",
									"# x x x x x x x #",
									"# # # < o > # # #")
					.addIngredient('#', MiscItems.border)
					.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
					.setContent(itemsToDisplay)
					.addIngredient('<', new BackItem())
					.addIngredient('o', new ListingsItem())
					.addIngredient('>', new ForwardItem())
					.build();

	public static void updateItemsToDisplay() {
		((PagedGui) gui).setContent(itemsToDisplay);
	}

	public static void addItem(Item item) {
		itemsToDisplay.addFirst(item);
		updateItemsToDisplay();
	}

	public static void removeItem(Item item) {
		itemsToDisplay.remove(item);
		updateItemsToDisplay();
	}

	public static void setItems() {
		((PagedGui) gui).setContent(itemsToDisplay);
	}
}
