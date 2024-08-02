package org.cwitmer34.marketplace.data.mongo.collect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.cwitmer34.marketplace.TrialMarketplace;
import redis.clients.jedis.Jedis;

import java.util.List;

@Getter
@Setter
public class PlayerCollect {
	private String playerUuid;
	private String collectUuid;
	private final List<String> serializedItems;

	public PlayerCollect(final String playerUuid, final String collectUuid, final List<String> serializedItems) {
		this.playerUuid = playerUuid;
		this.collectUuid = collectUuid;
		this.serializedItems = serializedItems;
	}

	private final String key = "collect:" + playerUuid;

	public void setSerializedItems(final List<String> serializedItems) {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			if (serializedItems.isEmpty()) return;
			jedis.del(key);
			jedis.rpush(key, serializedItems.toArray(new String[0]));
		}
	}

	public void addSerializedItem(final String item) {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			jedis.rpush(key, item);
		}
	}

	public final void removeSerializedItem(final String item) {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			jedis.lrem(key, 0, item);
		}
	}

	public final List<String> getSerializedItems() {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			return jedis.lrange(key, 0, -1);
		}
	}

	public String getSerializedItem(final String item) {
		List<String> items = this.getSerializedItems();
		for (String collectedItem : items) {
			if (collectedItem.equals(item)) {
				return collectedItem;
			}
		}
		return null;
	}

	public void removeAllSerializedItems() {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			jedis.del(key);
		}
	}

	public boolean hasSerializedItem(final String item) {
		final List<String> items = getSerializedItems();
		return items.contains(item);
	}

	public final Document toBson() {
		return new Document("playerUuid", playerUuid)
						.append("collectUuid", collectUuid)
						.append("items", this.getSerializedItems());
	}
}
