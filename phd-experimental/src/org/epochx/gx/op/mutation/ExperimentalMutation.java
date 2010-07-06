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
	}
	
	@Override
	public GXCandidateProgram mutate(final CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p;
		
		double random = rng.nextDouble();
		
		if (random < 0.2) {
			// Insert statement.
			program = insert.mutate(program);
		} else if (random < 0.4) {
			// Delete statement.
			program = delete.mutate(program);
		} else {
			// Modify expression.
			program = modify.mutate(program);
		}
		
		return program;
	}

}
