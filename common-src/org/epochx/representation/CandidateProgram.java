package org.epochx.representation;

public abstract class CandidateProgram implements Cloneable, Comparable<CandidateProgram> {

	public abstract double getFitness();
	
	@Override
	public CandidateProgram clone() {
		CandidateProgram clone = null;
		try {
			clone = (CandidateProgram) super.clone();
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everything is 
			// going to blow up anyway.
		}
		
		return clone;
	}
	
	/**
	 * Compares this program to another based upon fitness. Returns a negative 
	 * integer if this program has a larger (worse) fitness value, zero if they 
	 * have equal fitnesses and a positive integer if this program has a 
	 * smaller (better) fitness value.
	 * 
	 * This is super expensive if using to sort a list. Might be possible to 
	 * improve performance if we can implement caching of fitness within a 
	 * GPCandidateProgram.
	 * 
	 * @param o the GPCandidateProgram to be compared.
	 * @return a negative integer, zero, or a positive integer if this program 
	 * has a worse, equal or better fitness respectively. 
	 */
	@Override
	public int compareTo(CandidateProgram o) {
		double thisFitness = this.getFitness();
		double objFitness = o.getFitness();
		
		if (thisFitness > objFitness) {
			return -1;
		} else if (thisFitness == objFitness) {
			return 0;
		} else {
			return 1;
		}
	}
}
