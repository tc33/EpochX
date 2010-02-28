package org.epochx.ge.op.init;

import java.util.*;

import org.epochx.core.Controller;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.RunListener;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;


/**
 * Note: full initialisation currently only works for depth first mapping.
 *
 */
public class FullInitialiser implements GEInitialiser, RunListener {
	/*
	 * TODO This constructs the chromosome using depth first mapping - what about others?
	 */
	
	// The current controlling model.
	private GEModel model;
	
	private RandomNumberGenerator rng;
	private Grammar grammar;
	private int popSize;
	private int maxInitialProgramDepth;
	private int maxCodonSize;
	
	/**
	 * Constructs a full initialiser.
	 * 
	 * @param model
	 */
	public FullInitialiser(GEModel model) {
		this.model = model;
		
		Controller.getLifeCycleManager().addRunListener(this);
	}
	
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Create and add new programs to the population.
		for(int i=0; i<popSize; i++) {
			GECandidateProgram candidate;
			do {
				// Create a new program at the models initial max depth.
				candidate = getInitialProgram(maxInitialProgramDepth);
			} while (firstGen.contains(candidate));
			
			// Add to the new population.
			firstGen.add(candidate);
        }
		
		return firstGen;
	}

	public GECandidateProgram getInitialProgram(int depth) {
		GrammarNode start = grammar.getStartRule();
		
		//TODO Check it is possible to create a program inside the max depth.
		List<Integer> codons = new ArrayList<Integer>();
		
		buildDerivationTree(codons, start, 0, depth);

		return new GECandidateProgram(codons, model);
	}
	
	private void buildDerivationTree(List<Integer> codons, GrammarNode rule, int depth, int maxDepth) {
		
		if (rule instanceof GrammarRule) {
			GrammarRule nt = (GrammarRule) rule;
			
			// Check if theres more than one production.
			int productionIndex = 0;
			int noProductions = nt.getNoProductions();
			if (noProductions > 1) {
				List<Integer> validProductions = getValidProductionIndexes(nt.getProductions(), maxDepth-depth-1);
				
				// Choose a production randomly.
				int chosenProduction = rng.nextInt(validProductions.size());
				productionIndex = validProductions.get(chosenProduction);
				
				// Scale the production index up to get our new codon.
				int codon = convertToCodon(productionIndex, noProductions);
				
				codons.add(codon);
			}

			// Drop down the tree at this production.
			GrammarProduction p = nt.getProduction(productionIndex);
			
			List<GrammarNode> symbols = p.getGrammarNodes();
			for (GrammarNode s: symbols) {
				buildDerivationTree(codons, s, depth+1, maxDepth);
			}
		} else {
			// Do nothing.
		}
		
	}
	
	private List<Integer> getValidProductionIndexes(List<GrammarProduction> grammarProductions, int maxDepth) {
		List<Integer> validRecursive = new ArrayList<Integer>();
		List<Integer> validAll = new ArrayList<Integer>();
		
		for (int i=0; i<grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);
			
			if (p.getMinDepth() <= maxDepth) {
				validAll.add(i);
				
				if (p.isRecursive()) {
					validRecursive.add(i);
				}
			}
		}
		
		// If there were any valid recursive productions, return them, otherwise use the others.
		return validRecursive.isEmpty() ? validAll : validRecursive;
	}
	
	/*
	 * Converts a production choice from a number of productions to a codon by 
	 * scaling the production index up to a random number inside the model's 
	 * max codon size limit, while maintaining the modulo of the number.
	 */
	private int convertToCodon(int productionIndex, int noProductions) {
		int codon = rng.nextInt(maxCodonSize-noProductions);
		
		// Increment codon until it is valid index.
		int currentIndex = codon % noProductions;
		// Comparing separate index count saves us %ing large ints.
		while((currentIndex % noProductions) != productionIndex) {
			codon++;
			currentIndex++;
		}
		
		return codon;
	}

	@Override
	public void onRunStart() {
		rng = model.getRNG();
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		maxInitialProgramDepth = model.getMaxInitialProgramDepth();
		maxCodonSize = model.getMaxCodonSize();
	}
}
