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

import java.util.Comparator;

public abstract class DoubleFitness implements Fitness {

	private static final Comparator<Double> MAXIMISE = new Comparator<Double>() {

		public int compare(Double d1, Double d2) {
			return Double.compare(d1, d2);
		}
	};

	private static final Comparator<Double> MINIMISE = new Comparator<Double>() {

		public int compare(Double d1, Double d2) {
			return Double.compare(d2, d1);
		}
	};

	private final double fitness;

	public DoubleFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getValue() {
		return fitness;
	}

	public int compareTo(Fitness o) {
		if (this.getClass().isAssignableFrom(o.getClass())) {
			return comparator().compare(fitness, ((DoubleFitness) o).fitness);
		} else {
			throw new IllegalArgumentException("Expected " + this.getClass() + ", found " + o.getClass());
		}
	}

	@Override
	public String toString() {
		return Double.toString(fitness);
	}

	public abstract Comparator<Double> comparator();

	public static class Maximise extends DoubleFitness {

		public Maximise(double fitness) {
			super(fitness);
		}

		@Override
		public Comparator<Double> comparator() {
			return MAXIMISE;
		}
	}

	public static class Minimise extends DoubleFitness {

		public Minimise(double fitness) {
			super(fitness);
		}

		@Override
		public Comparator<Double> comparator() {
			return MINIMISE;
		}
	}
}