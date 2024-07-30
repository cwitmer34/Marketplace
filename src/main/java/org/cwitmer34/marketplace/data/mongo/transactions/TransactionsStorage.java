package org.cwitmer34.marketplace.data.mongo.transactions;

public interface TransactionsStorage {

	/**
	 * Load the transactions from storage
	 *
	 * @param transactions The transactions to load
	 */

	void load(PlayerTransactions transactions);

	/**
	 * Save the transactions to the players transaction history
	 *
	 * @param transactions The transactions to save
	 */

	void save(PlayerTransactions transactions);

	/**
	 * Delete transaction history from a player
	 *
	 * @param transactions The transactions to delete
	 */

	void delete(PlayerTransactions transactions);

	/**
	 * Add a transaction to the players transaction history
	 *
	 * @param transactions All transactions linked to a player
	 * @param transaction  The transaction to add
	 */

	void addTransaction(PlayerTransactions transactions, String transaction);

	/**
	 * Remove a transaction from the players transaction history
	 *
	 * @param transactions All transactions linked to a player
	 * @param transaction  The transaction to remove
	 */

	void removeTransaction(PlayerTransactions transactions, String transaction);
}
