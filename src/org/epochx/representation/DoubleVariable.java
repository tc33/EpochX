package org.epochx.representation;

public class DoubleVariable extends DoubleNode {

	private String identifier;
	
	private double value;
	
	public DoubleVariable(String identifier, double value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public Double evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}
	
}
