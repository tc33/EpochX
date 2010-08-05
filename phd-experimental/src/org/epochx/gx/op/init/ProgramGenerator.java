package org.epochx.gx.op.init;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.tools.random.*;

public class ProgramGenerator {
	
	public static AST getAST(DataType returnType, RandomNumberGenerator rng, VariableHandler vars, int noStatements) {
		AST p = new AST();
		
		for (int i=0; i<noStatements; i++) {
			p.addStatement(getStatement(rng, vars, 0));
		}
		
		p.addReturnStatement(getReturnStatement(returnType, rng, vars));
		
		return p;
	}
	
	public static ReturnStatement getReturnStatement(DataType type, RandomNumberGenerator rng, VariableHandler vars) {
		Variable variable = vars.getActiveVariable(type);
		
		ReturnStatement returnStatement = new ReturnStatement(type, variable);
		
		return returnStatement;
	}
	
	/**
	 * Randomly chooses a statement type to construct. If there are no possible
	 * valid statements of that type then a different statement type will be 
	 * selected until all have been tried. If none are valid then null is 
	 * returned, otherwise the constructed statement is returned.
	 */
	public static Statement getStatement(RandomNumberGenerator rng, VariableHandler vars, int nesting) {
		Statement result = null;
		
		int noOptions = 4;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));
	
			if (ran == 0) {
				// Declaration.
				result = getDeclaration(rng, vars);
			} else if (ran == 1) {
				// Assignment.
				result = getAssignment(rng, vars);
			} else if (ran == 2) {
				// If statement.
				result = getIf(rng, vars, nesting);
			} else if (ran == 3) {
				// Times loop.
				result = getTimesLoop(rng, vars, nesting);
			}/* else {
				// Loop.
				result = getWhileLoop(rng, vars);
			}*/
		}
		
		return result;
	}
	
	public static Declaration getDeclaration(RandomNumberGenerator rng, VariableHandler vars, Expression expression) {
		String varName = vars.getNewVariableName();
		
		DataType type = expression.getDataType();
		Variable variable = new Variable(type, varName);

		vars.add(variable);
		
		Declaration decl = new Declaration(type, variable, expression);
		
		return decl;
	}
	
	public static Declaration getDeclaration(RandomNumberGenerator rng, VariableHandler vars, DataType type) {
		Expression expression = getExpression(rng, vars, type, 0);

		return getDeclaration(rng, vars, expression);
	}
	
	public static Declaration getDeclaration(RandomNumberGenerator rng, VariableHandler vars) {
		DataType type = getDataType(rng);
		
		return getDeclaration(rng, vars, type);
	}
	
	public static Assignment getAssignment(RandomNumberGenerator rng, VariableHandler vars) {
		Assignment result = null;
		Variable variable = vars.getActiveVariable();

		if (variable != null) {
			Expression expression = getExpression(rng, vars, variable.getDataType(), 0);
			result = new Assignment(variable, expression);
		}
		
		return result;
	}
	
	public static IfStatement getIf(RandomNumberGenerator rng, VariableHandler vars, int nesting) {
		Expression condition = getExpression(rng, vars, DataType.BOOLEAN, 0);
		Block ifCode = getBlock(rng, vars, nesting+1);
		
		IfStatement ifStatement = new IfStatement(condition, ifCode);
		
		return ifStatement;
	}
	
	/*public static Loop getWhileLoop(RandomNumberGenerator rng, VariableHandler vars) {
		Expression condition = getExpression(rng, vars, DataType.BOOLEAN, 0);
		Block body = getBlock(rng, vars);
		
		Loop loop = new Loop(condition, body);
		
		return null;
	}*/
	
	/**
	 * 
	 */
	public static TimesLoop getTimesLoop(RandomNumberGenerator rng, VariableHandler vars, int nesting) {
		if (nesting >= 2) {
			return null;
		}
		
		// End point declaration.
		//Expression iterations = getExpression(rng, vars, DataType.INT, 0);
		Declaration endVar = getDeclaration(rng, vars, DataType.INT);
		
		// End variable should not be used so hide from active.
		vars.removeActiveVariable(endVar.getVariable());
		
		// Index variable.
		Declaration indexVar = getDeclaration(rng, vars, new IntLiteral(0));
		
		// Index variable should not be used directly so hide from active.
		vars.removeActiveVariable(indexVar.getVariable());
		
		// Index cover variable.
		Declaration indexCoverVar = getDeclaration(rng, vars, indexVar.getVariable());
		
		// Generate a block.
		Block body = getBlock(rng, vars, nesting+1);
		
		// Remove the index cover variable, to ensure scope restricted to block.
		vars.removeActiveVariable(indexCoverVar.getVariable());
		
		TimesLoop loop = new TimesLoop(body, indexVar, indexCoverVar, endVar);
		
		return loop;
	}
	
	public static Expression getExpression(RandomNumberGenerator rng, VariableHandler vars, DataType type, int level) {
		//System.out.println("level: " + level);
		Expression result = null;
		
		int noOptions = 4;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		//TODO This needs to be formalised. And why does it happen anyway.
		// If nesting level exceeds 10 then don't allow any more binary expressions.
		if (level > 10) {
			indexes.remove(2);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));

			if (ran == 0) {
				// Literal value.
				result = getLiteral(rng, type);
			} else if (ran == 1) {
				// Variable.
				result = vars.getActiveVariable(type);
			} else if (ran == 2) {
				// Binary expression.
				result = getBinaryExpression(rng, vars, type, level+1);
			} else {
				// Unary expression.
				result = getUnaryExpression(rng, vars, type);
			}
		}
		
		return result;
	}
	
	public static BinaryExpression getBinaryExpression(RandomNumberGenerator rng, VariableHandler vars, DataType dataType, int level) {
		Operator op = Operator.getBinaryOperator(rng, dataType);
		
		Expression leftExpression = getExpression(rng, vars, dataType, level);
		Expression rightExpression = getExpression(rng, vars, dataType, level);
		
		return new BinaryExpression(op, leftExpression, rightExpression, dataType);
	}
	
	public static UnaryExpression getUnaryExpression(RandomNumberGenerator rng, VariableHandler vars, DataType dataType) {
		Operator op = Operator.getUnaryOperator(rng, dataType);
		UnaryExpression result = null;
		
		if (op != null) {
			//TODO The ! operator should not be restricted to operate on a variable.
			Variable var = vars.getActiveVariable(dataType);
			
			if (var != null) {
				result = new UnaryExpression(op, var, dataType);
			}
		}

		return result;
	}
	
	public static Block getBlock(RandomNumberGenerator rng, VariableHandler vars, int nesting) {
		// Record number of variables to return to.
		int noVariables = vars.getNoActiveVariables();
		
		List<Statement> statements = new ArrayList<Statement>();

		//int noStatements = rng.nextInt(10);
		int noStatements = 1;
		
		for (int i=0; i<noStatements; i++) {
			statements.add(getStatement(rng, vars, nesting));
		}
		
		Block result = new Block(statements);
		
		// Pop off any newly declared variables to return to size before block.
		vars.setNoActiveVariables(noVariables);
		
		return result;
	}

	public static DataType getDataType(RandomNumberGenerator rng) {
		DataType[] types = DataType.values();
		
		return types[rng.nextInt(types.length)];
	}
	
	public static Literal getLiteral(RandomNumberGenerator rng, DataType datatype) {
		Literal result = null;
		
		if (datatype == DataType.BOOLEAN) {
			result = getBooleanLiteral(rng);
		} else if (datatype == DataType.INT) {
			result = getIntLiteral(rng);
		} else if (datatype == DataType.DOUBLE) {
			result = getDoubleLiteral(rng);
		} else {
			// broken.
		}
		
		return result;
	}
	
	public static BooleanLiteral getBooleanLiteral(RandomNumberGenerator rng) {
		BooleanLiteral literal = new BooleanLiteral(rng.nextBoolean());
		
		return literal;
	}

	public static IntLiteral getIntLiteral(RandomNumberGenerator rng) {
		IntLiteral literal = new IntLiteral(rng.nextInt());
		
		return literal;
	}
	
	public static DoubleLiteral getDoubleLiteral(RandomNumberGenerator rng) {
		DoubleLiteral literal = new DoubleLiteral(rng.nextDouble());
		
		return literal;
	}
	
	public static String format(CharSequence s) {
		int indentLevel = 0;
		StringBuilder formatted = new StringBuilder();
		
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			formatted.append(c);
			if (c == '{') {
				indentLevel++;
			} else if (c == '\n') {
				// If there's space then look ahead to test next char.
				if (i+1 < s.length()) {
					c = s.charAt(i+1);
					if (c == '}') {
						indentLevel--;
					}
				}
				formatted.append(indent(indentLevel));
			}
		}
		
		return formatted.toString();
	}
	
	private static String indent(int indentLevel) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i<indentLevel; i++) {
			buffer.append("    ");
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		GXModel model = new EvenParity(3);
		model.getLifeCycleManager().fireConfigureEvent();
		VariableHandler vars = model.getVariableHandler();
		AST program = getAST(DataType.BOOLEAN, model.getRNG(), vars, 10);
		
		vars.reset();
		System.out.println(format(program.toString()));
		System.out.println("-------------------------------------");
		
		// Set the values for our arguments.
		String[] argNames = {"d0", "d1", "d2"};
		Boolean[] argValues = {false, true, false};
    	for (int i=0; i<argNames.length; i++) {
    		vars.setParameterValue(argNames[i], argValues[i]);
    	}
		
		System.out.println("Result: " + program.evaluate(vars));
	}
}
