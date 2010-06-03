package org.epochx.gx.representation;

public class IfStatement implements Statement {

	private Expression condition;
	
	private Block ifCode;
	
	private Block elseCode;
	
	public IfStatement(Expression condition, Block ifCode, Block elseCode) {
		this.condition = condition;
		this.ifCode = ifCode;
		this.elseCode = elseCode;
	}
	
	public IfStatement(Expression condition, Block ifCode) {
		this(condition, ifCode, null);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("if (");
		buffer.append(condition.toString());
		buffer.append(")");
		buffer.append(ifCode.toString());
		if (elseCode != null) {
			buffer.append(" else ");
			buffer.append(elseCode.toString());
		}
		
		return buffer.toString();
	}
	
	@Override
	public IfStatement clone() {
		IfStatement clone = null;
		try {
			clone = (IfStatement) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.condition = this.condition.clone();
		clone.ifCode = this.ifCode.clone();
		clone.elseCode = this.elseCode.clone();
		
		return clone;
	}
	
}
