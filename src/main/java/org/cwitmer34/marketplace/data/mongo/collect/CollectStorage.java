package org.cwitmer34.marketplace.data.mongo.collect;

public interface CollectStorage {

	/**
	 * Load the collection from the storage
	 *
	 * @param collection The collection to load
	 */

	void load(PlayerCollect collection);

	/**
	 * Save the collection to the storage
	 *
	 * @param collection The collection to save
	 */

	void save(PlayerCollect collection);

	/**
	 * Delete the collection from the storage
	 *
	 * @param collection The collection to delete
	 */

	void delete(PlayerCollect collection);

	/**
	 * Add an item to the collection
	 *
	 * @param collection The collection to add the item to
	 * @param serializedItem The serialized item to add
	 */

	void addItemToCollect(PlayerCollect collection, String serializedItem);

	/**
	 * Remove an item from the collection
	 *
	 * @param collection The collection to remove the item from
	 * @param serializedItem The serialized item to remove
	 */

	void removeItemFromCollect(PlayerCollect collection, String serializedItem);
}
