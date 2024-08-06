package org.cwitmer34.marketplace.data.mongo.collect;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectHandler {

	private final CollectStorage collectStorage;
	private final Map<String, PlayerCollect> collects = new HashMap<>();

	public CollectHandler() {
		this.collectStorage = new CollectMongoStorage();
	}

	public void createCollect(final String playerUuid, final String collectUuid, final List<String> items) {
		final PlayerCollect collect = new PlayerCollect(playerUuid, collectUuid, items);
		collect.setSerializedItems(items);
		this.collects.put(playerUuid, collect);
	}

	public PlayerCollect getCollect(final String uuid) {
		return this.collects.get(uuid);
	}

	public PlayerCollect getOrCreateCollect(final String playerUuid) {
		PlayerCollect collect = this.collects.get(playerUuid);

		if (collect == null) {
			collect = new PlayerCollect(playerUuid, UUID.randomUUID().toString(), new ArrayList<>());
			this.collects.put(playerUuid, collect);
		}

		return collect;
	}

	public void addItem(final String playerUuid, final String serializedItem) {
		final PlayerCollect collect = getOrCreateCollect(playerUuid);

		if (collect != null) {
			ConsoleUtil.info("Adding collect item to player: " + collect.getPlayerUuid());
			collect.addSerializedItem(serializedItem);
		}
	}

	public void removeItem(final String playerUuid, final String serializedItem) {
		PlayerCollect collect = getOrCreateCollect(playerUuid);

		if (collect != null && collect.hasSerializedItem(serializedItem)) {
			try {
				ConsoleUtil.info("Removing collect item from player: " + playerUuid);
				collect.getSerializedItems().forEach(ConsoleUtil::warning);
				collect.removeSerializedItem(serializedItem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void syncCollects() {
		long startTime = System.currentTimeMillis();
		int totalCollectsSynced = 0;

		MongoCollection<Document> collection = TrialMarketplace.getMongo().getCollect();
		try (MongoCursor<Document> cursor = collection.find().iterator()) {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				String playerUuid = doc.getString("playerUuid");
				String collectUuid = doc.getString("collectUuid");
				List<String> serializedItems = doc.getList("items", String.class);

				createCollect(playerUuid, collectUuid, serializedItems);
				totalCollectsSynced++;
			}
		} catch (Exception e) {
			ConsoleUtil.severe("Failed to sync collects");
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 1000;
		ConsoleUtil.info("Total collects synced: " + totalCollectsSynced + ". Time taken: " + duration + "s.");
	}

	public void syncFromMongo() {
		long startTime = System.currentTimeMillis();
		AtomicInteger totalCollectsSynced = new AtomicInteger();

		collectStorage.loadAll().thenAccept(collects -> {
			for (final PlayerCollect collect : collects) {
				PlayerCollect playerCollect = new PlayerCollect(collect.getPlayerUuid(), collect.getCollectUuid(), collect.getSerializedItems());
				playerCollect.setSerializedItems(collect.getSerializedItems());
				this.collects.put(collect.getPlayerUuid(), collect);
				totalCollectsSynced.getAndIncrement();
			}
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime) / 1000;
			ConsoleUtil.info("Total collects synced: " + totalCollectsSynced + ". Time taken: " + duration + "s.");
		}).exceptionally(e -> {
			ConsoleUtil.severe("Failed to sync collects from Mongo to Redis.");
			return null;
		});
	}
}
