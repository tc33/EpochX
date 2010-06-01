package org.epochx.gx.op.init;

import java.util.*;

import org.apache.commons.lang.*;
import org.epochx.gr.model.*;
import org.epochx.gr.op.init.*;
import org.epochx.gr.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.*;

public class ExperimentalInitialiser implements GRInitialiser {

	// The controlling model.
	private GRModel model;
	
	private RandomNumberGenerator rng;
	private Grammar grammar;
	private int popSize;
	private int maxInitialProgramDepth;
	
	private List<String> declaredVariables;
	private int variableIndex;
	
	public ExperimentalInitialiser(GRModel model) {
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
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		maxInitialProgramDepth = model.getMaxInitialDepth();
	}
	
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
		
		for (int i=0; i<popSize; i++) {
			pop.add(initialiseProgram());
			System.out.println(pop.get(i));
		}
		
		return pop;
	}
	
	private GRCandidateProgram initialiseProgram() {
		GrammarRule startRule = grammar.getStartRule();
		
		declaredVariables = new ArrayList<String>();
		variableIndex = 0;
		
		NonTerminalSymbol parseTree = new NonTerminalSymbol(startRule);
		
		buildDerivationTree(parseTree, startRule, 0, maxInitialProgramDepth);
		
		return new GRCandidateProgram(parseTree, model);
	}
	
	private void buildDerivationTree(NonTerminalSymbol parseTree, GrammarRule rule, int depth, int maxDepth) {		
		// Check if theres more than one production.
		int productionIndex = 0;
		int noProductions = rule.getNoProductions();
		if (noProductions > 1) {
			List<Integer> validProductions = getValidProductionIndexes(rule.getProductions(), maxDepth-depth-1);
			
			// Choose a production randomly.
			int chosenProduction = rng.nextInt(validProductions.size());
			productionIndex = validProductions.get(chosenProduction);
		}

		// Drop down the tree at this production.
		GrammarProduction p = rule.getProduction(productionIndex);
		
		List<GrammarNode> grammarNodes = p.getGrammarNodes();
		for (GrammarNode node: grammarNodes) {
			if (node instanceof GrammarRule) {
				GrammarRule r = (GrammarRule) node;
				
				NonTerminalSymbol nt = new NonTerminalSymbol((GrammarRule) node);
				
				buildDerivationTree(nt, r, depth+1, maxDepth);
				
				parseTree.addChild(nt);
			} else {
				GrammarLiteral literal = (GrammarLiteral) node;
				
				if (literal.isDynamic()) {
					// Is a dynamic terminal.
					literal = resolveDynamicLiteral(literal);
				}
				
				// Must be a grammar literal.
				parseTree.addChild(new TerminalSymbol(literal));
			}
		}
	}
	
	private GrammarLiteral resolveDynamicLiteral(GrammarLiteral literal) {
		String value = literal.getValue();
		GrammarLiteral replacement = null;
		
		if (value.equals("@var-decl")) {
			String varName = "var" + variableIndex++;
			replacement = new GrammarLiteral(varName);
			declaredVariables.add(varName);			
		} else if (value.equals("@var-any")) {
			String varName = declaredVariables.get(rng.nextInt(declaredVariables.size()));
			replacement = new GrammarLiteral(varName);
		} else if (value.equals("@random-int")) {
			int random = rng.nextInt();
			replacement = new GrammarLiteral(Integer.toString(random));
		} else {
			throw new MalformedGrammarException("unknown dynamic literal");
		}
		
		return replacement;
	}
	
	private List<Integer> getValidProductionIndexes(List<GrammarProduction> grammarProductions, int maxDepth) {
		List<Integer> indexes = new ArrayList<Integer>();
		
		for (int i=0; i<grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);
			
			boolean valid = true;
			
			String varsAttribute = (String) p.getAttribute("vars");
			if (ObjectUtils.equals(varsAttribute, "not-empty")) {
				valid = !declaredVariables.isEmpty();
			} else if (ObjectUtils.equals(varsAttribute, "empty")) {
				valid = declaredVariables.isEmpty();
			}
			
			if (valid && (p.getMinDepth() <= maxDepth)) {
				indexes.add(i);
			}
		}
		
		// If there were any valid recursive productions, return them, otherwise use the others.
		return indexes;
	}

}
