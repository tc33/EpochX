package org.epochx.gr.op.mutation;

import java.util.List;

import org.epochx.core.Controller;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.op.init.GrowInitialiser;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.GenerationListener;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

public class WhighamMutation implements GRMutation, GenerationListener {

	private GRModel model;
	
	private RandomNumberGenerator rng;
	
	private GrowInitialiser init;
	
	public WhighamMutation(GRModel model) {
		this.model = model;
		
		init = new GrowInitialiser(model);
		
		Controller.getLifeCycleManager().addGenerationListener(this);
		
		initialise();
	}
	
	@Override
	public GRCandidateProgram mutate(CandidateProgram program) {
		GRCandidateProgram mutatedProgram = (GRCandidateProgram) program.clone();
		NonTerminalSymbol parseTree = mutatedProgram.getParseTree();
		
		List<NonTerminalSymbol> nonTerminals = parseTree.getNonTerminalSymbols();
		
		int selection = rng.nextInt(nonTerminals.size());
		
		NonTerminalSymbol point = nonTerminals.get(selection);
		
		GrammarRule rule = point.getGrammarRule();
			
		NonTerminalSymbol replacement = init.growParseTree(5, rule);
		
		// Replace current children with our new children.
		point.setChildren(replacement.getChildren());
		
		return mutatedProgram;
	}

	@Override
	public Object[] getOperatorStats() {
		return null;
	}
	
	/*
	 * Initialises WhighamMutation, in particular all parameters from the 
	 * model should be refreshed incase they've changed since the last call.
	 */
	private void initialise() {
		rng = model.getRNG();
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
