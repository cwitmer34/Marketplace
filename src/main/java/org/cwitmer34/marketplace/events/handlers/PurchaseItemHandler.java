package org.cwitmer34.marketplace.events.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.discord.DiscordWebhook;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;

import java.awt.*;

public class PurchaseItemHandler implements Listener {


	public PurchaseItemHandler() {
		MarketplaceMain.getPlugin().getServer().getPluginManager().registerEvents(this, MarketplaceMain.getPlugin());
	}

	@EventHandler
	public void onPurchaseItem(PurchaseItemEvent event) {
		Player buyer = event.getBuyer();
		ItemStack item = event.getItem();
		PlayerListing listing = event.getListing();

		if (MessageConfig.announcePurchases) {
			String broadcast = GeneralUtil.parsePurchasePlaceholders(buyer.getName(), item, listing, MessageConfig.purchaseAnnounce);
			MarketplaceMain.getPlugin().getServer().broadcastMessage(broadcast);
		}

		String message = GeneralUtil.parsePurchasePlaceholders(buyer.getName(), item, listing, MessageConfig.purchaseMessage);
		buyer.sendMessage(message);

		if (MessageConfig.enableDiscordWebhook) {
			MarketplaceMain.getDiscordWebhook()
							.addEmbed(new DiscordWebhook.EmbedObject()
											.setTitle("New Purchase")
											.setDescription(buyer.getName() + " just purchased an item! This listing had " + listing.getDuration() + " remaining.")
											.addField("Seller", listing.getPlayerName(), true)
											.addField("Buyer", buyer.getName(), true)
											.addField("Item", item.getItemMeta().hasDisplayName() ? item.getItemMeta().displayName().examinableName() : item.getI18NDisplayName(), true)
											.addField("Price", String.valueOf(listing.getPrice()), true)
											.addField("Amount", String.valueOf(item.getAmount()), true)
											.setColor(Color.GREEN)
											.setThumbnail("https://cravatar.eu/helmhead/" + buyer.getName() + "/600.png")
											.setFooter(listing.getItemUuid(), null)
							);

			try {
				MarketplaceMain.getDiscordWebhook().execute();
			} catch (Exception e) {
				ConsoleUtil.severe("Could not connect to Discord webhook!");
			}
		}
	}

}
