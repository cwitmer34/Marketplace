package org.cwitmer34.marketplace.data.mongo.listings;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.data.mongo.collect.PlayerCollect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListingsMongoStorage implements ListingsStorage {
	@Override
	public void load(final PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document query = createDoc(listings.getItemUuid());
				final Document document = TrialMarketplace.getMongo().getListings().find(query).first();

				if (document == null) {
					save(listings);
					return;
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public CompletableFuture<List<PlayerListing>> loadAll() {
		final List<PlayerCollect> collects = new CopyOnWriteArrayList<>();
		final CompletableFuture<List<PlayerListing>> future = new CompletableFuture<>();

		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					final MongoCollection<Document> collection = TrialMarketplace.getMongo().getCollect();
					final MongoCollection<Document> listingsCollection = TrialMarketplace.getMongo().getListings();
					final List<PlayerListing> listings = new ArrayList<>();
					listingsCollection.find().forEach(document -> {
						final PlayerListing listing = new PlayerListing(
							document.getString("playerUuid"),
							document.getString("playerName"),
							document.getString("itemUuid"),
							document.getString("serializedItem"),
							document.getString("duration"),
							document.getInteger("price")
						);
						listings.add(listing);
					});
					future.complete(new ArrayList<>(listings));
				} catch (final Exception e) {
					future.completeExceptionally(e);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

		return future;
	}

	@Override
	public void save(final PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document doc = createDoc(listings.getItemUuid());
				final Document document = TrialMarketplace.getMongo().getListings().find(doc).first();

				if (document == null) {
					TrialMarketplace.getMongo().getListings().insertOne(listings.toBson(listings.getItemUuid()));
					return;
				}

				TrialMarketplace.getMongo().getListings().replaceOne(document, listings.toBson(listings.getItemUuid()));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void delete(final PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document query = createDoc(listings.getItemUuid());
				final Document document = TrialMarketplace.getMongo().getListings().find(query).first();

				if (document != null) {
					TrialMarketplace.getMongo().getListings().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	private Document createDoc(String itemUuid) {
		return new Document("itemUuid", itemUuid);
	}
}
