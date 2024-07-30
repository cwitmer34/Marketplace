package org.cwitmer34.marketplace.data.mongo.transactions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsHandler {

	private final TransactionsStorage transactionsStorage;
	private final Map<String, PlayerTransactions> transactions = new HashMap<>();

	public TransactionsHandler() {
		this.transactionsStorage = new TransactionsMongoStorage();
	}

	public void createTransaction(String uuid, List<String> logs) {
		PlayerTransactions transaction = new PlayerTransactions(uuid, logs);
		transactions.put(uuid, transaction);
		transactionsStorage.load(transaction);
	}

	public void deleteTransaction(String uuid) {
		transactions.remove(uuid);
		PlayerTransactions transaction = transactions.get(uuid);
		transactionsStorage.delete(transaction);
	}

	public PlayerTransactions getTransaction(String uuid) {
		return transactions.get(uuid);
	}

	public void addTransaction(String uuid, String log) {
		PlayerTransactions transaction = transactions.get(uuid);
		transaction.getTransactions().addFirst(log);
		transactionsStorage.addTransaction(transaction, log);
	}

	public void removeTransaction(String uuid, String log) {
		PlayerTransactions transaction = transactions.get(uuid);
		transaction.getTransactions().remove(log);
		transactionsStorage.removeTransaction(transaction, log);
	}

}
