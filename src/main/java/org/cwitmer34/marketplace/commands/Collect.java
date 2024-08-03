package org.cwitmer34.marketplace.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.collect.CollectHandler;
import org.cwitmer34.marketplace.data.mongo.collect.PlayerCollect;
import org.cwitmer34.marketplace.guis.CollectGUI;
import org.cwitmer34.marketplace.items.guiItems.CollectItem;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Collect implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("Only players can use this command!");
			return true;
		} else if (!(player.hasPermission("marketplace.collect"))) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noCollectPermission));
			return true;
		}

		PlayerCollect playerCollect = TrialMarketplace.getCollectHandler().getCollect(player.getUniqueId().toString());
		List<Item> items;
		try {
			items = GeneralUtil.deserializeItems(playerCollect.getSerializedItems());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		if (items.isEmpty()) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.noItemsInCollect));
			return true;
		}

		CollectGUI collectGUI = TrialMarketplace.getCollectGuis().get(player.getUniqueId().toString());
		try {
			collectGUI.setItems(items);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Window.single().setGui(collectGUI.getGui()).open(player);

		return true;
	}


}
