package org.cwitmer34.marketplace.data.mongo.listings;

import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;

public class ListingsMongoStorage implements ListingsStorage {
	@Override
	public void load(PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document query = createDoc(listings.getItemUuid());
				Document document = TrialMarketplace.getMongo().getListings().find(query).first();
				if (document == null) {
					save(listings);
					return;
				}
				listings.setPlayerUuid(document.getString("playerUuid"));
				listings.setPlayerName(document.getString("playerName"));
				listings.setSerializedItem(document.getString("itemUuid"));
				listings.setSerializedItem(document.getString("serializedItem"));
				listings.setDuration(document.getString("duration"));
				listings.setPrice(document.getInteger("price"));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void save(PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document doc = createDoc(listings.getItemUuid());
				Document document = TrialMarketplace.getMongo().getListings().find(doc).first();
				if (document == null) {
					TrialMarketplace.getMongo().getListings().insertOne(listings.toBson());
					return;
				}

				TrialMarketplace.getMongo().getListings().replaceOne(document, listings.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}

	private Document createDoc(String itemUuid) {
		return new Document("itemUuid", itemUuid);
	}

	@Override
	public void delete(PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document query = createDoc(listings.getItemUuid());
				Document document = TrialMarketplace.getMongo().getListings().find(query).first();
				if (document != null) {
					TrialMarketplace.getMongo().getListings().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
