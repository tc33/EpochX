package org.epochx.ge.example.bf;

import java.io.File;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.eval.BrainfuckEvaluator;
import org.epochx.tools.grammar.Grammar;


public class HelloWorld extends GEAbstractModel {

	private BrainfuckEvaluator evaluator;
	
	private Grammar grammar;

	public HelloWorld() {
		evaluator = new BrainfuckEvaluator();
		grammar = new Grammar(new File("example-grammars/Brainfuck/brainfuck.bnf"));	
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM});
		setNoRuns(1);
		setNoElites(20);
		setNoGenerations(200);
		setPopulationSize(30000);
		setMaxProgramDepth(10);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(500);
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		byte[] result = (byte[]) evaluator.eval(program.getSourceCode(), null, new Byte[]{});
		
		// hello world
		// 104 101 108 108 111 32 119 111 114 108 100
		// 8 5 12 12 15 0 23 15 18 12 4
		// Calculate how far away each letter is.
		//byte[] target = {8, 5, 12, 12, 15, 0, 23, 15, 18, 12, 4};
		byte[] target = {0,0,0,9};
		
		int count = 0;
		for (int i=1; i<target.length; i++) {
			count += Math.abs(target[i] - result[i]);
		}
		
		return count;
	}

	@Override
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Controller.run(new HelloWorld());
	}
}
