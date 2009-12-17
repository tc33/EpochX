package org.epochx.ge.example.match;

import java.io.File;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.op.init.FixedLengthInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.grammar.Grammar;


public class StringMatch extends GEAbstractModel {

	private Grammar grammar;
	
	public StringMatch() {
		grammar = new Grammar(new File("example-grammars/StringMatch.bnf"));
		
		DepthFirstMapper mapper = new DepthFirstMapper(this);
		mapper.setRemovingUnusedCodons(false);
		mapper.setWrapping(false);
		mapper.setExtending(false);
		
		setMapper(mapper);
		setInitialiser(new FixedLengthInitialiser(this, 30));
		setMaxChromosomeLength(500);
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM});
		setNoGenerations(1000);
		setNoElites(20);
		setPopulationSize(500);
		setNoRuns(100);
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		String match = "hello_world";
		String src = program.getSourceCode();
		
		if (src == null) {
			return Integer.MAX_VALUE;
		}
		
		int srcLength = src.length();
		int matchLength = match.length();
		int score = 0;
		
		for (int i=0; i<matchLength; i++) {
			if ((i < srcLength) && (match.charAt(i) != src.charAt(i))) {
				score++;
			}
		}
		
		if (srcLength != matchLength) {
			score += Math.abs(matchLength - srcLength);
		}
		
		return score;
	}

	@Override
	public Grammar getGrammar() {
		return grammar;
	}

	public static void main(String[] args) {
		Controller.run(new StringMatch());
	}
}
