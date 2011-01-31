package org.epochx.gx.node;


import org.epochx.tools.util.*;

public class PreDecrementExpression extends UnaryExpression {

	public PreDecrementExpression() {
		this(null);
	}
	
	public PreDecrementExpression(Expression child) {
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
			var.setValue(d--);
			
			return d;
		} else if (returnType == Long.class) {
			// Increment as long.
			long l = NumericUtils.asLong(c);
			var.setValue(l--);
			
			return l;
		} else if (returnType == Integer.class) {
			// Increment as integer.
			int i = NumericUtils.asInteger(c);
			var.setValue(i--);
			
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
		return "PRE-INC";
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
		buffer.append("--");
		buffer.append(((ASTNode) getChild(0)).toJava());
		
		return buffer.toString();
	}
}
