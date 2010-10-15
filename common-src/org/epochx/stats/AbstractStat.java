package org.epochx.stats;

import org.epochx.stats.StatsManager.*;

public abstract class AbstractStat implements Stat {

	private ExpiryEvent expiry;
	
	public AbstractStat(ExpiryEvent expiry) {
		this.expiry = expiry;
	}
	
	@Override
	public ExpiryEvent getExpiryEvent() {
		return expiry;
	}
	
	@Override
	public Object getStatValue() {
		return null;
	}

}
