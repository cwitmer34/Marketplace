package org.cwitmer34.marketplace.data.mongo.listings;

import java.util.HashMap;
import java.util.Map;

public class ListingsHandler {

	private final ListingsStorage listingsStorage;
	private final Map<String, PlayerListings> listings = new HashMap<>();

	public ListingsHandler() {
		this.listingsStorage = new ListingsMongoStorage();
	}

	public void createListing(String uuid, Map<String, Map<String, Double>> itemPricePairs) {
		PlayerListings listing = new PlayerListings(uuid, itemPricePairs);
		listings.put(uuid, listing);
		listingsStorage.load(listing);
	}

	public void deleteListing(String uuid) {
		listings.remove(uuid);
		PlayerListings listing = listings.get(uuid);
		listingsStorage.delete(listing);
	}

	public PlayerListings getListing(String uuid) {
		return listings.get(uuid);
	}

	public void addListing(String uuid, String serializedItem, String duration, Double price) {
		PlayerListings listing = listings.getOrDefault(uuid, new PlayerListings(uuid, Map.of(serializedItem, Map.of(duration, price))));
		listing.getItems().put(serializedItem, Map.of(duration, price));
		listingsStorage.addItemToListings(listing, serializedItem, duration, price);
	}

	public void removeListing(String uuid, String serializedItem) {
		PlayerListings listing = listings.get(uuid);
		listing.getItems().remove(serializedItem);
		listingsStorage.removeItemFromListings(listing, serializedItem);
	}


}
