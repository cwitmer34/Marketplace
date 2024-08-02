package org.cwitmer34.marketplace;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.cwitmer34.marketplace.commands.*;
import org.cwitmer34.marketplace.data.mongo.Mongo;
import org.cwitmer34.marketplace.data.mongo.collect.CollectHandler;
import org.cwitmer34.marketplace.data.mongo.listings.ListingsHandler;
import org.cwitmer34.marketplace.data.mongo.transactions.TransactionsHandler;
import org.cwitmer34.marketplace.discord.DiscordWebhook;
import org.cwitmer34.marketplace.guis.CollectGUI;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.events.handlers.JoinHandler;
import org.cwitmer34.marketplace.items.guiItems.ListedItem;
import org.cwitmer34.marketplace.data.redis.Redis;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrialMarketplace extends JavaPlugin {

	@Getter
	public static Economy economy = null;

	@Getter
	public static Mongo mongo;

	@Getter
	public static Redis redis;

	@Getter
	public static DiscordWebhook discordWebhook;

	@Getter
	public static Map<String, CollectGUI> collectGuis = new HashMap<>();

	@Getter
	public static CollectHandler collectHandler;

	@Getter
	public static TransactionsHandler transactionsHandler;

	@Getter
	public static ListingsHandler listingsHandler;

	@Getter
	public static TrialMarketplace plugin;

	public static FileConfiguration config;

	@Override
	public void onEnable() {

		plugin = this;
		saveDefaultConfig();
		config = getConfig();

		mongo = new Mongo();
		redis = new Redis();
		discordWebhook = new DiscordWebhook();
		listingsHandler = new ListingsHandler();
		collectHandler = new CollectHandler();
		transactionsHandler = new TransactionsHandler();

		initPlayerListings();
		initPlayerCollects();
		initPlayerTransactions();
		listingsHandler.syncFromMongo();
		collectHandler.syncFromMongo();

		if (!hasVaultDependency()) {
			ConsoleUtil.severe("Disabling plugin due to missing Vault dependency...");
			ConsoleUtil.severe("Follow this link to download Vault: https://www.spigotmc.org/resources/vault.34315/");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else if (!setupEconomy()) {
			ConsoleUtil.severe("Found Vault dependency, but failed to hook into economy provider.");
			ConsoleUtil.severe("Make sure you have an economy plugin installed that is supported by Vault. Disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else if (!setupPermissions()) {
			ConsoleUtil.warning("Permissions provider not found, inheriting default Vault permissions...");
		}

		new JoinHandler();

		getCommand("transactions").setExecutor(new Transactions());
		getCommand("marketplace").setExecutor(new Marketplace());
		getCommand("collect").setExecutor(new Collect());
		getCommand("blackmarket").setExecutor(new Blackmarket());
		getCommand("sell").setExecutor(new Sell());
		getCommand("test").setExecutor(new test());
		getCommand("mpreload").setExecutor(new MPReload());
		getCommand("flush").setExecutor(new Flush());
	}

	private void initPlayerListings() {
		new BukkitRunnable() {
			@Override
			public void run() {
				MongoCollection<Document> collection = mongo.getListings();
				try (MongoCursor<Document> cursor = collection.find().iterator()) {
					while (cursor.hasNext()) {
						Document doc = cursor.next();
						String playerUuid = doc.getString("playerUuid");
						String playerName = doc.getString("playerName");
						String itemUuid = doc.getString("itemUuid");
						String serializedItem = doc.getString("serializedItem");
						String duration = doc.getString("duration");
						int price = doc.getInteger("price");

						listingsHandler.createListing(playerUuid, playerName, itemUuid, serializedItem, duration, price);
						ItemStack itemStack = GeneralUtil.itemStackFromBase64(serializedItem);
						MarketplaceGUI.addItem(new ListedItem(itemStack, playerName, itemUuid, price, duration));

					}
				} catch (Exception e) {
					getLogger().severe("Failed to fetch listings from MongoDB: " + e.getMessage());
				}
			}
		}.runTaskAsynchronously(this);
	}

	private void initPlayerCollects() {
		new BukkitRunnable() {
			@Override
			public void run() {
				MongoCollection<Document> collection = mongo.getCollect();
				try (MongoCursor<Document> cursor = collection.find().iterator()) {
					while (cursor.hasNext()) {
						Document doc = cursor.next();
						String playerUuid = doc.getString("playerUuid");
						String collectUuid = doc.getString("collectUuid");
						List<String> serializedItems = doc.getList("items", String.class);
						collectHandler.createCollect(playerUuid, collectUuid, serializedItems);
					}
				} catch (Exception e) {
					getLogger().severe("Failed to fetch playerCollect from MongoDB: " + e.getMessage());
				}
			}
		}.runTaskAsynchronously(this);
	}

	private void initPlayerTransactions() {
		new BukkitRunnable() {
			@Override
			public void run() {
				MongoCollection<Document> collection = mongo.getTransactions();
				try (MongoCursor<Document> cursor = collection.find().iterator()) {
					while (cursor.hasNext()) {
						Document doc = cursor.next();
						String uuid = doc.getString("uuid");
						List<String> transactions = doc.getList("transactions", String.class);
						transactionsHandler.createTransaction(uuid, transactions);
					}
				} catch (Exception e) {
					ConsoleUtil.severe("Failed to fetch transactions from MongoDB: " + e.getMessage());
				}
			}
		}.runTaskAsynchronously(this);
	}

	private boolean hasVaultDependency() {
		return getServer().getPluginManager().getPlugin("Vault") != null;
	}

	private boolean setupEconomy() {

		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			ConsoleUtil.info("Hooked into VaultAPI - economy provider: " + economyProvider.getPlugin().getName());
		}

		return economy != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);

		if (permissionProvider.getPlugin().getName().equalsIgnoreCase("vault")) return false;

		ConsoleUtil.info("Hooked into VaultAPI - permission provider: " + permissionProvider.getPlugin().getName());
		return true;
	}

	@Override
	public void onDisable() {
		getListingsHandler().syncListings();
		getCollectHandler().syncCollects();
	}

}
