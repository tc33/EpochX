package org.epochx.gx.op.mutation;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class ModifyMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	
	private VariableHandler vars;
	
	public ModifyMutation(final GXModel model) {
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
	}
	
	@Override
	public GXCandidateProgram mutate(CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p.clone();
		
		vars.reset();
		
		AST ast = program.getAST();
		ast.modifyExpression(0.2, vars, rng);
		
		return program;
	}

}
