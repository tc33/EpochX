package org.epochx.ge.model.java.sort;

import java.io.File;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.op.init.GrowInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.GenerationStatField;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

/**
 *
 */
public class Sort extends GEAbstractModel {

	private static final int NO_ELEMENTS = 5;
	private static final int NO_TESTS = 10;
	
	private Grammar grammar;
	
	private Executor executor;
	
	private int[][] testSet;
	
	private double fullScore;
	
	public Sort() {
		grammar = new Grammar(new File("example-grammars/Java/Sort.bnf"));
		
		// Set run parameters.
		setPopulationSize(1000);
		setNoGenerations(100);
		setGenStatFields(new GenerationStatField[]{
				GenerationStatField.FITNESS_MIN,
				GenerationStatField.FITNESS_AVE,
				GenerationStatField.BEST_PROGRAM
		});
		setNoElites(30);
		setInitialiser(new GrowInitialiser(this));
		setMaxProgramDepth(15);
		setMaxInitialProgramDepth(15);
		setPoolSize(100);
		
		executor = new JavaEvaluator();
		generateTestSet(NO_ELEMENTS, NO_TESTS);
		
		fullScore = (NO_ELEMENTS-1) * NO_TESTS;
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		double score = 0.0;
		
		if (!program.isValid()) {
			return fullScore;
		}
		
		for (int i=0; i<NO_TESTS; i++) {
			EvoList list = new EvoList(testSet[i]);
			
			try {
				executor.exec(program.getSourceCode(), new String[]{"list"}, new Object[]{list});
			} catch (Exception e) {
				return fullScore;
			}
				
			for (int j=0; j<NO_ELEMENTS-1; j++) {
				if (list.get(j) < list.get(j+1)) {
					score++;
				}
			}
		}
			
		return fullScore - score;
	}

	@Override
	public Grammar getGrammar() {
		return grammar;
	}
	
	private void generateTestSet(int noElements, int noTests) {
		testSet = new int[noTests][noElements];
		for (int i=0; i<noTests; i++) {
			for (int j=0; j<noElements; j++) {
				// Choose random numbers between 0 and 100.
				testSet[i][j] = (int) Math.floor(Math.random() * 100);
			}
		}
	}
	
	/**
	 * 
	 */
	public class EvoList {
		
		private int[] list;
		
		public EvoList(int[] list) {
			this.list = list;
		}
		
		public void swap(int x, int y) {
			if (x < 0 || x >= list.length || 
					y < 0 || y >= list.length) {
				// This is an error...
				
			} else {
				// Do swap.
				int temp = list[x];
				list[y] = list[x];
				list[x] = temp;
			}
		}
		
		/**
		 * Which is larger, the value at index x or y.
		 * 
		 * @param x
		 * @param y
		 */
		public int max(int x, int y) {
			return (list[x] > list[y]) ? x : y;
		}
		
		/**
		 * Which is smaller, the value at index x or y.
		 * 
		 * @param x
		 * @param y
		 */
		public int min(int x, int y) {
			return (list[x] < list[y]) ? x : y;
		}
		
		public int get(int index) {
			return list[index];
		}
		
		public int length() {
			return list.length;
		}
		
		public int increment(int index) {
			int result = index + 1;
			
			// Have the index wrap round to the beginning.
			return (result < list.length) ? result : 0;
		}
		
		public int decrement(int index) {
			int result = index - 1;
			
			// Have the index wrap round to the end.
			return (result >= 0) ? result : (list.length-1);
		}
	}

	public static void main(String[] args) {
		Controller.run(new Sort());
	}
}