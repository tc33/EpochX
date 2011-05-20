/*
 * $Id: BranchedBreeder.java 614 2011-04-12 11:58:53Z tc33 $
 */

package org.epochx;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.List;

import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;

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

			for (int i = 0; i < parents.length && size > 0; i++) {
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
		if (event.getKey() == OPERATORS || event.getKey() == SELECTOR
				|| event.getKey() == RANDOM_SEQUENCE) {
			setup();
		}
	}
}