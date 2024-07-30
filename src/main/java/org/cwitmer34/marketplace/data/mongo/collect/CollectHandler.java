package org.cwitmer34.marketplace.data.mongo.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectHandler {

	private final CollectStorage collectStorage;
	private final Map<String, PlayerCollect> collects = new HashMap<>();

	public CollectHandler() {
		this.collectStorage = new CollectMongoStorage();
	}

	public void createCollect(String uuid, List<String> serializedItems) {
		PlayerCollect collect = new PlayerCollect(uuid, serializedItems);
		collects.put(uuid, collect);
		collectStorage.load(collect);
	}

	public void deleteCollect(String uuid) {
		collects.remove(uuid);
		PlayerCollect collect = collects.get(uuid);
		collectStorage.delete(collect);
	}

	public PlayerCollect getCollect(String uuid) {
		return collects.get(uuid);
	}

	public void addItemToCollect(String uuid, String serializedItem) {
		PlayerCollect collect = collects.get(uuid);
		collect.getItems().add(serializedItem);
		collectStorage.addItemToCollect(collect, serializedItem);
	}

	public void removeItemFromCollect(String uuid, String serializedItem) {
		PlayerCollect collect = collects.get(uuid);
		collect.getItems().remove(serializedItem);
		collectStorage.removeItemFromCollect(collect, serializedItem);
	}



}
