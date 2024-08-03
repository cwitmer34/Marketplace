package org.cwitmer34.marketplace.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;

public class ButtonsConfig {
	private static FileConfiguration config = TrialMarketplace.config;
	public static final ItemStack PREVIOUS = checkItem(config.getString("buttons.previous"));
	public static final ItemStack NEXT = checkItem(config.getString("buttons.next"));
	public static final ItemStack BACK = checkItem(config.getString("buttons.back"));
	public static final ItemStack COLLECT = checkItem(config.getString("buttons.collect"));
	public static final ItemStack BORDER = checkItem(config.getString("buttons.background"));
	public static final ItemStack INNER_BORDER = checkItem(config.getString("buttons.inner-border"));


	private static ItemStack checkItem(String path) {
		Material material = Material.matchMaterial(path);
		if (material == null) {
			ConsoleUtil.severe("Invalid material: " + path);
			ConsoleUtil.severe("Disabling plugin...");
			TrialMarketplace.getPlugin().getServer().getPluginManager().disablePlugin(TrialMarketplace.getPlugin());
			return null;
		}
		return new ItemStack(material);
	}

}
