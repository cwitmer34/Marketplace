package org.cwitmer34.marketplace.data.mongo.listings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.util.ConsoleUtil;
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
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
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

	public final PlayerListing getPlayerListing() {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			final String key = "listing:" + itemUuid;
			final String json = jedis.get(key);
			final JSONObject item = new JSONObject(json);
			return new PlayerListing(
							item.getString("playerUuid"),
							item.getString("playerName"),
							item.getString("itemUuid"),
							item.getString("serializedItem"),
							item.getString("duration"),
							item.getInt("price")
			);
		}
	}

	public final JSONObject getPlayerListingToJson() {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			ConsoleUtil.warning("itemUuid: " + itemUuid);
			String key = "listing:" + itemUuid;
			String json = jedis.get(key);
			ConsoleUtil.warning("json: " + json);
			JSONObject jsonObject = new JSONObject(json);
			ConsoleUtil.warning("jsonObject: " + jsonObject);
			return jsonObject;
		}
	}

	public final void removeItemFromListing() {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			final String key = "listing:" + itemUuid;
			jedis.del(key);
		}
	}

	public final Document toBson() {
		final JSONObject jsonObject = this.getPlayerListingToJson();
		ConsoleUtil.warning("jsonObject " + jsonObject);
		return Document.parse(jsonObject.toString());
	}
}
