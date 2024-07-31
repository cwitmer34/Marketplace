package org.cwitmer34.marketplace.data.mongo.transactions;

import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.List;

public class TransactionsMongoStorage implements TransactionsStorage {
	@Override
	public void load(PlayerTransactions transactions) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					save(transactions);
					return;
				}
				List<String> logs = document.getList("transactions", String.class);
				transactions.setTransactions(logs);
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void save(PlayerTransactions transactions) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					TrialMarketplace.getMongo().getTransactions().insertOne(transactions.toBson());
					return;
				}
				TrialMarketplace.getMongo().getTransactions().replaceOne(document, transactions.toBson(), new ReplaceOptions().upsert(true));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

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
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void removeTransaction(PlayerTransactions transactions, String transaction) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					transactions.getTransactions().remove(transaction);
					save(transactions);
					return;
				}

				transactions.getTransactions().remove(transaction);
				TrialMarketplace.getMongo().getTransactions().replaceOne(document, transactions.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}

	@Override
	public void delete(PlayerTransactions transactions) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document != null) {
					TrialMarketplace.getMongo().getTransactions().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
