package com.epochx.life;

import com.epochx.representation.*;

public interface CrossoverListener<TYPE> {

	//public CandidateProgram[] onCrossoverSelection(List<CandidateProgram> pop, CandidateProgram[] parents);
	
	/**
	 * Called after selection and crossover of 2 individuals.
	 * 
	 * @param parents the programs that were selected to undergo crossover.
	 * @param children the programs that were generated as a result of the 
	 * crossover operation.
	 * @return an array of CandidatePrograms to be used as the children of the 
	 * crossover operation, or null if the crossover should be reverted.
	 */
	public CandidateProgram<TYPE>[] onCrossover(CandidateProgram<TYPE>[] parents, CandidateProgram<TYPE>[] children);
	
	//public CandidateProgram[] onCrossoverAndSelection(CandidateProgram[] parents, CandidateProgram[] children);
	
}
