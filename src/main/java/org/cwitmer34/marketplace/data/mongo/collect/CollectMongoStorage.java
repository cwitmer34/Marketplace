package org.cwitmer34.marketplace.data.mongo.collect;

import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;

public class CollectMongoStorage implements CollectStorage {
	@Override
	public void load(PlayerCollect collection) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collection.getUuid())).first();
				if (document == null) {
					save(collection);
					return;
				}

				collection.setItems(document.getList("items", String.class));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void save(PlayerCollect collection) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collection.getUuid())).first();
				if (document == null) {
					TrialMarketplace.getMongo().getCollect().insertOne(collection.toBson());
					return;
				}

				TrialMarketplace.getMongo().getCollect().replaceOne(document, collection.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}


	@Override
	public void addItemToCollect(PlayerCollect collection, String serializedItem) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collection.getUuid())).first();
				if (document == null) {
					collection.getItems().add(serializedItem);
					save(collection);
					return;
				}

				collection.getItems().add(serializedItem);
				TrialMarketplace.getMongo().getCollect().replaceOne(document, collection.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void removeItemFromCollect(PlayerCollect collection, String serializedItem) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collection.getUuid())).first();
				if (document == null) {
					collection.getItems().remove(serializedItem);
					save(collection);
					return;
				}

				collection.getItems().remove(serializedItem);
				TrialMarketplace.getMongo().getCollect().replaceOne(document, collection.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}

	@Override
	public void delete(PlayerCollect collection) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collection.getUuid())).first();
				if (document != null) {
					TrialMarketplace.getMongo().getCollect().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
