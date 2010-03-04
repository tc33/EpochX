package org.epochx.life;

import java.util.List;

import org.epochx.representation.CandidateProgram;

public class ElitismAdapter implements ElitismListener {

	@Override
	public List<CandidateProgram> onElitism(List<CandidateProgram> elites) {
		return elites;
	}

	@Override
	public void onElitismEnd() {}

	@Override
	public void onElitismStart() {}

}
