package org.epochx.representation;

public class DoubleVariable extends DoubleNode {

	private String identifier;
	
	private Double value;
	
	public DoubleVariable(String identifier) {
		this(identifier, null);
	}
	
	public DoubleVariable(String identifier, Double value) {
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
	
	@Override
	public String toString() {
		return identifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj == this);
	}
	
	@Override
	public DoubleVariable clone() {
		return this;
	}
}
