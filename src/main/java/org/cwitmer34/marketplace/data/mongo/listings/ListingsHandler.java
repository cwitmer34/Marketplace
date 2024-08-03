package org.cwitmer34.marketplace.data.mongo.listings;

import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.data.mongo.collect.PlayerCollect;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ListingsHandler {
	private final ListingsStorage listingsStorage;
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

	public void syncListings() {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			final Set<String> keys = jedis.keys("listing:*");
			TrialMarketplace.getMongo().getListings().drop();
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
				ConsoleUtil.info("PlayerListing: " + listing);
				this.listingsStorage.save(listing, false);
				ConsoleUtil.info("Synced listing to Mongo from Redis.");
			}
		} catch (Exception e) {
			ConsoleUtil.severe("Failed to sync listings to Mongo from Redis.");
			e.printStackTrace();
		}
	}

	public void syncFromMongo() {
		listingsStorage.loadAll().thenAccept(listings -> {
			for (final PlayerListing listing : listings) {
				ConsoleUtil.info(String.valueOf(listing));
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
				ConsoleUtil.info("Synced listing from Mongo to Redis.");
			}
		}).exceptionally(e -> {
			ConsoleUtil.severe("Failed to sync listings from Mongo to Redis.");
			e.printStackTrace();
			return null;
		});
	}
}
