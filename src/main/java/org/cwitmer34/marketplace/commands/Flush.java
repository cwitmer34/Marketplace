package org.cwitmer34.marketplace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.jetbrains.annotations.NotNull;

public class Flush implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		TrialMarketplace.getRedis().flush();
		return true;
	}
}
