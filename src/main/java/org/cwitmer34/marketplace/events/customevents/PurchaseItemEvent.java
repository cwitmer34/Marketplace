package org.cwitmer34.marketplace.events.customevents;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.data.mongo.listings.PlayerListing;
import org.jetbrains.annotations.NotNull;

public class PurchaseItemEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	private final Player buyer;
	@Getter
	private final ItemStack item;
	@Getter
	private final PlayerListing listing;

	public PurchaseItemEvent(Player buyer, ItemStack item, PlayerListing listing) {
		this.buyer = buyer;
		this.item = item;
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

