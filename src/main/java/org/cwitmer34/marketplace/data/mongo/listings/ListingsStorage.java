package org.cwitmer34.marketplace.data.mongo.listings;

public interface ListingsStorage {

	/**
	 * Load the listings from the storage
	 *
	 * @param listings The listings to load
	 */

	void load(PlayerListing listings);

	/**
	 * Save the listings to the storage
	 *
	 * @param listings The listings to save
	 */

	void save(PlayerListing listings);

	/**
	 * Delete the listings from the storage
	 *
	 * @param listings The listings to delete
	 */

	void delete(PlayerListing listings);

}
