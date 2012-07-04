/*
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */

package org.epochx.event.stat;

import java.util.concurrent.TimeUnit;

import org.epochx.event.OperatorEvent.EndOperator;

/**
 * 
 */
public abstract class OperatorDuration extends AbstractStat<EndOperator> {

	private long duration;

	public OperatorDuration() {
		super(OperatorEndTime.class);
	}

	@Override
	public void refresh(EndOperator event) {
		long start = AbstractStat.get(OperatorStartTime.class).getTime();
		long end = AbstractStat.get(OperatorEndTime.class).getTime();
		
		duration = end - start;
	}

	public abstract long getDuration();

	@Override
	public String toString() {
		return Long.toString(getDuration());
	}

	public class NanoSeconds extends OperatorDuration {
		@Override
		public long getDuration() {
			return duration;
		}
	}
	
	public class MilliSeconds extends OperatorDuration {
		@Override
		public long getDuration() {
			return TimeUnit.NANOSECONDS.toMillis(duration);
		}
	}
	
	public class Seconds extends OperatorDuration {
		@Override
		public long getDuration() {
			return TimeUnit.NANOSECONDS.toSeconds(duration);
		}
	}
	
	public class Minutes extends OperatorDuration {
		@Override
		public long getDuration() {
			return TimeUnit.NANOSECONDS.toMinutes(duration);
		}
	}
	
	public class Hours extends OperatorDuration {
		@Override
		public long getDuration() {
			return TimeUnit.NANOSECONDS.toHours(duration);
		}
	}
}