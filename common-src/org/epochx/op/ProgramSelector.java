package org.epochx.op;

import java.util.List;

import org.epochx.representation.CandidateProgram;

public interface ProgramSelector {
	
	public void setSelectionPool(List<CandidateProgram> pop);
	
	public CandidateProgram getProgram();
	
}
