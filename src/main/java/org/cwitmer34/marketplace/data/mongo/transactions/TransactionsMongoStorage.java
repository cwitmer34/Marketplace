package org.cwitmer34.marketplace.data.mongo.transactions;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;

import java.util.Map;

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
				transactions.getTransactions().addAll(document.getList("transactions", String.class));
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

				TrialMarketplace.getMongo().getTransactions().replaceOne(document, transactions.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}


	@Override
	public void addTransaction(PlayerTransactions transactions, String transaction) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("uuid", transactions.getUuid())).first();
				if (document == null) {
					transactions.getTransactions().add(transaction);
					save(transactions);
					return;
				}

				transactions.getTransactions().add(transaction);
				TrialMarketplace.getMongo().getTransactions().replaceOne(document, transactions.toBson());
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
