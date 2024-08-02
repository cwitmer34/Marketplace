package org.cwitmer34.marketplace.data.mongo.collect;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CollectStorage {

	/**
	 * Load the collects from storage
	 *
	 * @param collects The collects to load
	 */

	void load(final PlayerCollect collects);

	/**
	 * Load all player collects from storage
	 *
	 * @return All collects
	 */

	CompletableFuture<List<PlayerCollect>> loadAll();

	/**
	 * Save the collects to the players collect history
	 *
	 * @param collects The collects to save
	 */

	void save(final PlayerCollect collects, final boolean async);

	/**
	 * Delete collect history from a player
	 *
	 * @param collects The collects to delete
	 */

	void delete(final PlayerCollect collects);

	/**
	 * Add a collect to the players collect history
	 *
	 * @param collects All collects linked to a player
	 * @param collect  The collect to add
	 */

	void addTransaction(final PlayerCollect collects, final String collect);

	/**
	 * Remove a collect from the players collect history
	 *
	 * @param collects All collects linked to a player
	 * @param collect  The collect to remove
	 */

	void removeItem(PlayerCollect collects, String collect);
}
