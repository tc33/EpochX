package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class DeleteMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	
	private ProgramGenerator programGenerator;
	
	public DeleteMutation(final GXModel model) {
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
		List<Statement> statements = ast.getStatements();
		
		for (int i=0; i<statements.size(); i++) {
			int deletePosition = rng.nextInt(statements.size());
			
			Statement s = statements.get(deletePosition);
			
			if (s instanceof Declaration) {
				continue;
			} else {
				statements.remove(s);
			}
		}

		return program;
	}

}
