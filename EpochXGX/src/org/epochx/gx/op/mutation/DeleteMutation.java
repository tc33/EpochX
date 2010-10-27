package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class DeleteMutation implements GXMutation {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	
	private int minNoStatements;
	
	public DeleteMutation(final GXModel model) {
		this.model = model;
		
		// Configure parameters from the model.
		Life.get().addConfigListener(new ConfigAdapter() {
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
	}
	
	@Override
	public GXCandidateProgram mutate(CandidateProgram p) {
		GXCandidateProgram program = (GXCandidateProgram) p;
		
		Method method = program.getMethod();
		int noStatements = program.getNoStatements();
		
		Statement deleted = null;
		int i = 0;
		do {
			int deletePosition = rng.nextInt(noStatements);
			Statement s = method.getBody().getStatement(deletePosition);
			
			if ((noStatements - s.getNoStatements()) > minNoStatements) {
				// Deleted may be null if would remove too many statements OR if attempted to delete an ineligible decl.
				deleted = method.deleteStatement(deletePosition);
			}
			
			// Try 3 times at most, then cancel.
			i++;
		} while(deleted == null && i < 3);
		
		return program;
	}

}
