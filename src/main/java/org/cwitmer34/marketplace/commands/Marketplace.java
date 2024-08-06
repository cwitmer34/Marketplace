package org.cwitmer34.marketplace.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.window.Window;

public class Marketplace implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command.");
			return true;
		} else if (!(player.hasPermission("marketplace.view"))) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noViewPermission));
			return true;
		}

		Window.single()
						.setTitle("Marketplace")
						.setGui(MarketplaceMain.getMarketplaceGUI().getGui())
						.open(player);

		return true;
	}
}
