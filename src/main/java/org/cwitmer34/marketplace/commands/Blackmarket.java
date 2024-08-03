package org.cwitmer34.marketplace.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class Blackmarket implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		} else if (!(player.hasPermission("marketplace.blackmarket"))) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noBlackmarketPermission));
			return true;
		}

		return true;
	}
}
