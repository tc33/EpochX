package org.epochx.representation;

public class BooleanVariable extends BooleanNode {

	private String identifier;
	
	private boolean value;
	
	public BooleanVariable(String identifier, boolean value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	public void setValue(boolean value) {
		this.value = value;
	}
	
	@Override
	public Boolean evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

}
