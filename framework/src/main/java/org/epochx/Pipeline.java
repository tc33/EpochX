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

import java.util.*;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 609 $ $Date:: 2011-04-07 11:21:50#$
 */
public class Pipeline implements Component {

	private final List<Component> pipeline;

	public Pipeline() {
		pipeline = new ArrayList<Component>();
	}

	public Population process(Population population) {
		for (Component component: pipeline) {
			population = component.process(population);
		}

		return population;
	}

	public void add(Component component) {
		pipeline.add(component);
	}

	public void add(int index, Component component) {
		pipeline.add(index, component);
	}

	public boolean remove(Component component) {
		return pipeline.remove(component);
	}

	public Component remove(int index) {
		return pipeline.remove(index);
	}
}