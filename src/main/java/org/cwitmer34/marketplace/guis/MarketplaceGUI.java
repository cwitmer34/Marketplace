package org.cwitmer34.marketplace.guis;

import lombok.Getter;
import org.cwitmer34.marketplace.items.guiItems.*;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceGUI {
	@Getter
	private static List<Item> itemsToDisplay = new ArrayList<>();
	@Getter
	private static final Gui gui = PagedGui.items().setStructure(
									"# # # # # # # # #",
									"# x x x x x x x #",
									"# x x x x x x x #",
									"# x x x x x x x #",
									"# x x x x x x x #",
									"# # # < o > # # #")
					.addIngredient('#', MiscItems.border)
					.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
					.addIngredient('<', new BackItem())
					.addIngredient('o', new ListingsItem())
					.addIngredient('>', new ForwardItem())
					.build();
}
