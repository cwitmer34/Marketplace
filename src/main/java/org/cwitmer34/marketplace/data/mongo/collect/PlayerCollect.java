package org.cwitmer34.marketplace.data.mongo.collect;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;
import redis.clients.jedis.Jedis;
import xyz.xenondevs.invui.item.Item;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class PlayerCollect {
	private String playerUuid;
	private String collectUuid;
	private String key;
	private List<String> serializedItems;

	public PlayerCollect(String playerUuid, String collectUuid, List<String> serializedItems) {
		this.playerUuid = playerUuid;
		this.collectUuid = collectUuid;
		this.key = "collect:" + playerUuid;
		this.serializedItems = serializedItems;
	}

	public void setSerializedItems(final List<String> serializedItems) {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			if (serializedItems.isEmpty()) return;
			jedis.del(key);
			jedis.rpush(key, serializedItems.toArray(new String[0]));
		}
	}

	public void addSerializedItem(final String item) {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			ConsoleUtil.warning("Adding item to player collect: " + key);
			jedis.rpush(key, item);
		}
	}

	public final void removeSerializedItem(final String item) {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
			String item1 = getSerializedItem(item);
			jedis.lrem(key, 1, item1);
			List<Item> items = GeneralUtil.deserializeItems(this.getSerializedItems());
			MarketplaceMain.getCollectGuis().get(playerUuid).setItems(items);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public final List<String> getSerializedItems() {
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
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
		try (final Jedis jedis = MarketplaceMain.getRedis().getPool()) {
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
