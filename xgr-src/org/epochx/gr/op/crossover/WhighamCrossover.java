package org.epochx.gr.op.crossover;

import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Symbol;

public class WhighamCrossover implements GRCrossover {

	@Override
	public GRCandidateProgram[] crossover(CandidateProgram p1,
			CandidateProgram p2) {
		
		GRCandidateProgram program1 = (GRCandidateProgram) p1;
		GRCandidateProgram program2 = (GRCandidateProgram) p2;
		
		Symbol parseTree = program1.getParseTree();
		
		
		return null;
	}

	@Override
	public Object[] getOperatorStats() {
		return null;
	}

}
