package org.cwitmer34.marketplace.guis;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.BMConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.items.guiItems.*;
import org.cwitmer34.marketplace.util.GeneralUtil;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;

import java.io.IOException;
import java.util.*;


public class BlackmarketGUI {
	@Getter
	@Setter
	private static Map<String, Item> items = new HashMap<>();
	@Getter
	private static int taskID;

	public BlackmarketGUI(int amtOfItems) {
		Map<String, PlayerListing> listings = TrialMarketplace.getListingsHandler().getListings();
		Iterator<PlayerListing> listingIterator = listings.values().iterator();

		while (listingIterator.hasNext() && items.size() < amtOfItems) {
			PlayerListing listing = listingIterator.next();
		}
		setItems(items);
		updateItems();
	}

	@Getter
	public static final Gui gui = PagedGui.items().setStructure(
									"# # # # # # # # #",
									"# . . . . . . . #",
									"# . x x x x x . #",
									"# . x x x x x . #",
									"# . . . . . . . #",
									"# # # # # # # # #")
					.addIngredient('#', new BorderItem())
					.addIngredient('.', new BMBorderItem())
					.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
					.build();

	public static void removeItem(String itemUuid) throws IOException {
		items.remove(itemUuid);
		updateItems();
	}

	public static void refreshItems() {
		taskID = new BukkitRunnable() {
			@Override
			public void run() {

				Map<String, Item> marketplaceItems = TrialMarketplace.getMarketplaceGUI().getItems();
				Map<String, Item> blackmarketItems = new HashMap<>();
				List<String> keys = new ArrayList<>(marketplaceItems.keySet());
				Collections.shuffle(keys);

				for (int i = 0; i < BMConfig.amountOfItems && i < keys.size(); i++) {
					String key = keys.get(i);
					PlayerListing listing = TrialMarketplace.getListingsHandler().getListing(key);
					blackmarketItems.put(listing.getItemUuid(), new BMItem(listing.getSerializedItem(), listing.getPlayerName(), listing.getItemUuid(), listing.getPrice()));
				}
				setItems(blackmarketItems);
				updateItems();

				if (BMConfig.announce) {
					Bukkit.broadcastMessage(GeneralUtil.colorize(BMConfig.announceMessage));
				}
			}
		}.runTaskTimerAsynchronously(TrialMarketplace.getPlugin(), 0, BMConfig.refreshRate * 20 * 60).getTaskId();
	}

	public static void updateItems() {
		List<Item> itemsToDisplay = new ArrayList<>(items.values());
		((PagedGui) gui).setContent(itemsToDisplay);
		itemsToDisplay.forEach(Item::notifyWindows);
	}
}

