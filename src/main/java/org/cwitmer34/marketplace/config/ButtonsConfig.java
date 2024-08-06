package org.cwitmer34.marketplace.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;

public class ButtonsConfig {
	public static  Material BM_BORDER = Material.YELLOW_STAINED_GLASS_PANE ;
	public static Material PREVIOUS = Material.RED_CONCRETE;
	public static Material NEXT = Material.LIME_CONCRETE;
	public static Material COLLECT_BORDER = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
	public static Material BORDER = Material.BLACK_STAINED_GLASS_PANE;
	public static Material CONFIRM = Material.LIME_STAINED_GLASS_PANE;
	public static Material BACK = Material.RED_STAINED_GLASS_PANE;
	public static Material COLLECT = Material.BLACK_CONCRETE;

	public static void loadConfig() {
		PREVIOUS = getMaterial("previous", PREVIOUS);
		NEXT = getMaterial("next", NEXT);
		COLLECT = getMaterial("collect", COLLECT);
		BM_BORDER = getMaterial("blackmarket-border", BM_BORDER);
		COLLECT_BORDER = getMaterial("collect-border", COLLECT_BORDER);
		BORDER = getMaterial("border", BORDER);
		CONFIRM = getMaterial("confirm", CONFIRM);
		BACK = getMaterial("back", BACK);

	}

	private static Material getMaterial(String key, Material defaultValue) {
		ConfigurationSection section = TrialMarketplace.getPlugin().getConfig().getConfigurationSection("buttons");
		assert section != null;
		String value = section.getString(key);
		if (value != null) {
			try {
				Material material = Material.matchMaterial(value);
				if (material != null) {
					return material;
				}
				return Material.valueOf(value);
			} catch (IllegalArgumentException e) {
				ConsoleUtil.severe("Invalid material for key: " + key + "(" + value + ")" + ", using default: " + defaultValue);
			}
		}
		return defaultValue;
	}
}
