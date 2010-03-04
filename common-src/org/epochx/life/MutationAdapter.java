package org.epochx.life;

import org.epochx.representation.CandidateProgram;

public abstract class MutationAdapter implements MutationListener {

	@Override
	public CandidateProgram onMutation(CandidateProgram parent,
			CandidateProgram child) {
		return child;
	}

	@Override
	public void onMutationEnd() {}

	@Override
	public void onMutationStart() {}

}
