package org.epochx.gx.node;

import java.util.*;

import org.epochx.gx.util.*;


public class Subroutine extends ASTNode {

	private String name;
	
	private List<Variable> parameters;
	
	private boolean voidReturn;
	
	public Subroutine() {
		this(null, null);
	}
	
	public Subroutine(final Block child) {
		// A void subroutine, with no return value.
		super(child);
		
		voidReturn = true;
	}

	public Subroutine(final Block child1, final ReturnStatement child2) {
		super(child1, child2);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parameters
	 */
	public List<Variable> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<Variable> parameters) {
		this.parameters = parameters;
	}

	@Override
	public Object evaluate() {
		getChild(0).evaluate();
		
		if (voidReturn) {
			return null;
		} else {
			return getChild(1).evaluate();
		}
	}

	@Override
	public String getIdentifier() {
		return "SUBROUTINE";
	}
	
//	public int getNoStatements() {
//		Block body = (Block) getChild(0);
//		
//		int noStatements = 0;
//		if (body != null) {
//			noStatements = body.getNoNestedStatements();
//		}
//		
//		return noStatements;
//	}
	
	public Block getBody() {
		return (Block) getChild(0);
	}
	
	public ReturnStatement getReturnStatement() {
		if (voidReturn) {
			return null;
		} else {
			return (ReturnStatement) getChild(1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 && inputTypes[0] == Void.class) {
			return inputTypes[1];
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		if (voidReturn) {
			return (Class<ASTNode>[]) new Class<?>[]{Block.class};
		} else {
			return (Class<ASTNode>[]) new Class<?>[]{Block.class, ReturnStatement.class};
		}
	}
	
	@Override
	public String toJava() {
		// Temporarily put return statement inside block.
		Block body = (Block) getChild(0);
		
		String returnType = "void";
		if (!voidReturn) {
			ReturnStatement returnStatement = (ReturnStatement) getChild(1);
			Utils.insertChild(body, returnStatement, body.getArity());
			returnType = returnStatement.getReturnType().getSimpleName();
		}
		
		StringBuilder buffer = new StringBuilder();
		buffer.append("public ");
		buffer.append(returnType);
		buffer.append(' ');
		buffer.append(name);
		buffer.append('(');
		if (parameters != null) {
			for (int i=0; i<parameters.size(); i++) {
				Variable param = parameters.get(i);
				if (i > 0) {
					buffer.append(", ");
				}				
				buffer.append(param.getReturnType().getSimpleName());
				buffer.append(' ');
				buffer.append(param.getIdentifier());
			}
		}	
		buffer.append(')');
		buffer.append(body.toJava());
		
		if (!voidReturn) {
			Utils.removeChild(body, body.getArity());
		}
		
		return buffer.toString();
	}
}
