package org.cwitmer34.marketplace.events.customevents;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.jetbrains.annotations.NotNull;

@Getter
public class ListItemEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	private final Player player;
	private final ItemStack itemStack;
	private final PlayerListing listing;

	public ListItemEvent(Player player, ItemStack itemStack, PlayerListing listing) {
		this.player = player;
		this.itemStack = itemStack;
		this.listing = listing;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

}

