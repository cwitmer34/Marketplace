package org.cwitmer34.marketplace.guis;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.items.guiItems.*;
import org.cwitmer34.marketplace.util.GeneralUtil;
import xyz.xenondevs.invui.gui.AbstractGui;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.IOException;
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
					.addIngredient('#', new BorderItem())
					.addIngredient('.', new InnerBorderItem())
					.addIngredient('<', new BackToMarketItem())
					.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
					.build();

	public void setItems(List<Item> items) throws IOException {
		((PagedGui) gui).setContent(items);
		items.forEach(Item::notifyWindows);
	}
}
