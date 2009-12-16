package org.epochx.representation;

public class BooleanVariable extends BooleanNode {

	private String identifier;
	
	private Boolean value;
	
	public BooleanVariable(String identifier) {
		this(identifier, null);
	}
	
	public BooleanVariable(String identifier, Boolean value) {
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

	@Override
	public String toString() {
		return identifier;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj == this);
	}
	
	@Override
	public BooleanVariable clone() {
		return this;
	}
}
