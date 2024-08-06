package org.cwitmer34.marketplace.data.mongo.listings;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.util.ConsoleUtil;

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
				final Document document = MarketplaceMain.getMongo().getListings().find(query).first();

				if (document == null) {
					save(listings, true);
					return;
				}
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());
	}

	@Override
	public CompletableFuture<List<PlayerListing>> loadAll() {
		final List<PlayerListing> listings = new CopyOnWriteArrayList<>();
		final CompletableFuture<List<PlayerListing>> future = new CompletableFuture<>();

		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					final MongoCollection<Document> collection = MarketplaceMain.getMongo().getListings();

					if (collection.countDocuments() == 0) {
						future.complete(listings);
						return;
					}

					collection.find().forEach(document -> {
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
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());

		return future;
	}

	@Override
	public void save(final PlayerListing listings, final boolean async) {
		final Document query = createDoc(listings.getItemUuid());
		if (async) {
			new BukkitRunnable() {
				@Override
				public void run() {
					final Document document = MarketplaceMain.getMongo().getListings().find(query).first();

					if (document == null) {
						MarketplaceMain.getMongo().getListings().insertOne(listings.toBson());
						return;
					}

					MarketplaceMain.getMongo().getListings().replaceOne(document, listings.toBson());
				}
			}.runTaskAsynchronously(MarketplaceMain.getPlugin());
		} else {
			final Document document = MarketplaceMain.getMongo().getListings().find(query).first();
			ConsoleUtil.warning("document: " + document);
			if (document == null) {
				MarketplaceMain.getMongo().getListings().insertOne(listings.toBson());
				return;
			}

			MarketplaceMain.getMongo().getListings().replaceOne(document, listings.toBson());
		}
	}

	@Override
	public void delete(final PlayerListing listings) {
		new BukkitRunnable() {
			@Override
			public void run() {
				final Document query = createDoc(listings.getItemUuid());
				final Document document = MarketplaceMain.getMongo().getListings().find(query).first();

				if (document != null) {
					MarketplaceMain.getMongo().getListings().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(MarketplaceMain.getPlugin());
	}

	private Document createDoc(String itemUuid) {
		return new Document("itemUuid", itemUuid);
	}
}
