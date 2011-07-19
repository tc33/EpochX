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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx;

import java.util.List;

import org.epochx.event.*;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.GenerationEvent.StartGeneration;

/**
 * A <code>GenerationalStrategy</code> is an evolutionary strategy with clearly
 * defined generations. 
 * 
 * @see Breeder
 */
public class GenerationalStrategy extends Pipeline implements EvolutionaryStrategy, Listener<ConfigEvent> {

	private List<TerminationCriteria> criteria;

	/**
	 * Constructs a <code>GenerationalStrategy</code> with the provided 
	 * components. One of those components would typically be a {@link Breeder}.
	 * 
	 * @param components
	 */
	public GenerationalStrategy(Component ... components) {
		for (Component component: components) {
			add(component);
		}

		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}

	@Override
	public Population process(Population population) {

		int generation = 1;
		while (!terminate()) {
			EventManager.getInstance().fire(StartGeneration.class, new StartGeneration(generation, population));

			population = super.process(population);

			EventManager.getInstance().fire(EndGeneration.class, new EndGeneration(generation, population));
			generation++;
		}

		return population;
	}

	protected boolean terminate() {
		for (TerminationCriteria tc: criteria) {
			if (tc.terminate()) {
				return true;
			}
		}
		return false;
	}

	protected void setup() {
		criteria = Config.getInstance().get(EvolutionaryStrategy.TERMINATION_CRITERIA);
	}

	public void onEvent(ConfigEvent event) {
		if (event.getKey() == EvolutionaryStrategy.TERMINATION_CRITERIA) {
			setup();
		}
	}
}