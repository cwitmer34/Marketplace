package org.cwitmer34.marketplace.guis;

import lombok.Getter;
import lombok.Setter;
import org.cwitmer34.marketplace.items.guiItems.BackItem;
import org.cwitmer34.marketplace.items.guiItems.ForwardItem;
import org.cwitmer34.marketplace.items.guiItems.ListingsItem;
import org.cwitmer34.marketplace.items.guiItems.MiscItems;
import xyz.xenondevs.invui.gui.AbstractGui;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CollectGUI {
	private final Gui gui = PagedGui.items().setStructure(
									"< # # # # # # # #",
									"# . . . . . . . #",
									"# . x x x x x . #",
									"# . x x x x x . #",
									"# . . . . . . . #",
									"# # # # # # # # #")
					.addIngredient('#', MiscItems.border)
					.addIngredient('.', MiscItems.filler)
					.addIngredient('<', new BackItem())
					.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
					.build();

	public void setItems(List<Item> items) {
		((PagedGui) gui).setContent(items);
	}
}
