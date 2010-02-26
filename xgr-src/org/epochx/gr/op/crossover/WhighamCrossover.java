package org.epochx.gr.op.crossover;

import java.util.*;

import org.epochx.core.*;
import org.epochx.gr.core.*;
import org.epochx.gr.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.*;

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
		
		GRCandidateProgram program1 = (GRCandidateProgram) p1;
		GRCandidateProgram program2 = (GRCandidateProgram) p2;
		
		GRCandidateProgram child1 = (GRCandidateProgram) program1.clone();
		GRCandidateProgram child2 = (GRCandidateProgram) program2.clone();
		
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
			if (point1.getNoChildren() != point2.getNoChildren()) {
				System.out.println("Uneven no children");
			}
			
			for (int i=0; i<point1.getNoChildren(); i++) {
				Symbol child = point1.getChild(i);
				point1.setChild(i, point2.getChild(i));
				point2.setChild(i, child);
			}
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
