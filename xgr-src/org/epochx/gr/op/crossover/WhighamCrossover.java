package org.epochx.gr.op.crossover;

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
		
		int noNonTerminals1 = parseTree1.getNoNonTerminalSymbols();
		
		int point1 = parseTree1.getNonTerminalSymbolIndex(rng.nextInt(noNonTerminals1));
		 
		GrammarRule grammarRule = parseTree1.getGrammarRule(point1);
		
		// How many non-terminals in program 2 with the same rule.
		int noMatches = parseTree2.getNoNonTerminalSymbols(grammarRule);		
		
		if (noMatches == 0) {
			return null;
		} else {
			// Randomly choose a second point out of the matching non-terminals.
			int point2 = parseTree2.getNonTerminalSymbolIndex(rng.nextInt(noMatches), grammarRule);
			
			parseTree1.swapSubtree(point1, parseTree2, point2);
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
