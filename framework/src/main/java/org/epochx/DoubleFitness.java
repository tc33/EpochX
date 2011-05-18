package org.epochx;

import java.util.*;

public abstract class DoubleFitness implements Fitness {

	private static final Comparator<Double> MAXIMISE = new Comparator<Double>() {
		@Override
		public int compare(Double d1, Double d2) {
			return Double.compare(d1, d2);
		}
	};
	
	private static final Comparator<Double> MINIMISE = new Comparator<Double>() {
		@Override
		public int compare(Double d1, Double d2) {
			return Double.compare(d2, d1);
		}
	};
	
	private double fitness;
	
	public DoubleFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getValue() {
		return fitness;
	}
	
	@Override
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
	
	public static class Maximise extends DoubleFitness
	{
		public Maximise(double fitness) {
			super(fitness);
		}
		
		@Override
		public Comparator<Double> comparator() {
			return MAXIMISE;
		}
	}
	
	public static class Minimise extends DoubleFitness
	{
		public Minimise(double fitness) {
			super(fitness);
		}
		
		@Override
		public Comparator<Double> comparator() {
			return MINIMISE;
		}
	}
}