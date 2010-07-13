package org.epochx.gx.op.init;

import java.util.*;

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
	
	private int noStatements;
	
	public ExperimentalInitialiser(GXModel model) {
		this(model, 3);
	}
	
	public ExperimentalInitialiser(GXModel model, int noStatements) {
		this.model = model;
		this.noStatements = noStatements;
		
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

	private GXCandidateProgram initialiseProgram() {
		vars.reset();
		//TODO Need to get the data type here from the model.
		AST ast = ProgramGenerator.getAST(DataType.BOOLEAN, rng, vars, noStatements);
		Set<Variable> variables = new HashSet<Variable>(vars.getAllVariables());
		
		return new GXCandidateProgram(ast, variables, model);
	}
}
