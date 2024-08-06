package org.cwitmer34.marketplace.events.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.cwitmer34.marketplace.discord.DiscordWebhook;
import org.cwitmer34.marketplace.events.customevents.ListItemEvent;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import org.cwitmer34.marketplace.util.GeneralUtil;

import java.awt.*;

public class ListItemHandler implements Listener {


	public ListItemHandler() {
		TrialMarketplace.getPlugin().getServer().getPluginManager().registerEvents(this, TrialMarketplace.getPlugin());
	}

	@EventHandler
	public void onItemList(ListItemEvent event) {
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

		if (MessageConfig.enableDiscordWebhook) {
			TrialMarketplace.getDiscordWebhook()
							.addEmbed(new DiscordWebhook.EmbedObject()
											.setTitle("New Listing")
											.setDescription(player.getName() + " just listed an item!" + " This listing has a starting time of " + listing.getDuration())
											.setThumbnail("https://cravatar.eu/helmhead/" + player.getName() + "/600.png")
											.addField("Item", itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().displayName().examinableName() : itemStack.getI18NDisplayName(), true)
											.addField("Price", String.valueOf(listing.getPrice()), true)
											.addField("Amount", String.valueOf(itemStack.getAmount()), true)
											.addField("Seller", player.getName(), false)
											.setFooter(listing.getItemUuid(), null)
											.setColor(Color.MAGENTA)
							);

			try {
				TrialMarketplace.getDiscordWebhook().execute();
			} catch (Exception e) {
				ConsoleUtil.severe("Could not connect to Discord webhook!");
			}
		}

	}

}
