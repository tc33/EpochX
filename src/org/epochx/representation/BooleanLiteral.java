package org.epochx.representation;

public class BooleanLiteral extends BooleanNode {

	private Boolean value;
	
	public BooleanLiteral(Boolean value) {
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

	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean inst = (obj instanceof BooleanLiteral);
		
		if (!inst)
			return false;
		
		Boolean objVal = ((BooleanLiteral) obj).value;
		Boolean thisVal = this.value;
		
		if ((objVal == null) ^ (thisVal == null)) {
			return false;
		}
		
		return (thisVal == objVal) || thisVal.equals(objVal);
	}
	
	@Override
	public BooleanLiteral clone() {
		BooleanLiteral clone = (BooleanLiteral) super.clone();
		
		clone.value = this.value;

		return clone;
	}
}
