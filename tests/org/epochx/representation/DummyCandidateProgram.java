package org.epochx.representation;


public class DummyCandidateProgram extends CandidateProgram {
	
	private double fitness;
	
	private boolean valid;
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public double getFitness() {
		return fitness;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@Override
	public boolean isValid() {
		return valid;
	}
}
