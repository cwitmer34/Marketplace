package org.cwitmer34.marketplace.config;

import lombok.Data;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.cwitmer34.marketplace.TrialMarketplace;

import java.util.ArrayList;
import java.util.List;

@Data
public class Config {
	private static FileConfiguration config = TrialMarketplace.getPlugin().getConfig();

	@Getter
	private static ButtonsConfig buttonsConfig = new ButtonsConfig();

	public static final String discordWebhook = config.getString("discord-webhook");

	// General settings
	public static final boolean addToInvIfOnline = config.getBoolean("general-settings.addToInvIfOnline");
	public static final int maxListings = config.getInt("general-settings.maxListings");
	public static int maxTransactions = config.getInt("general-settings.maxTransactions"); ;
	public static final double minPrice = config.getInt("general-settings.minPrice");
	public static final double maxPrice = config.getInt("general-settings.maxPrice");
	public static final int cooldown = config.getInt("general-settings.cooldown");
	public static final String duration = config.getString("general-settings.duration");
	public static final int tax = config.getInt("general-settings.tax");
	public static final boolean priceAsInteger = config.getBoolean("general-settings.priceAsInteger");

	@Getter
	private static final List<Object> settings = new ArrayList<>(List.of(addToInvIfOnline, maxListings, minPrice, maxPrice, cooldown, duration, tax, priceAsInteger));
}
