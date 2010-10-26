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
	
	public List<Statement> getSwapStatements(GXCandidateProgram child) {
		// Select a random statement (and dependencies) from the first parent.
		int noStatements = child.getNoStatements();
		int statementIndex = rng.nextInt(noStatements);
		Statement swap = child.getMethod().getBody().getStatement(statementIndex);
		List<Statement> swapStatements = getDependentStatements(swap, child);
		
		return swapStatements;
	}
	
	public int getNoStatements(List<Statement> statements) {
		int noStatements = 0;
		for (Statement s: statements) {
			noStatements += s.getNoStatements();
		}
		
		return noStatements;
	}
	
	public void copyStatements(List<Statement> statements, GXCandidateProgram program) {
		// Setup variable handler for program 2.
		VariableHandler vars = model.getVariableHandler();
		vars.setAllVariables(new HashSet<Variable>(program.getVariables()));
		Map<Variable, Variable> variableCopies = new HashMap<Variable, Variable>();
		
		// Clone all the swap statements.
		for (int i=0; i<statements.size(); i++) {
			Statement s = statements.get(i).clone();
			statements.set(i, s);
			
			// Replace the variables with copies.
			s.copyVariables(vars, variableCopies);
		}

		// Rename all the variable copies to ensure they avoid clashes.
		Collection<Variable> allVariables = variableCopies.values();
		for (Variable v: allVariables) {
			v.setVariableName(vars.getNewVariableName());
			vars.add(v);
		}
	}
	
	public void insertStatements(List<Statement> statements, GXCandidateProgram program) {
		int insertPoint = -1;
		int swapDepth = -1;
		int insertPointDepth = -1;
		
		// Find the maximum loop depth of the swap statements.
		swapDepth = 0;
		for (Statement s: statements) {
			int d = s.getLoopDepth();
			if (d > swapDepth) {
				swapDepth = d;
			}
		}
		
		List<Integer> insertPoints = new ArrayList<Integer>();
		int noInsertPoints = program.getNoInsertPoints();
		for (int i=0; i<noInsertPoints; i++) {
			insertPoints.add(i);
		}
		
		do {
			// Pick an insertion point at random.
			insertPoint = insertPoints.get(rng.nextInt(insertPoints.size()));
			
			// Find the current loop depth at the insert point.
			insertPointDepth = program.getLoopDepthOfInsertPoint(insertPoint);

			if (insertPoints.isEmpty()) {
				System.out.println("Insert points run out - this shouldn't ever happen!");
			}
			
			// Check the nesting depth will be valid if we insert here.
		} while ((insertPointDepth + swapDepth) > 1 && !insertPoints.isEmpty());
		
		// Insert the statements into the program.
		program.getMethod().getBody().insertStatements(statements, insertPoint);
		
		// Any new variables need to be added to the program's list of variables.
		List<Variable> newVars = new ArrayList<Variable>();
		for (Statement s: statements) {
			newVars.addAll(s.getDeclaredVariables());
		}
		program.getVariables().addAll(newVars);
	}
	
	public void deleteStatements(int noDeletions, GXCandidateProgram program) {
		int i=0;
		int attempts = 0;
		int noStatements = program.getNoStatements();
		while (i < noDeletions && attempts < 5 && noStatements > minNoStatements) {
			int toDelete = rng.nextInt(noStatements);
			Statement s = program.getMethod().getBody().getStatement(toDelete);
			int noDeleted = s.getNoStatements();
			
			if ((noStatements - noDeleted) >= minNoStatements) {
				// Will return null if the statement is a declaration which is used.
				Statement deleted = program.getMethod().getBody().deleteStatement(toDelete);
				
				if (deleted != null) {
					attempts = 0;
					i += noDeleted;
					noStatements -= noDeleted;
					
					// Remove any variables that were eclaration here.
					Set<Variable> declaredVars = deleted.getDeclaredVariables();
					for (Variable v: declaredVars) {
						program.getVariables().remove(v);
					}
					continue;
				}
			}
			
			attempts++;
		}
	}

	public int getNoDeletable(List<Statement> statements) {
		int noDeletable = 0;
		for (Statement s: statements) {
			if (!(s instanceof Declaration)) {
				noDeletable += s.getNoStatements();
			}
		}
		
		return noDeletable;
	}
	
	@Override
	public GXCandidateProgram[] crossover(CandidateProgram p1,
			CandidateProgram p2) {
		GXCandidateProgram child1 = (GXCandidateProgram) p1;
		GXCandidateProgram child2 = (GXCandidateProgram) p2;
		
		// Pick the statements to be swapped between programs.
		List<Statement> swapStatements1 = getSwapStatements(child1);
		List<Statement> swapStatements2 = getSwapStatements(child2);
		
		// Count the number of new statements.
		int noNewStatements1 = getNoStatements(swapStatements1);
		int noNewStatements2 = getNoStatements(swapStatements2);

		// Keep a copy of the non-copied swaps so we can remove them later.
		//List<Statement> originalSwaps1 = new ArrayList<Statement>(swapStatements1);
		//List<Statement> originalSwaps2 = new ArrayList<Statement>(swapStatements2);
		
		// Create a copy of each of the statements and rename variables.
		copyStatements(swapStatements1, child2);
		copyStatements(swapStatements2, child1);
		
		// Delete the right number of statements.
		deleteStatements(noNewStatements2, child1);
		deleteStatements(noNewStatements1, child2);
		
		List<GXCandidateProgram> children = new ArrayList<GXCandidateProgram>();
		// Insert the new statements.
		if ((child1.getNoStatements() + noNewStatements2) <= maxNoStatements) {
			insertStatements(swapStatements2, child1);
			children.add(child1);
		}
		if ((child2.getNoStatements() + noNewStatements1) <= maxNoStatements) {
			insertStatements(swapStatements1, child2);
			children.add(child2);
		}
		
		// Return an array containing only the second parent.
		return children.toArray(new GXCandidateProgram[children.size()]);
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
