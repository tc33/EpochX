/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.semantics;

import java.util.List;
import com.epochx.core.GPModel;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.representation.TerminalNode;

/**
 * @author lb212
 *
 */
public class AntSemanticModule implements SemanticModule {
	
	private List<TerminalNode<?>> terminals;
	private GPModel model;
	
	/**
	 * Constructor for Ant Semantic Module
	 * @param list List of terminal nodes
	 * @param model The GPModel object
	 */
	public AntSemanticModule(List<TerminalNode<?>> list, GPModel model) {
		this.terminals = list;
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#start()
	 */
	@Override
	public void start() {
		// Not required as we do not need to activate external software to study behaviour
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#stop()
	 */
	@Override
	public void stop() {
		// Not required as we do not need to activate external software to study behaviour
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#behaviourToCode(com.epochx.semantics.Representation)
	 */
	@Override
	public CandidateProgram behaviourToCode(Representation representation) {
		// TODO Auto-generated method stub
		
		// build syntax tree from instructions
		// see GPEquivalenceAA for details
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.epochx.semantics.SemanticModule#codeToBehaviour(com.epochx.core.representation.CandidateProgram)
	 */
	@Override
	public Representation codeToBehaviour(CandidateProgram program) {
		// TODO Auto-generated method stub
		
		// 1 - test run ant collecting moves and orientations
		// 2 - condense move trail by:
		// 3 - remove duplicate if branches
		// 4 - remove sequential orientations
		// 5 - check with GPEquivalenceAA and GenPop for others
		
		return null;
	}
}
