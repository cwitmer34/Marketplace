package org.cwitmer34.marketplace.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PurchaseItemEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
