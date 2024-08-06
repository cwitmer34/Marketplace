package org.cwitmer34.marketplace.config;

import lombok.Data;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.cwitmer34.marketplace.TrialMarketplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Config {
	private static FileConfiguration config = TrialMarketplace.getPlugin().getConfig();

	public static final String discordWebhook = config.getString("discord-webhook");

	// General settings
	public static final boolean addToInvIfOnline = config.getBoolean("general-settings.addToInvIfOnline");
	public static int maxTransactions = config.getInt("general-settings.maxTransactions");
	public static final double minPrice = Math.max(config.getInt("general-settings.minPrice"), 1);
	public static final double maxPrice = Math.min(config.getInt("general-settings.maxPrice"), Integer.MAX_VALUE);
	public static final String duration = Objects.requireNonNullElse(config.getString("general-settings.duration"), "7d0h0m0s") ;

	@Getter
	private static final List<Object> settings = List.of(addToInvIfOnline, minPrice, maxPrice, duration);
}
