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

import java.util.List;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.GrowInitialiser;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

public class WhighamMutation implements GRMutation {

	// The controlling model.
	private final GRModel model;

	private RandomNumberGenerator rng;

	private final GrowInitialiser init;

	public WhighamMutation(final GRModel model) {
		this.model = model;

		init = new GrowInitialiser(model);

		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {

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
		rng = model.getRNG();
	}

	@Override
	public GRCandidateProgram mutate(final CandidateProgram program) {
		final GRCandidateProgram mutatedProgram = (GRCandidateProgram) program
				.clone();

		final NonTerminalSymbol parseTree = mutatedProgram.getParseTree();

		// This is v.inefficient because we have to fly up and down the tree 
		// lots of times.
		final List<Integer> nonTerminals = parseTree.getNonTerminalIndexes();

		// Choose a node to change.
		final int selection = nonTerminals
				.get(rng.nextInt(nonTerminals.size()));
		final NonTerminalSymbol point = (NonTerminalSymbol) parseTree
				.getNthSymbol(selection);
		final int originalDepth = point.getDepth();

		// Construct a new subtree from that node's grammar rule.
		final GrammarRule rule = point.getGrammarRule();
		final NonTerminalSymbol replacement = init.getGrownParseTree(originalDepth,
				rule);

		// Replace node.
		if (selection == 0) {
			mutatedProgram.setParseTree(replacement);
		} else {
			parseTree.setNthSymbol(selection, replacement);
		}

		return mutatedProgram;
	}
}
