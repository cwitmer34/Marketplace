package org.cwitmer34.marketplace.data.mongo.collect;

public interface CollectStorage {

	/**
	 * Load the transactions from storage
	 *
	 * @param transactions The transactions to load
	 */

	void load(PlayerCollect transactions);

	/**
	 * Save the transactions to the players transaction history
	 *
	 * @param transactions The transactions to save
	 */

	void save(PlayerCollect transactions);

	/**
	 * Delete transaction history from a player
	 *
	 * @param transactions The transactions to delete
	 */

	void delete(PlayerCollect transactions);

	/**
	 * Add a transaction to the players transaction history
	 *
	 * @param transactions All transactions linked to a player
	 * @param transaction  The transaction to add
	 */

	void addTransaction(PlayerCollect transactions, String transaction);

	/**
	 * Remove a transaction from the players transaction history
	 *
	 * @param transactions All transactions linked to a player
	 * @param transaction  The transaction to remove
	 */

	void removeItem(PlayerCollect transactions, String transaction);
}
