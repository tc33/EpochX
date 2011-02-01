package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.epox.*;
import org.epochx.gp.op.mutation.*;
import org.epochx.gp.representation.*;
import org.epochx.gx.model.*;
import org.epochx.gx.node.*;
import org.epochx.gx.node.Variable;
import org.epochx.gx.op.init.*;
import org.epochx.gx.util.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;

public class ImperativeMutation implements GPMutation {

	// The controlling model.
	private GXModel model;
	
	private ImperativeInitialiser init;
	
	private RandomNumberGenerator rng;
	private int minNoStatements;
	private int maxNoStatements;
	
	private int maxExpressionDepth;
	
	// The probabilities for each type of mutation - should add to 1.0.
	private double probDeleteStatement;
	private double probInsertStatement;
	private double probModifyExpression;
	
	// If doing modify mutation, this is probability each expression is modified.
	private double probModifyEachExpression;
	
	public ImperativeMutation(final GXModel model, 
			double probDeleteStatement, 
			double probInsertStatement, 
			double probModifyExpression,
			double probModifyEachExpression) {
		this.model = model;
		this.probDeleteStatement = probDeleteStatement;
		this.probInsertStatement = probInsertStatement;
		this.probModifyExpression = probModifyExpression;
		this.probModifyEachExpression = probModifyEachExpression;
		
		// An initialiser to construct new expressions.
		init = new ImperativeInitialiser(model);
		
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
		maxNoStatements = model.getMaxNoStatements();
		maxExpressionDepth = model.getMaxExpressionDepth();
	}
	
	@Override
	public GPCandidateProgram mutate(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		Subroutine subroutine = (Subroutine) program.getRootNode();
		
		int noStatements = subroutine.getTotalNoStatements();
		
		double insertProb = probInsertStatement;
		double deleteProb = probDeleteStatement;
		
		double random = rng.nextDouble();
		if (noStatements <= minNoStatements) {
			// Don't allow delete. Increase probability of insert.
			insertProb += deleteProb;
			deleteProb = 0.0;
		} else if (noStatements >= maxNoStatements) {
			// Don't allow delete. Increase probability of delete.
			deleteProb += insertProb;
			insertProb = 0.0;
		}
		
		// Ensure the initialiser's active variables are reset.
		init.resetVariables();
		
		if (random < probModifyExpression) {
			// Modify expression.
			modifyExpressions(subroutine, 0);
		} else if (random < probModifyExpression+deleteProb) {
			// Delete statement.
			deleteStatement(subroutine, noStatements);
		} else {
			// Insert statement.
			insertStatement(subroutine, noStatements);
		}
		
		return program;
	}

	private void modifyExpressions(ASTNode root, int exprDepth) {
		Set<Variable> varsBlockStart = null;
		if (root instanceof Block) {
			varsBlockStart = new HashSet<Variable>(init.getActiveVariables());
		}
		
		// We assume that any expression can be changed for any other expression of the same return type.
		int arity = root.getArity();
		for (int i=0; i<arity; i++) {
			Class<? extends ASTNode>[] childTypes = root.getChildTypes();
			
			ASTNode child = (ASTNode) root.getChild(i);
			
			// Only consider modifying if child must be any expression.
			if (childTypes[i] == Expression.class) {
				// Decide whether to replace it.
				double rand = rng.nextDouble();
				
				if (rand < probModifyEachExpression) {
					// Existing expression's type.
					Class<?> datatype = child.getReturnType();
					
					// Construct a new one.
					Expression newExpr = init.getExpression(datatype, (maxExpressionDepth-exprDepth));
					
					// Replace the old one.
					root.setChild(i, newExpr);
					
					// No point modifying the new sub-expressions.
					continue;
				} else {
					// Consider modification of any expressions below expression child.
					modifyExpressions(child, exprDepth+1);
				}
			} else {
				// Consider modification of any expressions below non-expression child.
				modifyExpressions(child, exprDepth);
			}
			
			// If it was a declaration, then add its variable to active.
			if (child instanceof Declaration) {
				Declaration decl = (Declaration) child;
				init.addActiveVariable(decl.getVariable());
			}
		}
		
		// If this is the end of a block, then remove any new variables.
		if (root instanceof Block) {
			init.setActiveVariables(varsBlockStart);
		}
	}
	
	private void deleteStatement(Subroutine subroutine, int noStatements) {
		//int noStatements = subroutine.getTotalNoStatements();
		
		// Create list of possible statement numbers to choose.
		List<Integer> indexes = new ArrayList<Integer>(noStatements);
		for (int i=0; i<noStatements; i++) {
			indexes.add(i);
		}
		
		Statement deleted = null;
		int i = 0;
		do {
			int deletePosition = indexes.remove(rng.nextInt(indexes.size()));
			Statement s = subroutine.getNthStatement(deletePosition);
			
			if ((noStatements - s.getTotalNoStatements()) >= minNoStatements) {
				// Deleted may be null if would remove too many statements OR if attempted to delete an ineligible decl.
				deleted = removeStatement(subroutine, deletePosition);
			}
			
			// Keep trying until there are no more options.
			i++;
		} while(deleted == null && !indexes.isEmpty());
	}
	
	private Statement removeStatement(ASTNode root, int statementIndex) {
		int current = 0;
		int arity = root.getArity();
		
		for (int i=0; i<arity; i++) {
			ASTNode child = (ASTNode) root.getChild(i);
			
			if (child instanceof Statement) {
				Statement s = (Statement) child;
				Block block = null;
				if (root instanceof Block) {
					block = (Block) root;
				} else {
					// Statements should only be found within blocks.
					assert false;
				}
				
				if (statementIndex == current) {
					// Is the statement valid for deletion?
					if (isDeletable(block, s)) {
						Utils.removeChild(root, i);
						return s;
					} else {
						return null;
					}
				}
			}
			
			int noStatements = child.getTotalNoStatements();
			if (statementIndex < (current + noStatements)) {
				// Is within this statement.
				return removeStatement(child, statementIndex-current);
			} else {
				current += noStatements;
			}
		}
		
		return null;
	}
	
	/*
	 * Statements are deletable unless they are a return statement, or if they
	 * are a declaration for a variable that is used within later statements.
	 */
	private boolean isDeletable(Block parentBlock, Statement s) {
		boolean deletable = true;
		
		if (s instanceof ReturnStatement) {
			deletable = false;
		} else if (s instanceof Declaration) {
			Declaration d = (Declaration) s;
			Variable v = d.getVariable();
			
			if (countVariableUses(v, parentBlock) > 1) {
				deletable = false;
			}
		}
		
		return deletable;
	}
	
	private int countVariableUses(Variable var, ASTNode root) {
		int count = 0;
		if (root == var) {
			count++;
		} else {
			int arity = root.getArity();
			for (int i=0; i<arity; i++) {
				count += countVariableUses(var, (ASTNode) root.getChild(i));
			}
		}
		
		return count;
	}
	
	private void insertStatement(Subroutine subroutine, int noStatements) {
		// How many insert points?
		int noInsertPoints = getNoInsertPoints(subroutine);
		
		// Randomly choose an insertion location.
		int insertPoint = rng.nextInt(noInsertPoints);
		
		// Ensure the initialiser has its active variables initialised to that point.
		setupActiveVariables(subroutine, insertPoint, 0);
		
		//TODO We really shouldn't be having to do this again, just pass it through.
		//int noStatements = subroutine.getTotalNoStatements();
		
		// Determine the number of statements the new statement may contain.
		int statementSpaces = maxNoStatements-noStatements;
		
		// Generate a replacement statement.
		Statement newStatement = init.getStatement(statementSpaces-1);
		
		// Insert the statement at the right location.
		insertStatement(subroutine, newStatement, insertPoint, 0);
	}
	
	private int setupActiveVariables(ASTNode root, int insertPoint, int currentPoint) {
		Set<Variable> varsBlockStart = null;
		if (root instanceof Block) {
			varsBlockStart = new HashSet<Variable>(init.getActiveVariables());
		} else if (root instanceof Declaration) {
			init.addActiveVariable(((Declaration) root).getVariable());
		}
		
		int arity = root.getArity();
		for (int i=0; i<arity+1; i++) {
			if (root instanceof Block) {
				if (currentPoint == insertPoint) {
					// Found the point, terminate.
					return currentPoint;
				}
				currentPoint++;
			}
			
			// All except last insert point, are child indexes.
			if (i < arity) {
				ASTNode child = (ASTNode) root.getChild(i);
				currentPoint = setupActiveVariables(child, insertPoint, currentPoint);
			}
		}
		
		if (root instanceof Block) {
			init.setActiveVariables(varsBlockStart);
		}
		
		return currentPoint;
	}
	
	/*
	 * An insert point by definition, is a position between two statements in a
	 * block, or a position at the beginning or end of a block. This method 
	 * returns a sum of those positions.
	 */
	private int getNoInsertPoints(ASTNode root) {
		int noInsertPoints = 0;
		
		int arity = root.getArity();
		
		if (root instanceof Block) {
			// Add the number of insert points within this block.
			noInsertPoints += (arity + 1);
		}
		
		// Add the insert points below each of this node's children.
		for (int i=0; i<arity; i++) {
			ASTNode child = (ASTNode) root.getChild(i);
			noInsertPoints += getNoInsertPoints(child);
		}
		
		return noInsertPoints;
	}
	
	private void insertStatement(ASTNode root, Statement s, int insertPoint, int currentPoint) {
		int arity = root.getArity();
		
		// Include the position beyond the arity at end of block.
		for (int i=0; i<arity+1; i++) {
			if (root instanceof Block) {
				if (currentPoint == insertPoint) {
					// Insert here at ith child.
					Utils.insertChild(root, s, i);
				}
				currentPoint++;
			}
			
			// We don't want to include the empty position at the end of the block.
			if (i < arity) {
				ASTNode child = (ASTNode) root.getChild(i);
				int noChildInsertPoints = getNoInsertPoints(child);
				if (insertPoint < (noChildInsertPoints + currentPoint)) {
					insertStatement(child, s, insertPoint, currentPoint);
					
					// It will have been inserted by here, so lets end.
					break;
				} else {
					currentPoint += noChildInsertPoints;
				}
			}
		}
	}
}
