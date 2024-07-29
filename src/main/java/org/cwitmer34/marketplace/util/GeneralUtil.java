package org.cwitmer34.marketplace.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GeneralUtil {

	public static final Component prefix = Component.empty().append(Component.text(" ⚡ ").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));

	public static String legacyFromComponent(Component component) {
		return LegacyComponentSerializer.legacyAmpersand().serialize(component);
	}

	public static ItemStack itemStackFromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			return (ItemStack) dataInput.readObject();
		} catch (Exception e) {
			throw new IOException("unable to decode:", e);
		}
	}

	public static Item itemStackToItem(ItemStack item) {
		return new SimpleItem(item);
	}

	public static String itemStackToBase64(ItemStack item) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeObject(item);
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("cannot save item stack:", e);
		}
	}

	public static double getPrice(Item item) {
		ItemStack itemStack = item.getItemProvider().get();
		itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey("marketplace", "price"), PersistentDataType.DOUBLE);
		return 0;
	}
}
