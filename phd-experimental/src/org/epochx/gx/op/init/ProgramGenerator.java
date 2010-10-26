package org.epochx.gx.op.init;

import java.math.*;
import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.gx.representation.*;
import org.epochx.life.Life;
import org.epochx.tools.random.*;

public class ProgramGenerator {
	
	public static final double PROB_INT_GROUP1 = 0.9; // Includes +1, 0, -1
	public static final double PROB_INT_GROUP2 = 0.05; // Includes 2 -> 10
	public static final double PROB_INT_GROUP3 = 0.05; // Includes ints of 1sf
	
	public static final double PROB_DBL_GROUP1 = 0.5; // Includes -1.0 -> +1.0
	public static final double PROB_DBL_GROUP2 = 0.5; // Includes everything else.
	
	//private static final int MAX_NESTED_BLOCKS = 1;
	private static final int MAX_NESTED_LOOPS = 1;
	
	public static Method getMethod(String name, DataType returnType, RandomNumberGenerator rng, VariableHandler vars, int noStatements) {
		Block body = getBlock(rng, vars, 0, noStatements, false);
		ReturnStatement returnStatement = getReturnStatement(returnType, rng, vars);
		
		Method m = new Method(name, body, returnStatement);
		
		return m;
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
	public static Statement getStatement(RandomNumberGenerator rng, VariableHandler vars, int loopNestingLevel, int maxNestedStatements) {
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
			} else if (ran == 2 && maxNestedStatements > 0) {
				// If statement.
				result = getIf(rng, vars, loopNestingLevel, maxNestedStatements);
			} else if (ran == 3 && maxNestedStatements > 0 && loopNestingLevel < MAX_NESTED_LOOPS) {
				// Times loop.
				result = getTimesLoop(rng, vars, loopNestingLevel, maxNestedStatements);
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
	
	public static IfStatement getIf(RandomNumberGenerator rng, VariableHandler vars, int loopNestingLevel, int maxNestedStatements) {
		if (maxNestedStatements == 0) {
			return null;
		}
		
		Expression condition = getExpression(rng, vars, DataType.BOOLEAN, 0);
		Block ifCode = getBlock(rng, vars, loopNestingLevel, maxNestedStatements, true);
		
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
	public static TimesLoop getTimesLoop(RandomNumberGenerator rng, VariableHandler vars, int loopNestingLevel, int maxNestedStatements) {
		if (loopNestingLevel >= 1) {
			return null;
		}
		if (maxNestedStatements == 0) {
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
		Block body = getBlock(rng, vars, loopNestingLevel+1, maxNestedStatements, true);
		
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
		// If nesting level exceeds 5 then don't allow any more binary expressions.
		if (level > 5) {
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
	
	public static Block getBlock(RandomNumberGenerator rng, VariableHandler vars, int nesting, int noStatements, boolean upToNoStatements) {
		// Record number of variables to return to.
		int noVariables = vars.getNoActiveVariables();
		
		List<Statement> statements = new ArrayList<Statement>();

		// If only an up to number, then randomly decide how many.
		if (upToNoStatements && noStatements > 1) {
			// Minimum of 1 statement.
			noStatements = rng.nextInt(noStatements-1)+1;
		}
			
		int currentNo = 0;
		while (currentNo < noStatements) {
			int nextLevelStatements = noStatements - currentNo - 1;
			
			Statement s = getStatement(rng, vars, nesting, nextLevelStatements);
			
			// Don't add a multiple statement that will take us over the size limit.
			int n = s.getNoStatements();
			if (n + currentNo <= noStatements) {
				currentNo += n;
				statements.add(s);
			}
		}
		
		Block result = new Block(statements);
		
		// Pop off any newly declared variables to return to size before block.
		vars.setNoActiveVariables(noVariables);
		
		return result;
	}
	
	public static Block getBlock(RandomNumberGenerator rng, VariableHandler vars, int loopNestingLevel) {
		return getBlock(rng, vars, loopNestingLevel, 1, true);
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
		double choice = rng.nextDouble();
		
		int value;
		if (choice < PROB_INT_GROUP1) {
			// -1, 0 or +1
			value = rng.nextInt(2)-1;
		} else if (choice < PROB_INT_GROUP2) {
			// 2-10
			value = rng.nextInt(8)+2;
		} else {
			// 1 s.f.
			value = rng.nextInt(9)+1;
			// How many 0s? (up to 8)
			int exp = rng.nextInt(7)+1;
			// value * 10^exp
			value = value * new Double(Math.pow(10, exp)).intValue();
		}
		
		return new IntLiteral(value);
	}
	
	public static DoubleLiteral getDoubleLiteral(RandomNumberGenerator rng) {
		double choice = rng.nextDouble();
		
		double value = rng.nextDouble();
		if (choice < (PROB_DBL_GROUP1/2)) {
			// -1.0 -> 0.0.
			value = -value;
		}
		
		// Round to 3 d.p.
		BigDecimal bd = new BigDecimal(choice);
	    bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
	    value = bd.doubleValue();
		
		return new DoubleLiteral(value);
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
		Life.get().fireConfigureEvent();
		VariableHandler vars = model.getVariableHandler();
		Method program = getMethod("getFibonacci", DataType.BOOLEAN, model.getRNG(), vars, 10);
		
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
