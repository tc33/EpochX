package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class ModifyMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	
	private ProgramGenerator programGenerator;
	
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
		programGenerator = model.getProgramGenerator();
	}
	
	@Override
	public GXCandidateProgram mutate(CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p;
		
		AST ast = program.getAST();
		ast.modifyExpression(0.2);

		return program;
	}

}
