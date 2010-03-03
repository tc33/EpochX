package org.epochx.op;

import org.epochx.representation.CandidateProgram;

public interface Crossover extends Operator {

	public CandidateProgram[] crossover(CandidateProgram parent1, CandidateProgram parent2);
	
}
