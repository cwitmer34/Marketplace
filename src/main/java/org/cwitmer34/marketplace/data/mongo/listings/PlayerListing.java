package org.cwitmer34.marketplace.data.mongo.listings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.json.JsonObject;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

@Getter
@Setter
@AllArgsConstructor
public class PlayerListing {
	private String playerUuid;
	private String playerName;
	private String itemUuid;
	private String serializedItem;
	private String duration;
	private int price;

	public final void setPlayerListing() {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			final String key = "listing:" + itemUuid;
			final JSONObject json = new JSONObject();
			json.put("playerUuid", playerUuid);
			json.put("playerName", playerName);
			json.put("itemUuid", itemUuid);
			json.put("serializedItem", serializedItem);
			json.put("duration", duration);
			json.put("price", price);
			jedis.set(key, json.toString());
		}
	}

	public final JSONObject getPlayerListing(final String itemUuid) {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			final String key = "listing:" + itemUuid;
			final String json = jedis.get(key);
			return new JSONObject(json);
		}
	}

	public final void removeItemFromListing(final String itemUuid) {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			final String key = "listing:" + itemUuid;
			jedis.del(key);
		}
	}

	public final Document toBson(final String itemUuid) {
		final JSONObject jsonObject = this.getPlayerListing(itemUuid);
		return Document.parse(jsonObject.toString());
	}
}
