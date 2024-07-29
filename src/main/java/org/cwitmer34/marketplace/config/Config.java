package org.cwitmer34.marketplace.config;

import org.cwitmer34.marketplace.TrialMarketplace;

public class Config {
	public static void reload() {
		TrialMarketplace.getPlugin().reloadConfig();
	}


	public static boolean init() {
		return true;
	}


}
