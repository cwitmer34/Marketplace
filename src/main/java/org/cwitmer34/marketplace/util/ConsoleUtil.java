package org.cwitmer34.marketplace.util;

import org.cwitmer34.marketplace.MarketplaceMain;

import java.util.logging.Logger;

public class ConsoleUtil {
	static final Logger logger = MarketplaceMain.getPlugin().getLogger();

	public static void severe(String message) {
		logger.severe(message);
	}

	public static void warning(String message) {
		logger.warning(message);
	}

	public static void info(String message) {
		logger.info(message);
	}
}
