package org.cwitmer34.marketplace.timeformat;

import org.ocpsoft.prettytime.TimeUnit;

public class Ticks implements TimeUnit {
	@Override
	public long getMillisPerUnit() {
		return 50;
	}

	@Override
	public long getMaxQuantity() {
		return 0;
	}

	@Override
	public boolean isPrecise() {
		return false;
	}
}
