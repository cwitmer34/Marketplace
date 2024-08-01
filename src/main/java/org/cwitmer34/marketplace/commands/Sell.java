package org.cwitmer34.marketplace.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.events.customevents.ListItemEvent;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.items.guiItems.ListedItem;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.cwitmer34.marketplace.config.Config;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;

import java.util.Objects;
import java.util.UUID;

public class Sell implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		} else if (!(player.hasPermission("marketplace.sell"))) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You do not have permission to use /sell!").color(NamedTextColor.RED)));
			return true;
		} else if (args.length == 0 || tryParse(args[0]) == null) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You must provide the amount you want to sell, i.e").color(NamedTextColor.RED))
							.append(Component.text(" /sell 5000").color(NamedTextColor.YELLOW)));
			return true;
		} else if (Objects.requireNonNull(tryParse(args[0])) < Config.minPrice) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("The minimum sell price is ")).append(Component.text((int) Config.minPrice).color(NamedTextColor.YELLOW)));
			return true;
		} else if (Objects.requireNonNull(tryParse(args[0])) > Config.maxPrice) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("The maximum sell price is ")).append(Component.text((int) Config.maxPrice).color(NamedTextColor.YELLOW)));
			return true;
		} else if (player.getInventory().getItemInMainHand().getType().isAir()) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You must be holding the item you want to sell").color(NamedTextColor.RED)));
			return true;
		}

		int sellPrice = Integer.parseInt(args[0]);
		ItemStack itemStack = player.getInventory().getItemInMainHand();
		String itemUuid = UUID.randomUUID().toString();
		Item item = new ListedItem(itemStack, player.getName(), itemUuid, sellPrice, Config.duration);

		MarketplaceGUI.addItem(item);
		TrialMarketplace.getListingsHandler().createListing(player.getUniqueId().toString(), player.getName(), itemUuid, GeneralUtil.itemStackToBase64(itemStack), Config.duration, sellPrice);
		ListItemEvent event = new ListItemEvent(player, itemStack, sellPrice);
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
