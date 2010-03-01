package org.epochx.gr.op.crossover;

import java.util.*;

import org.epochx.core.Controller;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.GenerationListener;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

public class WhighamCrossover implements GRCrossover, GenerationListener {

	// The current controlling model.
	private GRModel model;
	
	// The random number generator in use.
	private RandomNumberGenerator rng;
	
	public WhighamCrossover(GRModel model) {
		this.model = model;
		
		Controller.getLifeCycleManager().addGenerationListener(this);
		
		initialise();
	}
	
	/*
	 * Initialises WhighamCrossover, in particular all parameters from the 
	 * model should be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		rng = model.getRNG();
	}
	
	@Override
	public GRCandidateProgram[] crossover(CandidateProgram p1,
			CandidateProgram p2) {

		GRCandidateProgram child1 = (GRCandidateProgram) p1;
		GRCandidateProgram child2 = (GRCandidateProgram) p2;
		
		NonTerminalSymbol parseTree1 = child1.getParseTree();
		NonTerminalSymbol parseTree2 = child2.getParseTree();
		
		//TODO Implement getNoNonTerminals(), getNthTerminal() etc methods in Grammar parse tree representation.
		List<NonTerminalSymbol> nonTerminals1 = parseTree1.getNonTerminalSymbols();
		List<NonTerminalSymbol> nonTerminals2 = parseTree2.getNonTerminalSymbols();
		
		int selection = rng.nextInt(nonTerminals1.size());
		
		NonTerminalSymbol point1 = nonTerminals1.get(selection);
		
		// Generate a list of matching non-terminals from the second program.
		List<NonTerminalSymbol> matchingNonTerminals = new ArrayList<NonTerminalSymbol>();
		for (NonTerminalSymbol nt: nonTerminals2) {
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
			NonTerminalSymbol point2 = matchingNonTerminals.get(selection);
			
			// Swap the non-terminals' children.			
			List<Symbol> temp = point1.getChildren();
			point1.setChildren(point2.getChildren());
			point2.setChildren(temp);
		}

		return new GRCandidateProgram[]{child1, child2};
	}

	@Override
	public Object[] getOperatorStats() {
		return null;
	}

	/**
	 * Called at the start of each generation. For each generation we should 
	 * reset all parameters taken from the model incase they've changed. The 
	 * generation event is then CONFIRMed.
	 */
	@Override
	public void onGenerationStart() {
		// Reset.
		initialise();
	}
}
