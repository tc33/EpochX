/*
 * $Id: Population.java 615 2011-04-14 11:27:06Z tc33 $
 */

package org.epochx;

import java.util.ArrayList;
import java.util.List;

import org.epochx.Config.ConfigKey;

/**
 * TODO: make it iterable
 * 
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 615 $ $Date:: 2011-04-14 12:27:06#$
 */
public class Population {
	public static final ConfigKey<Integer> SIZE = new ConfigKey<Integer>();

	private List<Individual> individuals;

	public Population() {
		individuals = new ArrayList<Individual>(Config.getInstance().get(SIZE));
	}

	public int size() {
		return individuals.size();
	}

	public void add(Individual individual) {
		individuals.add(individual);
	}

	public Individual get(int index) {
		return individuals.get(index);
	}

	public Individual fittest() {
		Individual fittest = null;

		for (Individual individual : individuals) {
			if (fittest == null
					|| individual.getFitness().compareTo(fittest.getFitness()) > 0) {
				fittest = individual;
			}
		}

		return fittest;
	}
}