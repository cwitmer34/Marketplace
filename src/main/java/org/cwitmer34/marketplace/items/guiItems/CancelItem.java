package org.cwitmer34.marketplace.items.guiItems;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.cwitmer34.marketplace.guis.MarketplaceGUI;
import org.cwitmer34.marketplace.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class CancelItem extends AbstractItem {

	private final Item cancel = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("§cCancel").addLoreLines("§7Click to cancel your purchase"));

	@Override
	public ItemProvider getItemProvider() {
		return cancel.getItemProvider();
	}

	@Override
	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
		if (!clickType.isLeftClick()) return;
		player.sendMessage(GeneralUtil.prefix.append(Component.text("You cancelled your purchase").color(NamedTextColor.GREEN)));
		Window.single()
						.setTitle("Marketplace")
						.setGui(MarketplaceGUI.getGui())
						.open(player);
	}
}