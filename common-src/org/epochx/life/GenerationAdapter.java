package org.epochx.life;

import java.util.List;

import org.epochx.representation.CandidateProgram;

public abstract class GenerationAdapter implements GenerationListener {

	@Override
	public void onGenerationStart() {}
	
	@Override
	public List<CandidateProgram> onGeneration(List<CandidateProgram> pop) {
		return pop;
	}
	
	@Override
	public void onGenerationEnd() {}

}
