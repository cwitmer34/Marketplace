package org.cwitmer34.marketplace.data.mongo.transactions;

import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.List;

public class TransactionsMongoStorage implements TransactionsStorage {
	@Override
	public void load(PlayerTransactions transactions) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = MarketplaceMain.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					save(transactions);
					return;
				}
				List<String> logs = document.getList("transactions", String.class);
				transactions.setTransactions(logs);
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());
	}

	@Override
	public void save(PlayerTransactions transactions) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = MarketplaceMain.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					MarketplaceMain.getMongo().getTransactions().insertOne(transactions.toBson());
					return;
				}
				MarketplaceMain.getMongo().getTransactions().replaceOne(document, transactions.toBson());
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());

	}


	@Override
	public void addTransaction(PlayerTransactions transactions, String transaction) {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<String> logs = transactions.getTransactions();
				ConsoleUtil.info("Adding transaction to player: " + transactions.getUuid());
				logs.addFirst(transaction);
				transactions.getTransactions().forEach(ConsoleUtil::info);
				transactions.setTransactions(logs);
				save(transactions);
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());
	}

	@Override
	public void removeTransaction(PlayerTransactions transactions, String transaction) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = MarketplaceMain.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					transactions.getTransactions().remove(transaction);
					save(transactions);
					return;
				}

				transactions.getTransactions().remove(transaction);
				MarketplaceMain.getMongo().getTransactions().replaceOne(document, transactions.toBson());
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());

	}

	@Override
	public void delete(PlayerTransactions transactions) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = MarketplaceMain.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document != null) {
					MarketplaceMain.getMongo().getTransactions().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());
	}
}
