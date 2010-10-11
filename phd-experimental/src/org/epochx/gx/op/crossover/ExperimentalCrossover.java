package org.epochx.gx.op.crossover;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class ExperimentalCrossover implements GXCrossover {

	// The controlling model.
	private GXModel model;
	
	private RandomNumberGenerator rng;
	private int minNoStatements;
	private int maxNoStatements;
	
	public ExperimentalCrossover(final GXModel model) {
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
		minNoStatements = model.getMinNoStatements();
		maxNoStatements = model.getMaxNoStatements();
	}
	
	@Override
	public GXCandidateProgram[] crossover(CandidateProgram p1,
			CandidateProgram p2) {
		GXCandidateProgram child1 = (GXCandidateProgram) p1;
		GXCandidateProgram child2 = (GXCandidateProgram) p2;
		
		// Select a random statement from the first parent.
		int noStatements = child1.getNoStatements();
		int statementIndex = rng.nextInt(noStatements);
		Statement swap = child1.getMethod().getBody().getStatement(statementIndex);

		// Get any dependencies.
		List<Statement> swapStatements = getDependentStatements(swap, child1);
		
		// Count the number of new statements.
		int noNewStatements = 0;
		for (Statement s: swapStatements) {
			noNewStatements += s.getNoStatements();
		}
		
		// Check there is room for this amount of new statements in program 2.
		if (noNewStatements + child2.getNoStatements() > maxNoStatements) {
			return null;
		}
		
		// Setup variable handler for program 2.
		VariableHandler vars = model.getVariableHandler();
		vars.setAllVariables(child2.getVariables());
		Map<Variable, Variable> variableCopies = new HashMap<Variable, Variable>();
		
		// Clone all the swap statements.
		for (int i=0; i<swapStatements.size(); i++) {
			Statement s = swapStatements.get(i).clone();
			swapStatements.set(i, s);
			
			// Replace the variables with copies.
			s.copyVariables(variableCopies);
		}

		// Rename all the variable copies to ensure they avoid clashes.
		Set<Variable> allVariables = variableCopies.keySet();
		for (Variable v: allVariables) {
			v.setVariableName(vars.getNewVariableName());
		}
		
		int insertPoint = -1;
		int swapDepth = -1;
		int insertPointDepth = -1;
		do {
			// Pick an insertion point at random.
			insertPoint = rng.nextInt(child2.getNoInsertPoints());
	
			// Find the maximum depth of the swap statements.
			swapDepth = 0;
			for (Statement s: swapStatements) {
				int d = s.getDepth();
				if (d > swapDepth) {
					swapDepth = d;
				}
			}
			
			// Find the current depth at the insert point.
			insertPointDepth = child2.getDepthOfInsertPoint(insertPoint);

			// Check the nesting depth will be valid if we insert here.
		} while ((insertPointDepth + swapDepth) > 1);
		
		// Insert the statements into the second parent.
		child2.getMethod().getBody().insertStatements(swapStatements, insertPoint);
		
		// Return an array containing only the second parent.
		return new GXCandidateProgram[]{child2};
	}

	/*
	 * Returns the given statement along with all its dependencies and 
	 * recursively all their dependencies too.
	 */
	private List<Statement> getDependentStatements(Statement s, GXCandidateProgram program) {
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(0, s);
		
		// Are there any non-declared variables referred to in the statement?
		Set<Variable> usedVars = s.getUsedVariables();
		Set<Variable> declVars = s.getDeclaredVariables();
		usedVars.removeAll(declVars);
		
		// Remove any that are parameters.
		
		
		if (!usedVars.isEmpty()) {
			for (Variable v: usedVars) {
				// Get the declaration.
				Declaration decl = program.getMethod().getDeclaration(v);
				
				// In theory decl should only be null if the variable is a parameter.
				if (decl != null) {					
					// Add any dependencies they have at the start.
					statements.addAll(0, getDependentStatements(decl, program));
				}
			}
		}
		
		return statements;
	}
}
