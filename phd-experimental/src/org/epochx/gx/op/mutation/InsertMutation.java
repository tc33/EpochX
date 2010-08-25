package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class InsertMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	
	private VariableHandler vars;
	
	private int maxNoStatements;
	
	public InsertMutation(final GXModel model) {
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
		vars = model.getVariableHandler();
		maxNoStatements = model.getMaxNoStatements();
	}
	
	@Override
	public GXCandidateProgram mutate(CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p;
		
		// Reset the program state.
		vars.reset();
		vars.setAllVariables(new HashSet<Variable>(program.getVariables()));
		
		// Consider each insert point for insertion.
		Method method = program.getMethod();
		method.insertStatement(0.1, vars, rng, maxNoStatements);
		
		// Update the set of variables stored in the program.
		program.setVariables(new HashSet<Variable>(vars.getAllVariables()));
		
		return program;
	}

}
