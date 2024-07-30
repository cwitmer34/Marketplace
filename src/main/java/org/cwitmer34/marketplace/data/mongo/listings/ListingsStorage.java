package org.cwitmer34.marketplace.data.mongo.listings;

public interface ListingsStorage {

	/**
	 * Load the listings from the storage
	 *
	 * @param listings The listings to load
	 */

	void load(PlayerListings listings);

	/**
	 * Save the listings to the storage
	 *
	 * @param listings The listings to save
	 */

	void save(PlayerListings listings);

	/**
	 * Delete the listings from the storage
	 *
	 * @param listings The listings to delete
	 */

	void delete(PlayerListings listings);

	/**
	 * Add an item to the listings
	 *
	 * @param listings       The listings to add the item to
	 * @param serializedItem The serialized item to add
	 * @param price          The price of the item
	 * @param duration       The time left on the listing
	 */

	void addItemToListings(PlayerListings listings, String serializedItem, String duration, double price);

	/**
	 * Remove an item from the listings
	 *
	 * @param listings       The listings to remove the item from
	 * @param serializedItem The serialized item to remove
	 */

	void removeItemFromListings(PlayerListings listings, String serializedItem);
}
