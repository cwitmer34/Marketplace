package org.cwitmer34.marketplace.data.mongo.transactions;

import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.ArrayList;
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
		PlayerTransactions transaction = transactions.get(uuid);
		transactionsStorage.delete(transaction);
		transactions.remove(uuid);
	}

	public PlayerTransactions getTransaction(String uuid) {
		PlayerTransactions playerTransactions = transactions.get(uuid);
		if (playerTransactions == null) {
			createTransaction(uuid, new ArrayList<>());
			playerTransactions = transactions.get(uuid);
		}
		transactions.replace(uuid, playerTransactions);
		return playerTransactions;
	}

	public void addTransaction(String uuid, String log) {
		PlayerTransactions transaction = transactions.get(uuid);
		if (transaction == null) {
			List<String> logs = new ArrayList<>();
			logs.add(log);
			createTransaction(uuid, logs);
			return;
		}
		ConsoleUtil.warning(transaction.getUuid());
		ConsoleUtil.warning("Adding transaction to player: " + uuid);
		ConsoleUtil.warning("Transaction: " + log);
		transaction.getTransactions().forEach(trans -> ConsoleUtil.warning(trans + "previous transaction"));
		transaction.getTransactions().add(log);
		transactionsStorage.save(transaction);
	}

	public void removeTransaction(String uuid, String log) {
		PlayerTransactions transaction = transactions.get(uuid);
		transaction.getTransactions().remove(log);
		transactionsStorage.removeTransaction(transaction, log);
	}

}
