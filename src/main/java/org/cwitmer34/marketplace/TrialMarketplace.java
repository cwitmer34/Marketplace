package org.cwitmer34.marketplace;

import com.mongodb.BasicDBObject;
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
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.items.guiItems.ListedItem;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;
import xyz.xenondevs.invui.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrialMarketplace extends JavaPlugin {

	@Getter
	public static Economy economy = null;

	@Getter
	public static Mongo mongo;

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
		listingsHandler = new ListingsHandler();
		collectHandler = new CollectHandler();
		transactionsHandler = new TransactionsHandler();

		initPlayerListings();
		initPlayerCollects();
		initPlayerTransactions();


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

		getCommand("transactions").setExecutor(new Transactions());
		getCommand("marketplace").setExecutor(new Marketplace());
		getCommand("blackmarket").setExecutor(new Blackmarket());
		getCommand("sell").setExecutor(new Sell());
		getCommand("test").setExecutor(new test());
		getCommand("mpreload").setExecutor(new MPReload());
	}

	private void initPlayerListings() {
		new BukkitRunnable() {
			@Override
			public void run() {
				MongoCollection<Document> collection = mongo.getListings();
				try (MongoCursor<Document> cursor = collection.find().iterator()) {
					while (cursor.hasNext()) {
						Document doc = cursor.next();
						String uuid = doc.getString("uuid");
						Map<String, Map<String, Double>> items = new HashMap<>();
						BasicDBObject dbObject = (BasicDBObject) doc.get("items");
						for (String key : dbObject.keySet()) {
							BasicDBObject item = (BasicDBObject) dbObject.get(key);
							items.put(key, Map.of(item.firstEntry().getKey(), item.getDouble(item.firstEntry().getKey())));
							ItemStack itemStack = GeneralUtil.itemStackFromBase64(key);
							MarketplaceGUI.getItemsToDisplay().add(new ListedItem(itemStack, item.getInt(item.firstEntry().getKey()), key));
						}
						listingsHandler.createListing(uuid, items);
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
						String uuid = doc.getString("uuid");
						List<String> serializedItems = doc.getList("items", String.class);
						collectHandler.createCollect(uuid, serializedItems);
					}
				} catch (Exception e) {
					getLogger().severe("Failed to fetch playerCollects from MongoDB: " + e.getMessage());
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
						//transactionsHandler.createTransactions(uuid, transactions);
					}
				} catch (Exception e) {
					getLogger().severe("Failed to fetch transactions from MongoDB: " + e.getMessage());
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
		// Plugin shutdown logic
	}
}
