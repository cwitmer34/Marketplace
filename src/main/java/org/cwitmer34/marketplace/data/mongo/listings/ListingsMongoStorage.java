package org.cwitmer34.marketplace.data.mongo.listings;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;

import java.util.Map;

public class ListingsMongoStorage implements ListingsStorage {
	@Override
	public void load(PlayerListings listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getListings().find(new Document("uuid", listings.getUuid())).first();
				if (document == null) {
					save(listings);
					return;
				}
				BasicDBObject items = (BasicDBObject) document.get("items");
				items.keySet().forEach(key -> {
					BasicDBObject item = (BasicDBObject) items.get(key);
					listings.getItems().put(key, Map.of(item.firstEntry().getKey(), item.getDouble(item.firstEntry().getKey())));
				});
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void save(PlayerListings listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getListings().find(new Document("uuid", listings.getUuid())).first();
				if (document == null) {
					TrialMarketplace.getMongo().getListings().insertOne(listings.toBson());
					return;
				}

				TrialMarketplace.getMongo().getListings().replaceOne(document, listings.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}


	@Override
	public void addItemToListings(PlayerListings listings, String serializedItem, String duration, double price) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getListings().find(new Document("uuid", listings.getUuid())).first();
				if (document == null) {
					listings.getItems().put(serializedItem, Map.of(duration, price));
					save(listings);
					return;
				}

				listings.getItems().put(serializedItem, Map.of(duration, price));
				TrialMarketplace.getMongo().getListings().replaceOne(document, listings.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void removeItemFromListings(PlayerListings listings, String serializedItem) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getListings().find(new Document("uuid", listings.getUuid())).first();
				if (document == null) {
					listings.getItems().remove(serializedItem);
					save(listings);
					return;
				}

				listings.getItems().remove(serializedItem);
				TrialMarketplace.getMongo().getListings().replaceOne(document, listings.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}

	@Override
	public void delete(PlayerListings listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getListings().find(new Document("uuid", listings.getUuid())).first();
				if (document != null) {
					TrialMarketplace.getMongo().getListings().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
