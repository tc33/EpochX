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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.epochx.Config.ConfigKey;

/**
 * A <code>Population</code> is an ordered collection of {@link Individual}s.
 */
public class Population implements Iterable<Individual> {

	// TODO: make it serializable

	/**
	 * The key for setting and retrieving the population size configuration
	 * parameter.
	 */
	public static final ConfigKey<Integer> SIZE = new ConfigKey<Integer>();

	/**
	 * The list of individuals of this propulation.
	 */
	private final ArrayList<Individual> individuals;

	/**
	 * Constructs an empty <code>Population</code>.
	 */
	public Population() {
		individuals = new ArrayList<Individual>(Config.getInstance().get(SIZE));
	}

	/**
	 * Returns the number of individuals within this population.
	 * 
	 * @return the number of individuals in this population
	 */
	public int size() {
		return individuals.size();
	}

	/**
	 * Appends the specified individual to the end of this population.
	 * 
	 * @param individual the individual to add to this population
	 */
	public void add(Individual individual) {
		individuals.add(individual);
	}

	/**
	 * Returns the individual at the specified index in this population.
	 * 
	 * @param index the index of the individual to be returned
	 * @return the individual at the specified index position
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *         <code>(index < 0 || index > size())</code>
	 */
	public Individual get(int index) {
		return individuals.get(index);
	}

	/**
	 * Returns the individual in this population with the best fitness. If
	 * multiple individuals have equal fitnesses then the individual with the
	 * lowest index will be returned.
	 * 
	 * @return an <code>Individual</code> with the best fitness in this
	 *         population.
	 */
	public Individual fittest() {
		Individual fittest = null;

		for (Individual individual: individuals) {
			if ((fittest == null) || (individual.getFitness().compareTo(fittest.getFitness()) > 0)) {
				fittest = individual;
			}
		}

		return fittest;
	}

	/**
	 * Returns the group of best individuals of the population.
	 * 
	 * @param size the number of individuals of the group (elite).
	 * 
	 * @return the group of best individuals of the population.
	 */
	public Individual[] elite(int size) {
		@SuppressWarnings("unchecked")
		ArrayList<Individual> copy = (ArrayList<Individual>) individuals.clone();
		Collections.sort(copy, new Comparator<Individual>() {

			public int compare(Individual o1, Individual o2) {
				return o1.getFitness().compareTo(o2.getFitness());
			}
		});

		Individual[] fittest = new Individual[size];

		for (int i = 0; i < size; i++) {
			fittest[i] = copy.get(i);
		}

		return fittest;
	}

	/**
	 * Returns an iterator over the individuals in this population.
	 * 
	 * @return an iterator over the individuals in this population.
	 */
	public Iterator<Individual> iterator() {
		return individuals.iterator();
	}

}