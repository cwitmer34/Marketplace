package org.cwitmer34.marketplace.data.mongo.listings;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ListingsHandler {

	private final ListingsStorage listingsStorage;
	private final Map<String, PlayerListing> listings = new HashMap<>();

	public ListingsHandler() {
		this.listingsStorage = new ListingsMongoStorage();
	}

	public void createListing(String playerUuid, String playerName, String itemUuid, String serializedItem, String duration, int price) {
		PlayerListing listing = new PlayerListing(playerUuid, playerName, itemUuid, serializedItem, duration, price);
		listings.put(itemUuid, listing);
		listingsStorage.load(listing);
	}

	public void deleteListing(String itemUuid) {
		PlayerListing listing = listings.get(itemUuid);
		listingsStorage.delete(listing);
		listings.remove(itemUuid);
	}

	public PlayerListing getListing(String itemUuid) {
		return listings.get(itemUuid);
	}


}
