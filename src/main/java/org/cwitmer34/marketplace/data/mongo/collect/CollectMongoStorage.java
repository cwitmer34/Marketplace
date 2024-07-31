package org.cwitmer34.marketplace.data.mongo.collect;

import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;

import java.util.List;

public class CollectMongoStorage implements CollectStorage {
	@Override
	public void load(PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("playerUuid", collect.getPlayerUuid())).first();
				if (document == null) {
					save(collect);
					return;
				}
				List<String> items = document.getList("items", String.class);
				collect.setSerializedItems(items);
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void save(PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("playerUuid", collect.getPlayerUuid())).first();
				if (document == null) {
					TrialMarketplace.getMongo().getCollect().insertOne(collect.toBson());
					return;
				}
				TrialMarketplace.getMongo().getCollect().replaceOne(document, collect.toBson(), new ReplaceOptions().upsert(true));
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}


	@Override
	public void addTransaction(PlayerCollect collect, String item) {
		new BukkitRunnable() {
			@Override
			public void run() {
				List<String> items = collect.getSerializedItems();
				ConsoleUtil.info("Adding collect to player: " + collect.getPlayerUuid());
				items.addFirst(item);
				collect.getSerializedItems().forEach(ConsoleUtil::info);
				collect.setSerializedItems(items);
				save(collect);
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}

	@Override
	public void removeItem(PlayerCollect collect, String item) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getTransactions().find(new Document("playerUuid", collect.getPlayerUuid())).first();
				collect.getSerializedItems().remove(item);
				if (document == null) {
					save(collect);
					return;
				}

				TrialMarketplace.getMongo().getTransactions().replaceOne(document, collect.toBson());
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());

	}

	@Override
	public void delete(PlayerCollect collect) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Document document = TrialMarketplace.getMongo().getCollect().find(new Document("playerUuid", collect.getPlayerUuid())).first();
				if (document != null) {
					TrialMarketplace.getMongo().getCollect().deleteOne(document);
				}
			}
		}.runTaskAsynchronously(TrialMarketplace.getPlugin());
	}
}
