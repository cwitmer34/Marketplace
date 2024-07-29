package org.cwitmer34.marketplace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.cwitmer34.marketplace.util.SettingsUtil;
import org.jetbrains.annotations.NotNull;

public class test implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		SettingsUtil.getSettings().forEach(setting -> sender.sendMessage(setting.toString()));
		return true;
	}
}
