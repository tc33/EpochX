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

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.List;

import org.epochx.event.*;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 614 $ $Date:: 2011-04-12 12:58:53#$
 */
public class BranchedBreeder implements Breeder, Listener<ConfigEvent> {

	private List<Operator> operators;

	private IndividualSelector selector;

	private RandomSequence random;

	public BranchedBreeder() {
		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}

	public Population process(Population population) {
		selector.setup(population);

		Population newPopulation = new Population();
		int size = population.size();

		double[] probabilities = new double[operators.size()];
		double cumulative = 0.0;
		for (int i = 0; i < operators.size(); i++) {
			cumulative += operators.get(i).probability();
			probabilities[i] = cumulative;
		}

		while (size > 0) {
			double r = random.nextDouble() * cumulative;
			Operator operator = null;
			for (int i = 0; i < probabilities.length; i++) {
				if (r < probabilities[i]) {
					operator = operators.get(i);
				}
			}

			Individual[] parents = new Individual[operator.inputSize()];

			for (int i = 0; i < parents.length; i++) {
				parents[i] = selector.select();
			}

			parents = operator.apply(parents);

			for (int i = 0; (i < parents.length) && (size > 0); i++) {
				newPopulation.add(parents[i]);
				size--;
			}
		}

		return newPopulation;
	}

	protected void setup() {
		operators = Config.getInstance().get(OPERATORS);
		selector = Config.getInstance().get(SELECTOR);
		random = Config.getInstance().get(RANDOM_SEQUENCE);
	}

	public void onEvent(ConfigEvent event) {
		if ((event.getKey() == OPERATORS) || (event.getKey() == SELECTOR) || (event.getKey() == RANDOM_SEQUENCE)) {
			setup();
		}
	}
}