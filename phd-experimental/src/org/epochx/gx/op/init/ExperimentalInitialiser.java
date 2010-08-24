package org.epochx.gx.op.init;

import java.util.*;

import org.apache.commons.lang.*;
import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class ExperimentalInitialiser implements GXInitialiser {

	// The controlling model.
	private GXModel model;
	
	private int popSize;
	
	private RandomNumberGenerator rng;
	
	private VariableHandler vars;
	
	private int minNoStatements;
	private int maxNoStatements;
	
	public ExperimentalInitialiser(GXModel model) {
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
		popSize = model.getPopulationSize();
		rng = model.getRNG();
		vars = model.getVariableHandler();
		minNoStatements = model.getMinNoStatements();
		maxNoStatements = model.getMaxNoStatements();
	}
	
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
		
		for (int i=0; i<popSize; i++) {
			pop.add(initialiseProgram());
			//System.out.println(pop.get(i));
		}
		
		return pop;
	}
	
	public static void printNoCopies(List<CandidateProgram> pop) {
		// How many duplicates?
		int[] copies = new int[pop.size()];
		for (int i=0; i<copies.length; i++) {
			int noMatching = 0;
			CandidateProgram p = pop.get(i);
			for (CandidateProgram q: pop) {
				if (p.equals(q)) {
					noMatching++;
				}
			}
			copies[i] = noMatching;
		}
		System.out.println(ArrayUtils.toString(copies));
	}

	private GXCandidateProgram initialiseProgram() {
		vars.reset();
		int noStatements = rng.nextInt(maxNoStatements-minNoStatements) + minNoStatements;		
		//TODO Need to get the data type here from the model.
		AST ast = ProgramGenerator.getAST(DataType.INT, rng, vars, noStatements);
		//System.out.println(ProgramGenerator.format(ast.toString()));
		Set<Variable> variables = new HashSet<Variable>(vars.getAllVariables());
		//System.out.println("------");
		
		return new GXCandidateProgram(ast, variables, model);
	}
}
