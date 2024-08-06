package org.cwitmer34.marketplace.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.cwitmer34.marketplace.TrialMarketplace;

public class BMConfig {
	private static FileConfiguration config = TrialMarketplace.getPlugin().getConfig();

	public static final int duration = config.getInt("blackmarket.duration");
	public static boolean announce = config.getBoolean("blackmarket.announce");
	public static int amountOfItems = config.getInt("blackmarket.amountOfItems");
	public static long refreshRate = config.getLong("blackmarket.refreshRate");
	public static String announceMessage = config.getString("blackmarket.announce-message");
	public static int discountAmt = config.getInt("blackmarket.discount-amount");
}
