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

import org.epochx.event.RunEvent.StartRun;

/**
 * 
 */
public class RunStartTime extends AbstractStat<StartRun> {

	private long time;

	public RunStartTime() {
		super(NO_DEPENDENCIES);
	}

	@Override
	public void onEvent(StartRun event) {
		time = System.nanoTime();
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return Long.toString(getTime());
	}
}