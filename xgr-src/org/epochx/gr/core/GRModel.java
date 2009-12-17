package org.epochx.gr.core;

import org.epochx.core.Model;
import org.epochx.gr.op.crossover.GRCrossover;
import org.epochx.gr.op.init.GRInitialiser;
import org.epochx.gr.op.mutation.GRMutation;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;

public interface GRModel extends Model {

	@Override
	public GRInitialiser getInitialiser();

	@Override
	public GRCrossover getCrossover();

	@Override
	public GRMutation getMutation();

	public Grammar getGrammar();

	public int getMaxProgramDepth();
	
	public int getMaxInitialProgramDepth();
	
	@Override
	public double getFitness(CandidateProgram program);

}
