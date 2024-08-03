package org.cwitmer34.marketplace.commands;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.events.customevents.ListItemEvent;
import org.cwitmer34.marketplace.items.guiItems.ListedItem;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.cwitmer34.marketplace.config.Config;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;

import java.util.Objects;
import java.util.UUID;

public class Sell implements CommandExecutor {
	@Override
	@SneakyThrows
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		}
		String uuid = player.getUniqueId().toString();
		if (!(player.hasPermission("marketplace.sell"))) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noSellPermission));
			return true;
		} else if (args.length == 0 || tryParse(args[0]) == null) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.mustSpecifyPrice));
			return true;
		} else if (Objects.requireNonNull(tryParse(args[0])) < Config.minPrice) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.priceTooLow));
			return true;
		} else if (Objects.requireNonNull(tryParse(args[0])) > Config.maxPrice) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.priceTooHigh));
			return true;
		} else if (player.getInventory().getItemInMainHand().getType().isAir()) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.invalidItem));
			return true;
		}

		int sellPrice = Integer.parseInt(args[0]);
		ItemStack itemStack = player.getInventory().getItemInMainHand();
		String itemUuid = UUID.randomUUID().toString();
		Item item = new ListedItem(itemStack, player.getName(), itemUuid, sellPrice, Config.duration);

		TrialMarketplace.getMarketplaceGUI().addListing(itemUuid, item);

		TrialMarketplace.getListingsHandler().createListing(player.getUniqueId().toString(), player.getName(), itemUuid, GeneralUtil.itemStackToBase64(itemStack), Config.duration, sellPrice);
		PlayerListing listing = TrialMarketplace.getListingsHandler().getListing(itemUuid);
		ListItemEvent event = new ListItemEvent(player, itemStack, listing);
		Bukkit.getPluginManager().callEvent(event);

		return true;
	}

	public static Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
