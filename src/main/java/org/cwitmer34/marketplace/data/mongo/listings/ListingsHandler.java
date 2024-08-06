package org.cwitmer34.marketplace.data.mongo.listings;

import lombok.Getter;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.guis.BlackmarketGUI;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ListingsHandler {
	private final ListingsStorage listingsStorage;
	@Getter
	private final Map<String, PlayerListing> listings = new HashMap<>();

	public ListingsHandler() {
		this.listingsStorage = new ListingsMongoStorage();
	}

	public void createListing(final String playerUuid, final String playerName, final String itemUuid, final String serializedItem, final String duration, final int price) {
		final PlayerListing listing = new PlayerListing(playerUuid, playerName, itemUuid, serializedItem, duration, price);
		listing.setPlayerListing();
		this.listings.put(itemUuid, listing);
	}

	public void deleteListing(final String itemUuid) {
		final PlayerListing listing = this.listings.get(itemUuid);
		listing.removeItemFromListing();
	}


	public PlayerListing getListing(final String itemUuid) {
		return this.listings.get(itemUuid).getPlayerListing();
	}

	public void syncFromMongo() {
		long startTime = System.currentTimeMillis();
		AtomicInteger totalListingsSynced = new AtomicInteger();

		listingsStorage.loadAll().thenAccept(listings -> {
			for (final PlayerListing listing : listings) {
				final PlayerListing playerListing = new PlayerListing(
								listing.getPlayerUuid(),
								listing.getPlayerName(),
								listing.getItemUuid(),
								listing.getSerializedItem(),
								listing.getDuration(),
								listing.getPrice()
				);
				playerListing.setPlayerListing();
				this.listings.put(listing.getItemUuid(), playerListing);
				totalListingsSynced.getAndIncrement();
			}
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime) / 1000;
			ConsoleUtil.info("Total listings synced: " + totalListingsSynced + ". Time taken: " + duration + "s.");
			BlackmarketGUI.updateItems();
		}).exceptionally(e -> {
			ConsoleUtil.severe("Failed to sync listings from Mongo to Redis.");
			e.printStackTrace();
			return null;
		});
	}


	public void syncListings() {
		long startTime = System.currentTimeMillis();
		int totalListingsSynced = 0;

		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			final Set<String> keys = jedis.keys("listing:*");
			MarketplaceMain.getMongo().getListings().drop();
			if (keys.isEmpty()) {
				return;
			}
			for (final String key : keys) {
				final String value = jedis.get(key);
				if (value == null) {
					return;
				}
				final JSONObject json = new JSONObject(value);
				final PlayerListing listing = new PlayerListing(
								json.getString("playerUuid"),
								json.getString("playerName"),
								json.getString("itemUuid"),
								json.getString("serializedItem"),
								json.getString("duration"),
								json.getInt("price")
				);
				this.listingsStorage.save(listing, false);
				totalListingsSynced++;
			}
		} catch (Exception e) {
			ConsoleUtil.severe("Failed to sync listings to Mongo from Redis.");
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime) / 1000;
		ConsoleUtil.info("Synced " + totalListingsSynced + " from Redis to Mongo in " + duration + "s");
	}

}


