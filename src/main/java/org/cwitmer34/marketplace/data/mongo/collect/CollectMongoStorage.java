package org.cwitmer34.marketplace.data.mongo.collect;

import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;

public class CollectMongoStorage implements CollectStorage {
	@Override
	public void load(PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collect.getUuid())).first();
				if (document == null) {
					save(collect);
					return;
				}

				collect.setItems(document.getList("items", String.class));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void save(PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collect.getUuid())).first();
				if (document == null) {
					TrialMarketplace.getMongo().getCollect().insertOne(collect.toBson());
					return;
				}

				TrialMarketplace.getMongo().getCollect().replaceOne(document, collect.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}


	@Override
	public void addItemToCollect(PlayerCollect collect, String serializedItem) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collect.getUuid())).first();
				if (document == null) {
					collect.getItems().add(serializedItem);
					save(collect);
					return;
				}

				collect.getItems().add(serializedItem);
				TrialMarketplace.getMongo().getCollect().replaceOne(document, collect.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void removeItemFromCollect(PlayerCollect collect, String serializedItem) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collect.getUuid())).first();

				if (document == null) {
					save(collect);
					return;
				}

				collect.getItems().remove(serializedItem);
				TrialMarketplace.getMongo().getCollect().replaceOne(document, collect.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}

	@Override
	public void delete(PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("uuid", collect.getUuid())).first();
				if (document != null) {
					TrialMarketplace.getMongo().getCollect().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
