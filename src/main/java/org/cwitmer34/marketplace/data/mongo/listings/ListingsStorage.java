package org.cwitmer34.marketplace.data.mongo.listings;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ListingsStorage {

	/**
	 * Load the listings from the storage
	 *
	 * @param listings The listings to load
	 */

	void load(final PlayerListing listings);

	/**
	 * Load all player listings from storage
	 *
	 * @return All listings
	 */
	CompletableFuture<List<PlayerListing>> loadAll();

	/**
	 * Save the listings to the storage
	 *
	 * @param listings The listings to save
	 * @param async    If the save should be async
	 */

	void save(final PlayerListing listings, final boolean async);

	/**
	 * Delete the listings from the storage
	 *
	 * @param listings The listings to delete
	 */

	void delete(final PlayerListing listings);

}
