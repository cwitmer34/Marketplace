package org.cwitmer34.marketplace.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.util.GeneralUtil;

import java.util.Objects;

public class MessageConfig {
	private static FileConfiguration config = MarketplaceMain.config;
	public static String prefix = GeneralUtil.colorize(Objects.requireNonNullElse(config.getString("messages.prefix"), "&7[&6Marketplace&7] "));
	public static boolean announcePurchases = config.getBoolean("messages.purchases.announce");
	public static String purchaseAnnounce = config.getString("messages.purchases.announce-message");
	public static String purchaseMessage = config.getString("messages.purchases.message");
	public static boolean announceListings = config.getBoolean("messages.listings.announce");
	public static String listingAnnounce = config.getString("messages.listings.announce-message");
	public static String listingMessage = config.getString("messages.listings.message");
	public static String noSellPermission = config.getString("command-messages.noSellPermission");
	public static String noCollectPermission = config.getString("command-messages.noCollectPermission");
	public static String noViewPermission = config.getString("command-messages.noViewPermission");
	public static String noBlackmarketPermission = config.getString("command-messages.noBlackmarketPermission");
	public static String noTransactionPermission = config.getString("command-messages.noTransactionPermission");
	public static String noItemsInCollect = config.getString("command-messages.noCollectItems");
	public static String hasCollectItems = config.getString("command-messages.hasCollectItems");
	public static String mustSpecifyPrice = config.getString("command-messages.mustSpecifyPrice");
	public static String priceTooLow = config.getString("command-messages.priceTooLow");
	public static String priceTooHigh = config.getString("command-messages.priceTooHigh");
	public static String invalidItem = config.getString("command-messages.invalidItem");
	public static String noTransactions = config.getString("command-messages.noTransactions");
	public static String moreThanMaxTransactions = config.getString("command-messages.moreThanMaxTransactions");
	public static String lessThanMaxTransactions = config.getString("command-messages.lessThanMaxTransactions");
	public static String balanceTooLow = config.getString("command-messages.balanceTooLow");
	public static String noLongerAvailable = config.getString("command-messages.alreadyPurchased");
	public static String purchaseCancelled = config.getString("command-messages.purchaseCancelled");
	public static String itemSold = config.getString("command-messages.itemSold");
	public static String itemPurchased = config.getString("command-messages.itemPurchased");
	public static String noRefreshPermission = config.getString("blackmarket.noRefreshPermission");
	public static boolean enableDiscordWebhook = config.getBoolean("discord.enableMessages");
	public static String discordWebhook = config.getString("discord.webhook");
}
