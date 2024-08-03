package org.cwitmer34.marketplace.events.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;
import org.cwitmer34.marketplace.util.GeneralUtil;

public class PurchaseItemHandler implements Listener {


	public PurchaseItemHandler() {
		TrialMarketplace.getPlugin().getServer().getPluginManager().registerEvents(this, TrialMarketplace.getPlugin());
	}

	@EventHandler
	public void onPurchaseItem(PurchaseItemEvent event) {
		Player buyer = event.getBuyer();
		ItemStack item = event.getItem();
		PlayerListing listing = event.getListing();

		if (MessageConfig.announcePurchases) {
			String broadcast = GeneralUtil.parsePurchasePlaceholders(buyer.getName(), item, listing, MessageConfig.purchaseAnnounce);
			TrialMarketplace.getPlugin().getServer().broadcastMessage(broadcast);
		}

		String message = GeneralUtil.parsePurchasePlaceholders(buyer.getName(), item, listing, MessageConfig.purchaseMessage);
		buyer.sendMessage(message);

	}

}
