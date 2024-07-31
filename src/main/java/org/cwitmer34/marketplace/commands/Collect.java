package org.cwitmer34.marketplace.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
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
		}
		PlayerCollect playerCollect = TrialMarketplace.getCollectHandler().getCollect(player.getUniqueId().toString());
		List<Item> items = deserializeItems(playerCollect.getSerializedItems());

		if (items.isEmpty()) {
			player.sendMessage(GeneralUtil.prefix.append(GeneralUtil.prefix.append(Component.text("You have no items to collect!")).color(NamedTextColor.LIGHT_PURPLE)));
			return true;
		}

		CollectGUI collectGUI = TrialMarketplace.getCollectGuis().get(player.getUniqueId().toString());
		collectGUI.setItems(items);
		Window.single().setGui(collectGUI.getGui()).open(player);

		return true;
	}

	public List<Item> deserializeItems(List<String> serializedItems) {
		List<Item> items = new ArrayList<>();
		for (String serializedItem : serializedItems) {
			try {
				ItemStack itemStack = GeneralUtil.itemStackFromBase64(serializedItem);
				items.add(new CollectItem(new SimpleItem(itemStack)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return items;
	}
}
