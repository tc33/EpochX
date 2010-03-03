package org.epochx.op;

import org.epochx.representation.CandidateProgram;

public interface Mutation extends Operator {

	public CandidateProgram mutate(CandidateProgram program);
	
}
