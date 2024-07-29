//package org.cwitmer34.marketplace.items.guiItems;
//
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.ClickType;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//import org.cwitmer34.marketplace.data.itempdc.ItemHandler;
//import org.cwitmer34.marketplace.guis.ConfirmationGUI;
//import org.cwitmer34.marketplace.util.GeneralUtil;
//import org.jetbrains.annotations.NotNull;
//import xyz.xenondevs.invui.item.Item;
//import xyz.xenondevs.invui.item.ItemProvider;
//import xyz.xenondevs.invui.item.builder.ItemBuilder;
//import xyz.xenondevs.invui.item.impl.AbstractItem;
//import xyz.xenondevs.invui.item.impl.SimpleItem;
//import xyz.xenondevs.invui.window.Window;
//
//public class SellItem extends AbstractItem {
//
//	Item item;
//	double price;
//
//	public SellItem(ItemStack itemStack, double price) {
//		ItemHandler.setPrice(itemStack, price);
//		this.item = initItem(itemStack);
//		this.price = price;
//	}
//
//	private Item initItem(ItemStack itemStack) {
//		return new ListedItem(itemStack);
//	}
//
//	@Override
//	public ItemProvider getItemProvider() {
//		return item.getItemProvider();
//	}
//
//	@Override
//	public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
//		if (!clickType.isLeftClick()) return;
//		Window.single()
//						.setGui(ConfirmationGUI.get(event.getCurrentItem())) // TODO: change this to active listings
//						.open(player);
//		notifyWindows();
//
//		player.sendMessage("You bought " + event.getCurrentItem().getI18NDisplayName());
//	}
//
//}