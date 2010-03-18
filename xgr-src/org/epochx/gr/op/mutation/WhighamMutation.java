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

import java.util.*;

import org.epochx.core.Controller;
import org.epochx.gp.representation.*;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.GrowInitialiser;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

public class WhighamMutation implements GRMutation {

	private RandomNumberGenerator rng;
	
	private GrowInitialiser init;
	
	public WhighamMutation() {
		init = new GrowInitialiser();

		// Configure parameters from the model.
		LifeCycleManager.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		GRModel model = (GRModel) Controller.getModel();
		
		rng = model.getRNG();
	}
	
	@Override
	public GRCandidateProgram mutate(CandidateProgram program) {
		GRCandidateProgram mutatedProgram = (GRCandidateProgram) program.clone();
		
		NonTerminalSymbol parseTree = mutatedProgram.getParseTree();
		
		//TODO This is v.inefficient because we have to fly up and down the tree lots of times.
		List<Integer> nonTerminals = parseTree.getNonTerminalIndexes();
		
		// Choose a node to change.
		int selection = nonTerminals.get(rng.nextInt(nonTerminals.size()));
		NonTerminalSymbol point = (NonTerminalSymbol) parseTree.getNthSymbol(selection);
		int originalDepth = point.getDepth();
		
		/*
		 * TODO At the mo we use a max depth of whatever the original had but 
		 * would be better to use up to the maximum program depth. This is 
		 * difficult though because we don't know how deep the node with chose 
		 * is. 
		 */
		
		// Construct a new subtree from that node's grammar rule.
		GrammarRule rule = point.getGrammarRule();
		NonTerminalSymbol replacement = init.growParseTree(originalDepth, rule);
		
		// Replace node.
		if (selection == 0) {
			mutatedProgram.setParseTree(replacement);
		} else {
			parseTree.setNthSymbol(selection, replacement);
		}
		
		FunctionParser parser = new FunctionParser();
		try {
			parser.parse(parseTree.toString());
		} catch (MalformedProgramException e) {
			System.out.println("");			
		}
		
		return mutatedProgram;
	}
}
