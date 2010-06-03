package org.epochx.gx.op.init;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;

public class ExperimentalInitialiser implements GXInitialiser {

	// The controlling model.
	private GXModel model;
	
	private int popSize;
	
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
		generator.setRNG(model.getRNG());
		popSize = model.getPopulationSize();
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
		Program ast = generator.getProgram(2);		
		
		return new GXCandidateProgram(ast, model);
	}
	
	public void setParameters(Variable ... parameters) {
		generator.setParameters(parameters);
	}
}
