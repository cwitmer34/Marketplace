package org.cwitmer34.marketplace.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.Config;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.items.guiItems.CollectItem;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtil {

	public static String legacyFromComponent(Component component) {
		String legacy = LegacyComponentSerializer.legacySection().serialize(component);
		if (!legacy.contains("&") && !legacy.contains("§")) {
			legacy = "&5&o" + legacy;
		}
		return legacy.replaceAll("&m", "").replaceAll("&", "§");
	}

	public static List<Item> deserializeItems(List<String> serializedItems) throws IOException {
		List<Item> items = new ArrayList<>();
		for (String serializedItem : serializedItems) {
			ItemStack itemStack = GeneralUtil.itemStackFromBase64(serializedItem);
			items.add(new CollectItem(new SimpleItem(itemStack), itemStack));
		}
		return items;
	}

	public static ItemStack itemStackFromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			return (ItemStack) dataInput.readObject();
		} catch (Exception e) {
			throw new IOException("unable to decode:", e);
		}
	}


	public static String parsePurchasePlaceholders(String name, ItemStack item, PlayerListing listing, String s) {
		return parseListingPlaceholders(item, listing, s.replaceAll("%buyer%", name));
	}

	public static String parseCommandPlaceholders(String uuid, String s) {
		return MessageConfig.prefix + s.replaceAll("%minPrice%", String.valueOf(Config.minPrice))
						.replaceAll("%maxPrice%", String.valueOf(Config.maxPrice))
						.replaceAll("%starting_duration%", Config.duration)
						.replaceAll("%maxTransactions%", String.valueOf(Config.maxTransactions))
						.replaceAll("%totalTransactions%", String.valueOf(MarketplaceMain.getTransactionsHandler().getTransaction(uuid).getTransactions().size()))
						.replaceAll("&", "§");
	}

	public static String parseListingPlaceholders(ItemStack item, PlayerListing listing, String s) {
		return MessageConfig.prefix + s.replaceAll(
										"%item%", item.getItemMeta().hasDisplayName()
														? item.getItemMeta().getDisplayName()
														: item.getType().name())
						.replaceAll("%price%", String.valueOf(listing.getPrice()))
						.replaceAll("%seller%", listing.getPlayerName())
						.replaceAll("%duration%", listing.getDuration())
						.replaceAll("&", "§");
	}

	public static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)d(\\d+)h(\\d+)m(\\d+)s");

	public static String decrementDuration(String duration) {
		Matcher matcher = DURATION_PATTERN.matcher(duration);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid duration format");
		}

		int days = Integer.parseInt(matcher.group(1));
		int hours = Integer.parseInt(matcher.group(2));
		int minutes = Integer.parseInt(matcher.group(3));
		int seconds = Integer.parseInt(matcher.group(4));

		int totalSeconds = days * 86400 + hours * 3600 + minutes * 60 + seconds;
		totalSeconds -= 1;

		if (totalSeconds < 0) {
			throw new IllegalStateException("Duration has already expired");
		}

		days = totalSeconds / 86400;
		totalSeconds %= 86400;
		hours = totalSeconds / 3600;
		totalSeconds %= 3600;
		minutes = totalSeconds / 60;
		seconds = totalSeconds % 60;

		return String.format("%dd%dh%dm%ds", days, hours, minutes, seconds);
	}


	public static String itemStackToBase64(ItemStack item) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeObject(item);
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("cannot save item stack:", e);
		}
	}

	public static double getPrice(Item item) {
		ItemStack itemStack = item.getItemProvider().get();
		itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("marketplace", "price"), PersistentDataType.DOUBLE);
		return 0;
	}

	public static String colorize(String s) {
		return s.replaceAll("&", "§");
	}
}
