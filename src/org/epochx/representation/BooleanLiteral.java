package org.epochx.representation;

public class BooleanLiteral extends BooleanNode {

	private boolean value;
	
	public BooleanLiteral(boolean value) {
		this.value = value;
	}
	
	@Override
	public Boolean evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return Boolean.toString(value);
	}

}
