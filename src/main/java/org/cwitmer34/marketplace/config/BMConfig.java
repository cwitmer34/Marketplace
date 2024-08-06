package org.cwitmer34.marketplace.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.cwitmer34.marketplace.MarketplaceMain;

public class BMConfig {
	private static final FileConfiguration config = MarketplaceMain.getPlugin().getConfig();
	public static final int duration = config.getInt("blackmarket.duration");
	public static boolean announce = config.getBoolean("blackmarket.announce");
	public static int amountOfItems = Math.abs(Math.min(config.getInt("blackmarket.amountOfItems"), 10));
	public static long refreshRate = config.getLong("blackmarket.refreshRate");
	public static String announceMessage = config.getString("blackmarket.announce-message");
	public static int discountAmt = config.getInt("blackmarket.discount-amount");
}
