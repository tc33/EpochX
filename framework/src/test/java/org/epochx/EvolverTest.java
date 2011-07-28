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
package org.epochx;

import java.util.Collection;

import junit.framework.TestCase;

import org.epochx.Evolver.Placeholder;

/**
 * The <code>EvolverTest</code> class provides unit tests for methods of the
 * {@link Evolver} class.
 * 
 * @see Evolver
 */
public class EvolverTest extends TestCase {

	/**
	 * Test for the {@link Evolver#add(Placeholder, Component)} method.
	 */
	public void testAdd() {
		Evolver evolver = new Evolver();
		assertTrue(evolver.get(Placeholder.START).isEmpty());
		evolver.add(Placeholder.START, new Pipeline());
		assertFalse(evolver.get(Placeholder.START).isEmpty());
	}

	/**
	 * Test for the {@link Evolver#clear(Placeholder)} method.
	 */
	public void testClear() {
		Evolver evolver = new Evolver();
		evolver.add(Placeholder.END, new Pipeline());
		assertFalse(evolver.get(Placeholder.END).isEmpty());
		evolver.clear(Placeholder.END);
		assertTrue(evolver.get(Placeholder.END).isEmpty());
	}

	/**
	 * Test for the {@link Evolver#get(Placeholder)} method.
	 */
	public void testGet() {
		Evolver evolver = new Evolver();
		Pipeline component = new Pipeline();
		evolver.add(Placeholder.AFTER_EVALUATION, component);
		Collection<Component> list = evolver.get(Placeholder.AFTER_EVALUATION);
		assertTrue(list.contains(component));
		assertEquals(1, list.size());
	}

	/**
	 * Test for the {@link Evolver#remove(Placeholder, Component)} method.
	 */
	public void testRemove() {
		Evolver evolver = new Evolver();
		Pipeline component1 = new Pipeline();
		Pipeline component2 = new Pipeline();

		evolver.add(Placeholder.AFTER_INITIALISATION, component1);
		evolver.add(Placeholder.AFTER_INITIALISATION, component2);

		Collection<Component> list = evolver.get(Placeholder.AFTER_INITIALISATION);
		assertTrue(list.contains(component1));
		assertTrue(list.contains(component2));
		assertEquals(2, list.size());

		evolver.remove(Placeholder.AFTER_INITIALISATION, component1);

		list = evolver.get(Placeholder.AFTER_INITIALISATION);
		assertFalse(list.contains(component1));
		assertTrue(list.contains(component2));
		assertEquals(1, list.size());
	}

}