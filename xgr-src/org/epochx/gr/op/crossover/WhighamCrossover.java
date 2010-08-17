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
package org.epochx.gr.op.crossover;

import java.util.*;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

public class WhighamCrossover implements GRCrossover {

	// The controlling model.
	private final GRModel model;

	// The random number generator in use.
	private RandomNumberGenerator rng;

	public WhighamCrossover(final GRModel model) {
		this.model = model;

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
	public GRCandidateProgram[] crossover(final CandidateProgram p1,
			final CandidateProgram p2) {

		final GRCandidateProgram child1 = (GRCandidateProgram) p1;
		final GRCandidateProgram child2 = (GRCandidateProgram) p2;

		final NonTerminalSymbol parseTree1 = child1.getParseTree();
		final NonTerminalSymbol parseTree2 = child2.getParseTree();

		// TODO Implement getNoNonTerminals(), getNthTerminal() etc methods in
		// Grammar parse tree representation.
		final List<NonTerminalSymbol> nonTerminals1 = parseTree1
				.getNonTerminalSymbols();
		final List<NonTerminalSymbol> nonTerminals2 = parseTree2
				.getNonTerminalSymbols();

		int selection = rng.nextInt(nonTerminals1.size());

		final NonTerminalSymbol point1 = nonTerminals1.get(selection);

		// Generate a list of matching non-terminals from the second program.
		final List<NonTerminalSymbol> matchingNonTerminals = new ArrayList<NonTerminalSymbol>();
		for (final NonTerminalSymbol nt: nonTerminals2) {
			if (nt.equals(point1)) {
				matchingNonTerminals.add(nt);
			}
		}

		if (matchingNonTerminals.isEmpty()) {
			// No valid points in second program, cancel crossover.
			return null;
		} else {
			// Randomly choose a second point out of the matching non-terminals.
			selection = rng.nextInt(matchingNonTerminals.size());
			final NonTerminalSymbol point2 = matchingNonTerminals
					.get(selection);

			// Swap the non-terminals' children.
			final List<Symbol> temp = point1.getChildren();
			point1.setChildren(point2.getChildren());
			point2.setChildren(temp);
		}

		return new GRCandidateProgram[]{child1, child2};
	}

}
