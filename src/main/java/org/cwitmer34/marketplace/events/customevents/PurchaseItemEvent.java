package org.cwitmer34.marketplace.events.customevents;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PurchaseItemEvent extends Event  {
	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	private final Player player;
	@Getter
	private final ItemStack item;
	@Getter
	private final int price;

	public PurchaseItemEvent(Player player, ItemStack item, int price) {
		this.player = player;
		this.item = item;
		this.price = price;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}

}

