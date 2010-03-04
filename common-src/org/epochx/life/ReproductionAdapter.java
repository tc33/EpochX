package org.epochx.life;

import org.epochx.representation.CandidateProgram;

public abstract class ReproductionAdapter implements ReproductionListener {

	@Override
	public CandidateProgram onReproduction(CandidateProgram child) {
		return child;
	}

	@Override
	public void onReproductionEnd() {}

	@Override
	public void onReproductionStart() {}

}
