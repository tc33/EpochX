package org.epochx.selection;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import org.epochx.*;

public class RouletteSelector extends AbstractSelector {

	private double[] roulette;

	@Override
	public void setup(Population population) {
		Fitness best = population.get(0).getFitness();
		Fitness worst = best;

		if (!(best instanceof DoubleFitness)) {
			throw new IllegalArgumentException("Fitness not supported: " + best.getClass());
		}

		roulette = new double[population.size()];
		double total = 0.0;

		for (int i = 0; i < population.size(); i++) {
			Fitness fitness = population.get(i).getFitness();
			if (fitness.compareTo(best) > 0) {
				best = fitness;
			} else if (fitness.compareTo(worst) < 0) {
				worst = fitness;
			}

			roulette[i] = ((DoubleFitness) fitness).getValue();
			total += roulette[i];
		}

		double bestValue = ((DoubleFitness) best).getValue();
		double worstValue = ((DoubleFitness) worst).getValue();

		// Invert if minimising - using adjusted fitness.
		if (bestValue < worstValue) {
			total = 0.0;
			double delta = (bestValue < 0) ? Math.abs(bestValue) : 0.0;
			for (int i = 0; i < population.size(); i++) {
				roulette[i] = 1 / (1 + delta + roulette[i]);
				total += roulette[i];
			}
		}

		// Normalise roulette values and accumulate.
		double cumulative = 0.0;
		for (int i = 0; i < population.size(); i++) {
			roulette[i] = cumulative + (roulette[i] / total);
			cumulative = roulette[i];
		}
		roulette[population.size() - 1] = 1.0;

		super.setup(population);
	}

	public Individual select() {
		double random = Config.getInstance().get(RANDOM_SEQUENCE).nextDouble();

		for (int i = 0; i < roulette.length; i++) {
			if (random < roulette[i]) {
				return population.get(i);
			}
		}

		throw new IllegalStateException("Illegal roulette probabilities");
	}

}
