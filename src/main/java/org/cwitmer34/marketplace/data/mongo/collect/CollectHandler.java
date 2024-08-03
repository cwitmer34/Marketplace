package org.cwitmer34.marketplace.data.mongo.collect;

import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CollectHandler {

	private final CollectStorage collectStorage;
	private final Map<String, PlayerCollect> collects = new HashMap<>();

	public CollectHandler() {
		this.collectStorage = new CollectMongoStorage();
	}

	public void createCollect(final String playerUuid, final String collectUuid, final List<String> items) {
		final PlayerCollect collect = new PlayerCollect(playerUuid, collectUuid, items);
		collect.setSerializedItems(items);
		this.collects.put(playerUuid, collect);
	}

	public PlayerCollect getCollect(final String uuid) {
		return this.collects.get(uuid);
	}

	public PlayerCollect getOrCreateCollect(final String playerUuid) {
		PlayerCollect collect = this.collects.get(playerUuid);

		if (collect == null) {
			collect = new PlayerCollect(playerUuid, UUID.randomUUID().toString(), new ArrayList<>());
			this.collects.put(playerUuid, collect);
		}

		return collect;
	}

	public void addItem(final String playerUuid, final String serializedItem) {
		final PlayerCollect collect = getOrCreateCollect(playerUuid);

		if (collect != null) {
			ConsoleUtil.info("Adding collect item to player: " + collect.getPlayerUuid());
			collect.addSerializedItem(serializedItem);
		}
	}

	public void removeItem(final String playerUuid, final String serializedItem) {
		PlayerCollect collect = getOrCreateCollect(playerUuid);

		if (collect != null && collect.hasSerializedItem(serializedItem)) {
			try {
				ConsoleUtil.info("Removing collect item from player: " + playerUuid);
				collect.getSerializedItems().forEach(ConsoleUtil::warning);
				collect.removeSerializedItem(serializedItem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void syncCollects() {
		try (final Jedis jedis = TrialMarketplace.getRedis().getPool()) {
			final Set<String> keys = jedis.keys("collect:*");
			keys.forEach(ConsoleUtil::warning);
			TrialMarketplace.getMongo().getCollect().drop();
			if (keys.isEmpty()) {
				return;
			}

			final Pattern pattern = Pattern.compile("collect:(.*)");

			for (final String key : keys) {
				final Matcher matcher = pattern.matcher(key);
				if (matcher.matches()) {
					final String playerUuid = matcher.group(1);
					final PlayerCollect collect = this.collects.get(playerUuid);

					if (collect != null) {
						this.collectStorage.save(collect, false);
						ConsoleUtil.info("Synced collects to Mongo from Redis.");
					}
				}
			}
		} catch (final Exception e) {
			ConsoleUtil.severe("Failed to sync collects to Mongo from Redis.");
		}
	}

	public void syncFromMongo() {
		collectStorage.loadAll().thenAccept(collects -> {
			for (final PlayerCollect collect : collects) {
				PlayerCollect playerCollect = new PlayerCollect(collect.getPlayerUuid(), collect.getCollectUuid(), collect.getSerializedItems());
				playerCollect.setSerializedItems(collect.getSerializedItems());
				this.collects.put(collect.getPlayerUuid(), collect);
			}
		}).exceptionally(e -> {
			ConsoleUtil.severe("Failed to sync collects from Mongo to Redis.");
			return null;
		});
	}
}
