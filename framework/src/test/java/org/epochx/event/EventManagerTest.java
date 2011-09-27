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

package org.epochx.event;

import junit.framework.TestCase;

/**
 * 
 */
public class EventManagerTest extends TestCase {

	public void testReset() {
		EventManager manager = EventManager.getInstance();
		Listener<RunEvent> listener = new Listener<RunEvent>() {

			public void onEvent(RunEvent event) {
			}
		};
		
		manager.add(RunEvent.class, listener);
		assertTrue(manager.remove(RunEvent.class, listener));
		
		manager.add(RunEvent.class, listener);
		manager.reset();
		assertFalse(manager.remove(RunEvent.class, listener));
	}
}