package org.cwitmer34.marketplace.events.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.discord.DiscordWebhook;
import org.cwitmer34.marketplace.events.customevents.ListItemEvent;
import org.cwitmer34.marketplace.events.customevents.PurchaseItemEvent;

import java.awt.*;

public class ListItemHandler implements Listener {


	public ListItemHandler() {
		TrialMarketplace.getPlugin().getServer().getPluginManager().registerEvents(this, TrialMarketplace.getPlugin());
	}

	@EventHandler
	public void onPurchaseItem(ListItemEvent event) {
		String name = event.getPlayer().getName();
		String itemName = event.getItem().getItemMeta().getDisplayName();
		int price = event.getPrice();

//		try {
//			TrialMarketplace.getDiscordWebhook().addEmbed(new DiscordWebhook.EmbedObject()
//							.setColor(Color.PINK)
//							.setTitle("Item Listed")
//							.addField("Seller", name, true)
//							.addField("Item", itemName, true)
//							.addField("Price", String.valueOf(price), true));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
