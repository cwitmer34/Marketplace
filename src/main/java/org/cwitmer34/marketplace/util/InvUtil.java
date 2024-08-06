package org.cwitmer34.marketplace.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.MessageConfig;

public class InvUtil {
	public static void giveUnlessFullInv(final Player player, final ItemStack itemStack) {
		if (player.getInventory().firstEmpty() == -1) {
			player.sendMessage(Component.text(MessageConfig.prefix + "Your inventory is full! Item has been added to your collect.").color(NamedTextColor.RED));
			addToCollect(player.getUniqueId().toString(), itemStack);
		} else {
			player.getInventory().addItem(itemStack);
		}
	}

	public static void addToCollect(final String playerUuid, final ItemStack originalItem) {
		final String serializedItem = GeneralUtil.itemStackToBase64(originalItem);
		MarketplaceMain.getCollectHandler().addItem(playerUuid, serializedItem);
	}
}
