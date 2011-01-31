package org.epochx.gx.node;


import org.epochx.tools.util.*;

public class PostDecrementExpression extends UnaryExpression {

	public PostDecrementExpression() {
		this(null);
	}
	
	public PostDecrementExpression(Expression child) {
		super(child);
	}

	@Override
	public Object evaluate() {
		Variable var = (Variable) getChild(0);
		Object c = var.evaluate();
		
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			// Increment as double.
			double d = NumericUtils.asDouble(c);
			var.setValue(d-1);
			
			return d;
		} else if (returnType == Long.class) {
			// Increment as long.
			long l = NumericUtils.asLong(c);
			var.setValue(l-1);
			
			return l;
		} else if (returnType == Integer.class) {
			// Increment as integer.
			int i = NumericUtils.asInteger(c);
			var.setValue(i-1);
			
			return i;
		}

		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "POST-INC";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1 && TypeUtils.isNumericType(inputTypes[0])) {
			return inputTypes[0];
		}
		
		return null;
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Variable.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append("--");
		
		return buffer.toString();
	}
}
