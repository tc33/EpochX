package org.epochx.life;

import java.util.List;

import org.epochx.representation.CandidateProgram;

public abstract class InitialisationAdapter implements InitialisationListener {

	@Override
	public void onInitialisationStart() {}
	
	@Override
	public List<CandidateProgram> onInitialisation(List<CandidateProgram> pop) {
		return pop;
	}

	@Override
	public void onInitialisationEnd() {}

}
