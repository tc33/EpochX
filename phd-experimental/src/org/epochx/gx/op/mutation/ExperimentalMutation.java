package org.epochx.gx.op.mutation;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class ExperimentalMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	private int minNoStatements;
	private int maxNoStatements;
	
	private InsertMutation insert;
	private DeleteMutation delete;
	private ModifyMutation modify;
	
	public ExperimentalMutation(final GXModel model) {
		this.model = model;
		
		insert = new InsertMutation(model);
		delete = new DeleteMutation(model);
		modify = new ModifyMutation(model);
		
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
		minNoStatements = model.getMinNoStatements();
		maxNoStatements = model.getMaxNoStatements();
	}
	
	@Override
	public GXCandidateProgram mutate(final CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p;
		
		double random = rng.nextDouble();
		
		int noStatements = program.getNoStatements();
		
		double[] probabilties = {0.8, 0.1, 0.1};
		if (noStatements <= minNoStatements) {
			// Don't allow delete.
			probabilties[2] += probabilties[1];
			probabilties[1] = 0.0;
		} else if (noStatements >= maxNoStatements) {
			// Don't allow insert.
			probabilties[1] += probabilties[2];
			probabilties[2] = 0.0;
		}
		
		if (random < probabilties[0]) {
			// Insert statement.
			program = modify.mutate(program);
		} else if (random < probabilties[0]+probabilties[1]) {
			// Delete statement.
			program = delete.mutate(program);
		} else {
			// Modify expression.
			program = insert.mutate(program);
		}

		return program;
	}

}
