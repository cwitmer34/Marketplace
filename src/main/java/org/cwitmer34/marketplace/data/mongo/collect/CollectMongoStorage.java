package org.cwitmer34.marketplace.data.mongo.collect;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectMongoStorage implements CollectStorage {
	@Override
	public void load(final PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document document = TrialMarketplace.getMongo().getCollect().find(new Document("playerUuid", collect.getPlayerUuid())).first();

				if (document == null) {
					save(collect);
					return;
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public CompletableFuture<List<PlayerCollect>> loadAll() {
		final List<PlayerCollect> collects = new CopyOnWriteArrayList<>();
		final CompletableFuture<List<PlayerCollect>> future = new CompletableFuture<>();

		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					final MongoCollection<Document> collection = TrialMarketplace.getMongo().getCollect();
					collection.find().forEach(document -> {
						final PlayerCollect collect = new PlayerCollect(
										document.getString("playerUuid"),
										document.getString("collectUuid"),
										document.getList("serializedItems", String.class)
						);
						collects.add(collect);
					});
					future.complete(new ArrayList<>(collects));
				} catch (final Exception e) {
					future.completeExceptionally(e);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

		return future;
	}

	@Override
	public void save(final PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document document = TrialMarketplace.getMongo().getCollect().find(new Document("playerUuid", collect.getPlayerUuid())).first();

				if (document == null) {
					TrialMarketplace.getMongo().getCollect().insertOne(collect.toBson());
					return;
				}

				TrialMarketplace.getMongo().getCollect().replaceOne(document, collect.toBson(), new ReplaceOptions().upsert(true));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void addTransaction(final PlayerCollect collect, final String item) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final List<String> items = collect.getSerializedItems();
				ConsoleUtil.info("Adding collect to player: " + collect.getPlayerUuid());
				items.addFirst(item);
				collect.getSerializedItems().forEach(ConsoleUtil::info);
				collect.setSerializedItems(items);
				save(collect);
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void removeItem(final PlayerCollect collect, final String item) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("playerUuid", collect.getPlayerUuid())).first();
				collect.getSerializedItems().remove(item);

				if (document == null) {
					save(collect);
					return;
				}

				TrialMarketplace.getMongo().getTransactions().replaceOne(document, collect.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void delete(final PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document document = TrialMarketplace.getMongo().getCollect().find(new Document("playerUuid", collect.getPlayerUuid())).first();

				if (document != null) {
					TrialMarketplace.getMongo().getCollect().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
