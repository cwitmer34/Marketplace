package org.cwitmer34.marketplace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.jetbrains.annotations.NotNull;

public class MPReload implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		TrialMarketplace.getPlugin().saveConfig();
		commandSender.sendMessage("Marketplace reloaded.");
		return true;
	}
}
