package org.cwitmer34.marketplace.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.cwitmer34.marketplace.TrialMarketplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class SettingsUtil {
	private static FileConfiguration config = TrialMarketplace.getPlugin().getConfig();

	// General settings
	public static int updateInterval = config.getInt("general-settings.updateInterval");
	public static int maxListings = config.getInt("general-settings.maxListings");
	public static double minPrice = config.getInt("general-settings.minPrice");
	public static double maxPrice = config.getInt("general-settings.maxPrice");
	public static int cooldown = config.getInt("general-settings.cooldown");
	public static String duration = config.getString("general-settings.duration");
	public static int tax = config.getInt("general-settings.tax"); // in percent, i.e. 20 = 20%
	public static boolean priceAsInteger = config.getBoolean("general-settings.priceAsInteger");

	@Getter
	private static List<Object> settings = new ArrayList<>(List.of(updateInterval, maxListings, minPrice, maxPrice, cooldown, duration, tax, priceAsInteger));
}
