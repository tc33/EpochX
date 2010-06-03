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
	
	private RandomNumberGenerator rng;
	private int popSize;
	private int maxInitialProgramDepth;
	
	private List<String> declaredVariables;
	private int variableIndex;
	
	private ProgramGenerator generator;
	
	public ExperimentalInitialiser(GXModel model) {
		this.model = model;
		
		generator = new ProgramGenerator();
		
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

	private CandidateProgram initialiseProgram() {
		GXCandidateProgram program = new GXCandidateProgram(model);
		
		
		
		return null;
	}

}
