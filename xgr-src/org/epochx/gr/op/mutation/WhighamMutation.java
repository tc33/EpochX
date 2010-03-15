/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gr.op.mutation;

import org.epochx.core.Controller;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.GrowInitialiser;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

public class WhighamMutation implements GRMutation {

	private RandomNumberGenerator rng;
	
	private GrowInitialiser init;
	
	public WhighamMutation() {
		init = new GrowInitialiser();
		
		// Initialise parameters.
		updateModel();
		
		// Re-initialise parameters on each generation.
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				updateModel();
			}
		});
	}
	
	/*
	 * Initialises WhighamMutation, in particular all parameters from the 
	 * model should be refreshed incase they've changed since the last call.
	 */
	private void updateModel() {
		rng = Controller.getModel().getRNG();
	}
	
	@Override
	public GRCandidateProgram mutate(CandidateProgram program) {
		GRCandidateProgram mutatedProgram = (GRCandidateProgram) program.clone();
		/*NonTerminalSymbol parseTree = mutatedProgram.getParseTree();
		
		List<NonTerminalSymbol> nonTerminals = parseTree.getNonTerminalSymbols();
		
		int selection = rng.nextInt(nonTerminals.size());
		
		NonTerminalSymbol point = nonTerminals.get(selection);
		
		GrammarRule rule = point.getGrammarRule();
			
		NonTerminalSymbol replacement = init.growParseTree(5, rule);
		
		// Replace current children with our new children.
		point.setChildren(replacement.removeChildren());*/
		
		return mutatedProgram;
	}

}
