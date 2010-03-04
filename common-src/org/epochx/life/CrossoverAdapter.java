package org.epochx.life;

import org.epochx.representation.CandidateProgram;

public abstract class CrossoverAdapter implements CrossoverListener {

	@Override
	public void onCrossoverStart() {}
	
	@Override
	public CandidateProgram[] onCrossover(CandidateProgram[] parents,
			CandidateProgram[] children) {
		return children;
	}

	@Override
	public void onCrossoverEnd() {}

}
