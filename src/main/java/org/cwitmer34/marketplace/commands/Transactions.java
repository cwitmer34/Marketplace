package org.cwitmer34.marketplace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.Config;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Transactions implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		}
		String uuid = player.getUniqueId().toString();
		if (!(player.hasPermission("marketplace.history"))) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noTransactionPermission));
			return true;
		}
		final List<String> transactions = MarketplaceMain.getTransactionsHandler().getTransaction(uuid).getTransactions();
		if (transactions.isEmpty()) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.noTransactions));
			return true;
		}
		player.sendMessage(transactions.size() <= Config.maxTransactions
						? GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.lessThanMaxTransactions)
						: GeneralUtil.parseCommandPlaceholders(uuid, MessageConfig.moreThanMaxTransactions));

		for (int i = 0; i < transactions.size(); i++) {
			if (i >= Config.maxTransactions) break;
			player.sendMessage("  " + transactions.get(i));
		}
		return true;
	}
}
