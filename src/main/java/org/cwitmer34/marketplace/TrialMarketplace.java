package org.cwitmer34.marketplace;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.cwitmer34.marketplace.commands.*;
import org.cwitmer34.marketplace.util.ConsoleUtil;

public final class TrialMarketplace extends JavaPlugin {

	@Getter
	public static Economy economy = null;

	@Getter
	public static TrialMarketplace plugin;

	public static FileConfiguration config;

	@Override
	public void onEnable() {

		plugin = this;
		saveDefaultConfig();
		config = getConfig();

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

	private boolean setupConfig() {
		return config != null;
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
