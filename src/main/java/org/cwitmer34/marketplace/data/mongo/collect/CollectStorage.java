package org.cwitmer34.marketplace.data.mongo.collect;

import org.bson.Document;

public interface CollectStorage {

	/**
	 * Load the collect from the storage
	 *
	 * @param collect The collect to load
	 */

	void load(PlayerCollect collect);

	/**
	 * Save the collect to the storage
	 *
	 * @param collect The collect to save
	 */

	void save(PlayerCollect collect);

	/**
	 * Delete the collect from the storage
	 *
	 * @param collect The collect to delete
	 */

	void delete(PlayerCollect collect);

	/**
	 * Add an item to the collect
	 *
	 * @param collect The collect to add the item to
	 * @param serializedItem The serialized item to add
	 */

	void addItemToCollect(PlayerCollect collect, String serializedItem);

	/**
	 * Remove an item from the collect
	 *
	 * @param collect The collect to remove the item from
	 * @param serializedItem The serialized item to remove
	 */

	void removeItemFromCollect(PlayerCollect collect, String serializedItem);
}
