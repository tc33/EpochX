package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class InsertMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	
	private VariableHandler vars;
	
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
	}
	
	@Override
	public GXCandidateProgram mutate(CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p;
		
		AST ast = program.getAST();
		List<Statement> statements = ast.getStatements();
		
		int insertPosition = rng.nextInt(statements.size() + 1);
		vars.reset();
		vars.setAllVariables(new HashSet<Variable>(program.getVariables()));
		
		for (int i=0; i<insertPosition; i++) {
			statements.get(i).apply(vars);
		}
		
		ast.insertStatement(insertPosition, ProgramGenerator.getStatement(rng, vars, 0));

		program.setVariables(new HashSet<Variable>(vars.getAllVariables()));
		
		return program;
	}

}
