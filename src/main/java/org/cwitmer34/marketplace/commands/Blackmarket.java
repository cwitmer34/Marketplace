package org.cwitmer34.marketplace.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.BMConfig;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.guis.BlackmarketGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.window.Window;

public class Blackmarket implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		} else if (!(player.hasPermission("marketplace.blackmarket.view"))) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noBlackmarketPermission));
			return true;
		}
		if (args.length != 1) {
			Window.single().setGui(BlackmarketGUI.getGui()).open(player);
		} else if (args[0].equalsIgnoreCase("refresh") || args[0].equalsIgnoreCase("generate")) {
			if (player.hasPermission("marketplace.blackmarket.generate")) {

				Bukkit.getScheduler().cancelTask(BlackmarketGUI.getTaskID());
				BlackmarketGUI.refreshItems();

			} else {
				player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noRefreshPermission));
			}
		}
		return true;
	}
}
