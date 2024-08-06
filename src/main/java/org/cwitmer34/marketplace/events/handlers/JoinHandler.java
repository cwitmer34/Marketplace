package org.cwitmer34.marketplace.events.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.collect.PlayerCollect;
import org.cwitmer34.marketplace.guis.CollectGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;

public class JoinHandler implements Listener {
	public JoinHandler() {
		MarketplaceMain.getPlugin().getServer().getPluginManager().registerEvents(this, MarketplaceMain.getPlugin());
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PlayerCollect playerCollect = MarketplaceMain.getCollectHandler().getOrCreateCollect(player.getUniqueId().toString());
		if (!MarketplaceMain.getCollectGuis().containsKey(player.getUniqueId().toString())) {
			MarketplaceMain.getCollectGuis().put(player.getUniqueId().toString(), new CollectGUI());
		}

		if (!playerCollect.getSerializedItems().isEmpty()) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.hasCollectItems));
		}
	}
}
