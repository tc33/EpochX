package org.epochx.life;

import java.util.List;

import org.epochx.representation.CandidateProgram;

public class PoolSelectionAdapter implements PoolSelectionListener {

	@Override
	public void onPoolSelectionStart() {}
	
	@Override
	public List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool) {
		return pool;
	}

	@Override
	public void onPoolSelectionEnd() {}

}
