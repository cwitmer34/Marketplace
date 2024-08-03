package org.cwitmer34.marketplace.guis;

import com.google.common.cache.AbstractCache;
import lombok.Getter;
import lombok.Setter;
import org.cwitmer34.marketplace.items.guiItems.*;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MarketplaceGUI {
	@Getter
	private Map<String, Item> items = new HashMap<>();

	@Getter
	private final Gui gui = PagedGui.items().setStructure(
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

	public void removeListing(String itemUuid) throws IOException {
		items.remove(itemUuid);
		updateListings();
	}

	public void addListing(String itemUuid, Item item) {
		items.put(itemUuid, item);
		updateListings();
	}

	public void updateListings() {
		List<Item> itemsToDisplay = new ArrayList<>(items.values());
		((PagedGui) gui).setContent(itemsToDisplay);
		itemsToDisplay.forEach(Item::notifyWindows);
	}
}
