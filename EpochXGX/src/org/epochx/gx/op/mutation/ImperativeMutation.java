package org.epochx.gx.op.mutation;

import java.util.*;

import org.epochx.epox.*;
import org.epochx.gp.op.mutation.*;
import org.epochx.gp.representation.*;
import org.epochx.gx.model.*;
import org.epochx.gx.node.*;
import org.epochx.gx.node.Variable;
import org.epochx.gx.op.init.*;
import org.epochx.life.*;
import org.epochx.representation.*;
import org.epochx.tools.eval.*;
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
		
		if (random < probModifyExpression) {
			// Modify expression.
			modifyExpressions(subroutine, 0);
		} else if (random < probModifyExpression+deleteProb) {
			// Delete statement.
			deleteStatement(subroutine);
		} else {
			// Insert statement.
			insertStatement(subroutine);
		}
		
		return program;
	}

	private void modifyExpressions(ASTNode root, int exprDepth) {
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
		}
	}
	
	private void deleteStatement(Subroutine subroutine) {
		int noStatements = subroutine.getTotalNoStatements();
		
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
						removeChild(root, i);
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
	
	private void removeChild(ASTNode node, int index) {
		Node[] children = node.getChildren();
		int noChildren = children.length-1;
		
		Node[] newChildren = new Node[noChildren];
		
		for (int i=0; i<noChildren; i++) {
			if (i < index) {
				newChildren[i] = children[i];
			} else {
				newChildren[i] = children[i+1];
			}
		}
		
		node.setChildren(newChildren);
	}
	
	private void insertStatement(Subroutine subroutine) {
		// How many insert points?
		int noInsertPoints = 0;
		
		// Randomly choose an insertion location.
		int insertPoint = rng.nextInt(noInsertPoints);
		
		// Ensure the initialiser has its active variables initialised to that point.
		init.setActiveVariables(subroutine, insertPoint);
		
		// Generate a replacement statement.
		Statement newStatement = init.getStatement(maxNestedStatements);
		
		// Insert the statement at the right location.
		insertStatement(subroutine, newStatement, insertPoint);
	}
	
	private void insertStatement(ASTNode program, Statement s, int insertPoint) {
		
	}
}
