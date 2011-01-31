package org.epochx.gx.op.init;

import java.util.*;

import org.epochx.epox.*;
import org.epochx.gp.representation.*;
import org.epochx.gx.model.*;
import org.epochx.gx.node.*;
import org.epochx.gx.node.Literal;
import org.epochx.gx.node.Variable;
import org.epochx.life.*;
import org.epochx.op.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;
import org.epochx.tools.util.*;

public class ImperativeInitialiser implements Initialiser {

	private GXModel model;
	
	private RandomNumberGenerator rng;
	private List<Variable> parameters;
	
	private Class<?> returnType;
	private int minNoStatements;
	private int maxNoStatements;
	private int minBlockStatements;
	private int maxLoopDepth;
	private int maxExpressionDepth;
	
	private int popSize;

	private boolean acceptDuplicates;
	
	// Literal nodes that can be used.
	private Set<Literal> literals;
	
	// Variables that are currently in scope.
	private Set<Variable> activeVariables;
	
	// All variables, regardless of scope.
	private Set<Variable> allVariables;
	
	public ImperativeInitialiser(final GXModel model) {
		this(model, true);
	}
	
	public ImperativeInitialiser(final GXModel model, boolean acceptDuplicates) {
		this.model = model;
		this.acceptDuplicates = acceptDuplicates;
		
		literals = new HashSet<Literal>();
		activeVariables = new HashSet<Variable>();
		allVariables = new HashSet<Variable>();
		
		//TODO Would be better getting this as a setting from the model.
		minBlockStatements = 1;
		
		// Configure parameters from the model.
		Life.get().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	private void configure() {
		popSize = model.getPopulationSize();
		rng = model.getRNG();
		minNoStatements = model.getMinNoStatements();
		maxNoStatements = model.getMaxNoStatements();
		maxLoopDepth = model.getMaxLoopDepth();
		maxExpressionDepth = model.getMaxExpressionDepth();
		returnType = model.getReturnType();
		parameters = model.getParameters();
		literals = model.getLiterals();		
		
		resetVariables();
	}
	
	private void resetVariables() {
		allVariables.clear();
		activeVariables.clear();
		
		activeVariables = new HashSet<Variable>(parameters);
		allVariables = new HashSet<Variable>(parameters);
	}
	
	public void addLiteralNode(Literal literalNode) {
		literals.add(literalNode);
	}
	
	public void removeLiteralNode(Literal literalNode) {
		literals.remove(literalNode);
	}
	
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		if (popSize < 1) {
			throw new IllegalStateException("Population size must be 1 or greater");
		}
		
		// Create population list to be populated.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);

		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GPCandidateProgram candidate;

			do {
				// Grow a new node tree.
				candidate = getInitialProgram();
			} while (!acceptDuplicates && firstGen.contains(candidate));

			// Must be unique - add to the new population.
			firstGen.add(candidate);
		}

		return firstGen;
	}

	public GPCandidateProgram getInitialProgram() {
		resetVariables();
		
		final Node root = getSubroutine();

		return new GPCandidateProgram(root, model);
	}

	public Subroutine getSubroutine() {			
		if (minNoStatements < 0) {
			throw new IllegalStateException("Minimum number of statements must be 0 or greater");
		} else if (maxNoStatements < minNoStatements) {
			throw new IllegalStateException("Maximum number of statements must be at least as great as the minimum number");
		}
		
		return new Subroutine(getBlock(minNoStatements, maxNoStatements), getReturnStatement());
	}
	
	public ReturnStatement getReturnStatement() {
		Expression expr = getExpression(returnType, maxExpressionDepth);
		
		if (expr == null) {
			throw new IllegalStateException("Unable to generate program with specified return type");
		}
		
		return new ReturnStatement(expr);
	}

	public Block getBlock(int minNestedStatements, int maxNestedStatements) {
		if (maxNestedStatements < minNestedStatements) {
			return null;
		}
		
		// Record the state of in-scope variables.
		Set<Variable> activeVariablesBefore = new HashSet<Variable>(activeVariables);
		
		// Decide how many statements.
		int noStatements = rng.nextInt(maxNestedStatements-minNestedStatements) + minNestedStatements;
		
		List<Statement> statements = new ArrayList<Statement>();
		
		// The total number of statements including those nested within statements.
		int noStatementsRemaining = noStatements;
		
		// We assume statements of size 1 are always possible.
		while (noStatementsRemaining > 0) {
			// Construct a new statement, up to the remaining size
			Statement statement = getStatement(noStatementsRemaining - 1);
			
			statements.add(statement);
			
			// Reduce the remaining size.
			noStatementsRemaining -= statement.getTotalNoStatements();
		}
		
		// Convert to an array, to use as children.
		Statement[] blockChildren = statements.toArray(new Statement[statements.size()]);
		
		// Return the set of in-scope variables to remove any new.
		activeVariables = activeVariablesBefore;
		
		return new Block(blockChildren);
	}
	
	public Statement getStatement(int maxNestedStatements) {
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
				result = getDeclaration();
			} else if (ran == 1) {
				// Assignment.
				result = getAssignment();
			} else if (ran == 2) {
				// If statement.
				result = getIf(maxNestedStatements);
			} else if (ran == 3) {
				// Times loop.
				result = getForLoop(maxNestedStatements);
			}
		}
		
		return result;
	}

	public ForLoop getForLoop(int maxNestedStatements) {
		ForLoop loop = null;
		
		if (maxNestedStatements > minBlockStatements) {
			Expression upperBoundExpr = getExpression(Integer.class, maxExpressionDepth);
		
			Block body = getBlock(minBlockStatements, maxNestedStatements);
			
			loop = new ForLoop(upperBoundExpr, body);
		}
		
		return loop;
	}

	public IfStatement getIf(int maxNestedStatements) {
		IfStatement ifStatement = null;
		
		if (maxNestedStatements > minBlockStatements) {
			Expression condition = getExpression(Boolean.class, maxExpressionDepth);
		
			Block ifCode = getBlock(minBlockStatements, maxNestedStatements);
		
			ifStatement = new IfStatement(condition, ifCode);
		}
		
		return ifStatement;
	}

	public Assignment getAssignment() {
		//TODO If unable to create an expr of same type as var, should try a different var.
		Assignment assignment = null;
		
		// Randomly choose variable from those in scope.
		Variable var = getVariable(null);
		
		if (var != null) {
			// What type does the expression need to be?
			Class<?> datatype = var.getReturnType();
			
			// Generate a new expression to be assigned.
			Expression expr = getExpression(datatype, maxExpressionDepth);
			
			if (expr != null) {
				// Can create an expression of the right type.
				assignment = new Assignment(var, expr);
			}
		}
		
		return assignment;
	}

	public Declaration getDeclaration() {
		// Initialise the expression we will assign the value of into the variable.
		Expression expr = getExpression(null, maxExpressionDepth);
		
		//TODO Need a way of generating variable names.
		// Create new variable of a matching type. Expr shouldn't be null because can be any expression.
		Class<?> type = expr.getReturnType();
		Variable var =  new Variable("var", type);
		
		// Add new variable to set of in-scope variables.
		activeVariables.add(var);
		allVariables.add(var);
		
		return new Declaration(var, expr);
	}
	
	public Expression getExpression(Class<?> datatype, int maxDepth) {
		Expression result = null;
		
		int noOptions = 4;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));

			if (ran == 0) {
				// Literal value.
				result = getLiteral(datatype);
			} else if (ran == 1) {
				// Variable.
				result = getVariable(datatype);
			} else if (ran == 2) {
				// Binary expression.
				//if (maxExpressionDepth > 0) {
					result = getBinaryExpression(datatype, maxDepth);
				//}
			} else {
				// Unary expression.
				//if (maxExpressionDepth > 0) {
					result = getUnaryExpression(datatype, maxDepth);
				//}
			}
		}
		
		return result;
	}
	
	public BinaryExpression getBinaryExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		BinaryExpression result = null;
		
		int noOptions = 11;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));

			if (ran == 0) {
				result = getDivideExpression(datatype, maxDepth);
			} else if (ran == 1) {
				result = getMultiplyExpression(datatype, maxDepth);
			} else if (ran == 2) {
				result = getSubtractExpression(datatype, maxDepth);
			} else if (ran == 3){
				result = getAddExpression(datatype, maxDepth);
			} else if (ran == 4){
				result = getModuloExpression(datatype, maxDepth);
			} else if (ran == 5){
				result = getAndExpression(datatype, maxDepth);
			} else if (ran == 6){
				result = getOrExpression(datatype, maxDepth);
			} else if (ran == 7){
				result = getGreaterThanExpression(datatype, maxDepth);
			} else if (ran == 8){
				result = getGreaterThanOrEqualToExpression(datatype, maxDepth);
			} else if (ran == 9){
				result = getLessThanExpression(datatype, maxDepth);
			} else if (ran == 10){
				result = getLessThanOrEqualToExpression(datatype, maxDepth);
			} else {
				result = getEqualsExpression(datatype, maxDepth);
			}
		}
		
		return result;
	}
	
	public DivideExpression getDivideExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		DivideExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			//TODO These shouldn't have to be the same, but is tricky to ensure return type is correct.
			Expression leftExpression = getExpression(datatype, maxDepth-1);
			Expression rightExpression = getExpression(datatype, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new DivideExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public MultiplyExpression getMultiplyExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		MultiplyExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			//TODO These shouldn't have to be the same, but is tricky to ensure return type is correct.
			Expression leftExpression = getExpression(datatype, maxDepth-1);
			Expression rightExpression = getExpression(datatype, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new MultiplyExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public SubtractExpression getSubtractExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		SubtractExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			//TODO These shouldn't have to be the same, but is tricky to ensure return type is correct.
			Expression leftExpression = getExpression(datatype, maxDepth-1);
			Expression rightExpression = getExpression(datatype, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new SubtractExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public AddExpression getAddExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		AddExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			//TODO These shouldn't have to be the same, but is tricky to ensure return type is correct.
			Expression leftExpression = getExpression(datatype, maxDepth-1);
			Expression rightExpression = getExpression(datatype, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new AddExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public AndExpression getAndExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		AndExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Boolean.class, maxDepth-1);
			Expression rightExpression = getExpression(Boolean.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new AndExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public OrExpression getOrExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		OrExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Boolean.class, maxDepth-1);
			Expression rightExpression = getExpression(Boolean.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new OrExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public ModuloExpression getModuloExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		ModuloExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			//TODO These shouldn't have to be the same, but is tricky to ensure return type is correct.
			Expression leftExpression = getExpression(datatype, maxDepth-1);
			Expression rightExpression = getExpression(datatype, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new ModuloExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public GreaterThanExpression getGreaterThanExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		GreaterThanExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Number.class, maxDepth-1);
			Expression rightExpression = getExpression(Number.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new GreaterThanExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public GreaterThanOrEqualToExpression getGreaterThanOrEqualToExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		GreaterThanOrEqualToExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Number.class, maxDepth-1);
			Expression rightExpression = getExpression(Number.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new GreaterThanOrEqualToExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public LessThanExpression getLessThanExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		LessThanExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Number.class, maxDepth-1);
			Expression rightExpression = getExpression(Number.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new LessThanExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public LessThanOrEqualToExpression getLessThanOrEqualToExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		LessThanOrEqualToExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Number.class, maxDepth-1);
			Expression rightExpression = getExpression(Number.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new LessThanOrEqualToExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}
	
	public EqualsExpression getEqualsExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Binary expression is going to require a depth of at least 1.
			return null;
		}
		
		EqualsExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression leftExpression = getExpression(Number.class, maxDepth-1);
			Expression rightExpression = getExpression(Number.class, maxDepth-1);
			
			if (leftExpression != null && rightExpression != null) {
				expr = new EqualsExpression(leftExpression, rightExpression);
			}
		}
		
		return expr;
	}

	public UnaryExpression getUnaryExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Unary expression is going to require a depth of at least 1.
			return null;
		}
		
		UnaryExpression result = null;
		
		int noOptions = 4;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));

			if (ran == 0) {
				result = getNotExpression(datatype, maxDepth);
			} else if (ran == 1) {
				result = getPostDecrementExpression(datatype, maxDepth);
			} else if (ran == 2) {
				result = getPreDecrementExpression(datatype, maxDepth);
			} else if (ran == 3) {
				result = getPostIncrementExpression(datatype, maxDepth);
			} else {
				result = getPreIncrementExpression(datatype, maxDepth);
			}
		}
		
		return result;
	}
	
	public NotExpression getNotExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Unary expression is going to require a depth of at least 1.
			return null;
		}
		
		NotExpression expr = null;
		
		if (datatype == null || Boolean.class.isAssignableFrom(datatype)) {
			Expression subExpression = getExpression(Boolean.class, maxDepth-1);
			
			if (subExpression != null) {
				expr = new NotExpression(subExpression);
			}
		}
		
		return expr;
	}
	
	public PostIncrementExpression getPostIncrementExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Unary expression is going to require a depth of at least 1.
			return null;
		}
		
		PostIncrementExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			Variable var = getVariable(datatype);
			
			if (var != null) {
				expr = new PostIncrementExpression(var);
			}
		}
		
		return expr;
	}
	
	public PreIncrementExpression getPreIncrementExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Unary expression is going to require a depth of at least 1.
			return null;
		}
		
		PreIncrementExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			Variable var = getVariable(datatype);
			
			if (var != null) {
				expr = new PreIncrementExpression(var);
			}
		}
		
		return expr;
	}
	
	public PostDecrementExpression getPostDecrementExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Unary expression is going to require a depth of at least 1.
			return null;
		}
		
		PostDecrementExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			Variable var = getVariable(datatype);
			
			if (var != null) {
				expr = new PostDecrementExpression(var);
			}
		}
		
		return expr;
	}
	
	public PreDecrementExpression getPreDecrementExpression(Class<?> datatype, int maxDepth) {
		if (maxDepth < 1) {
			// Unary expression is going to require a depth of at least 1.
			return null;
		}
		
		PreDecrementExpression expr = null;
		
		// If it's null, then generate for any number type.
		datatype = (datatype==null) ? Number.class : datatype;
		
		if (TypeUtils.isNumericType(datatype)) {
			Variable var = getVariable(datatype);
			
			if (var != null) {
				expr = new PreDecrementExpression(var);
			}
		}
		
		return expr;
	}

	public Literal getLiteral(Class<?> datatype) {
		List<Literal> options = new ArrayList<Literal>();
		
		for (Literal literal: literals) {
			if (datatype == null || datatype.isAssignableFrom(literal.getReturnType())) {
				options.add(literal);
			}
		}
		
		Literal selectedLiteral = null;
		
		if (!options.isEmpty()) {
			selectedLiteral = options.get(rng.nextInt(options.size()));
		}
		
		return (Literal) selectedLiteral.newInstance();
	}

	public Variable getVariable(Class<?> datatype) {
		// Randomly select and return a variable from those in-scope.
		List<Variable> options = new ArrayList<Variable>();
		
		for (Variable var: activeVariables) {
			if (datatype == null || datatype.isAssignableFrom(var.getReturnType())) {
				options.add(var);
			}
		}
		
		Variable selectedVar = null;
		
		if (!options.isEmpty()) {
			selectedVar = options.get(rng.nextInt(options.size()));
		}
		
		return selectedVar;
	}
}
