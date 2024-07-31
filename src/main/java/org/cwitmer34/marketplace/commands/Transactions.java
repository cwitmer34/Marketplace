package org.cwitmer34.marketplace.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Transactions implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		} else if (!(player.hasPermission("marketplace.history"))) {
			player.sendMessage(Component.text("You do not have permission to view your previous transactions!").color(NamedTextColor.RED));
			return true;
		}
		List<String> transactions = TrialMarketplace.getTransactionsHandler().getTransaction(player.getUniqueId().toString()).getTransactions();
		if (transactions.isEmpty()) {
			player.sendMessage(GeneralUtil.prefix.append(Component.text("You have no previous transactions!").color(NamedTextColor.LIGHT_PURPLE)));
			return true;
		}
		player.sendMessage(transactions.size() <= 10 ? "You have " + transactions.size() + " previous transactions:" : "You have more than 10 previous transactions. Here are the last 10:");
		transactions.forEach((transaction) -> player.sendMessage("     " + transaction));
		return true;
	}
}
