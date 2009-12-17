package org.epochx.op;

import java.util.List;

import org.epochx.representation.CandidateProgram;


public interface PoolSelector {
	
	public List<CandidateProgram> getPool(List<CandidateProgram> pop, int poolSize);

}
