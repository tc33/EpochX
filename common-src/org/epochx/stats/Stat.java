package org.epochx.stats;

import org.epochx.stats.StatsManager.*;

public interface Stat {

	/**
	 * Returns when the data for this statistics field expires. For example a 
	 * value of CROSSOVER means it expires at the start of every crossover 
	 * event, and as such is likely to be cleared or otherwise unreliable from
	 * then until the next time it is set.
	 * 
	 * @return the event at which this statistics field is available to be 
	 * cleared.
	 */
	public ExpiryEvent getExpiryEvent();
	
	public Object getStatValue();
}
