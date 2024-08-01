package org.cwitmer34.marketplace.data.mongo.collect;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CollectStorage {

	/**
	 * Load the transactions from storage
	 *
	 * @param transactions The transactions to load
	 */

	void load(final PlayerCollect transactions);

	/**
	 * Load all player collects from storage
	 *
	 * @return All collects
	 */

	CompletableFuture<List<PlayerCollect>> loadAll();

	/**
	 * Save the transactions to the players transaction history
	 *
	 * @param transactions The transactions to save
	 */

	void save(final PlayerCollect transactions);

	/**
	 * Delete transaction history from a player
	 *
	 * @param transactions The transactions to delete
	 */

	void delete(final PlayerCollect transactions);

	/**
	 * Add a transaction to the players transaction history
	 *
	 * @param transactions All transactions linked to a player
	 * @param transaction  The transaction to add
	 */

	void addTransaction(final PlayerCollect transactions, final String transaction);

	/**
	 * Remove a transaction from the players transaction history
	 *
	 * @param transactions All transactions linked to a player
	 * @param transaction  The transaction to remove
	 */

	void removeItem(PlayerCollect transactions, String transaction);
}
