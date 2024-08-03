package org.cwitmer34.marketplace.events.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.MessageConfig;
import org.cwitmer34.marketplace.data.mongo.collect.PlayerCollect;
import org.cwitmer34.marketplace.guis.CollectGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;

import java.util.List;
import java.util.UUID;

public class JoinHandler implements Listener {
	public JoinHandler() {
		TrialMarketplace.getPlugin().getServer().getPluginManager().registerEvents(this, TrialMarketplace.getPlugin());
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PlayerCollect playerCollect = TrialMarketplace.getCollectHandler().getOrCreateCollect(player.getUniqueId().toString());
		if (!TrialMarketplace.getCollectGuis().containsKey(player.getUniqueId().toString())) {
			TrialMarketplace.getCollectGuis().put(player.getUniqueId().toString(), new CollectGUI());
		}

		if (!playerCollect.getSerializedItems().isEmpty()) {
			player.sendMessage(MessageConfig.prefix + GeneralUtil.colorize(MessageConfig.hasCollectItems));
		}
	}
}
