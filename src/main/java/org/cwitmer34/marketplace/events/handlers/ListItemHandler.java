package org.cwitmer34.marketplace.events.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.events.customevents.ListItemEvent;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;

public class ListItemHandler implements Listener {


	public ListItemHandler() {
		TrialMarketplace.getPlugin().getServer().getPluginManager().registerEvents(this, TrialMarketplace.getPlugin());
	}

	@EventHandler
	public void onPurchaseItem(ListItemEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = event.getItemStack();
		PlayerListing listing = event.getListing();

		String message = GeneralUtil.parseListingPlaceholders(
						itemStack,
						listing,
						MessageConfig.listingMessage);
		player.sendMessage(message);

		if (MessageConfig.announceListings) {
			String broadcast =
							GeneralUtil.parseListingPlaceholders(
											itemStack,
											listing,
											MessageConfig.listingAnnounce);
			TrialMarketplace.getPlugin().getServer().broadcastMessage(broadcast);
		}
//		else if (MessageConfig.enableDiscordWebhook) {
//			// TODO
//		}

	}

}
