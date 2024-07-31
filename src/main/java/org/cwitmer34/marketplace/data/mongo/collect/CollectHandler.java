package org.cwitmer34.marketplace.data.mongo.collect;

import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CollectHandler {

	private final CollectStorage collectStorage;
	private final Map<String, PlayerCollect> collects = new HashMap<>();

	public CollectHandler() {
		this.collectStorage = new CollectMongoStorage();
	}

	public void createCollect(String playerUuid, String collectUuid, List<String> items) {
		PlayerCollect collect = new PlayerCollect(playerUuid, collectUuid, items);
		collects.put(playerUuid, collect);
		collectStorage.load(collect);
	}

	public void deleteCollect(String uuid) {
		PlayerCollect collect = collects.get(uuid);
		collectStorage.delete(collect);
		collects.remove(uuid);
	}

	public PlayerCollect getCollect(String uuid) {
		return collects.get(uuid);
	}

	public void addItem(String uuid, String serializedItem) {
		ConsoleUtil.info("Adding collect item to player: " + uuid);
		if (!collects.containsKey(uuid)) {
			createCollect(uuid, UUID.randomUUID().toString(), List.of(serializedItem));
			return;
		}
		PlayerCollect collect = collects.get(uuid);
		collect.getSerializedItems().add(serializedItem);
		collectStorage.save(collect);
	}

	public void removeItem(String uuid, String serializedItem) {
		PlayerCollect collect = collects.get(uuid);
		collect.getSerializedItems().remove(serializedItem);
		collectStorage.removeItem(collect, serializedItem);
	}

}
