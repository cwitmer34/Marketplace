package org.cwitmer34.marketplace.items.guiItems;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.config.ButtonsConfig;
import org.cwitmer34.marketplace.data.mongo.collect.PlayerCollect;
import org.cwitmer34.marketplace.guis.CollectGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.List;

public class GoToCollectItem extends AbstractItem {

	@Override
	public ItemProvider getItemProvider() {
		return new ItemBuilder(ButtonsConfig.COLLECT).setDisplayName("§eGo to Collect").addLoreLines("§7Click to go to your collect inventory");
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
		if (!clickType.isLeftClick()) return;
		PlayerCollect playerCollect = TrialMarketplace.getCollectHandler().getCollect(player.getUniqueId().toString());
		List<Item> items;
		try {
			items = GeneralUtil.deserializeItems(playerCollect.getSerializedItems());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		CollectGUI collectGUI = TrialMarketplace.getCollectGuis().get(player.getUniqueId().toString());
		try {
			collectGUI.setItems(items);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Window.single().setGui(collectGUI.getGui()).open(player);
		notifyWindows();
	}

}

