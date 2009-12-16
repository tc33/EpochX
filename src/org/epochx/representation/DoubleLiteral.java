package org.epochx.representation;

public class DoubleLiteral extends DoubleNode {

	private Double value;
	
	public DoubleLiteral(Double value) {
		this.value = value;
	}
	
	@Override
	public Double evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return Double.toString(value);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean inst = (obj instanceof DoubleLiteral);
		
		if (!inst)
			return false;
		
		Double objVal = ((DoubleLiteral) obj).value;
		Double thisVal = this.value;
		
		if ((objVal == null) ^ (thisVal == null)) {
			return false;
		}
		
		return (thisVal == objVal) || thisVal.equals(objVal);
	}
	
	@Override
	public DoubleLiteral clone() {
		DoubleLiteral clone = (DoubleLiteral) super.clone();
		
		clone.value = this.value;

		return clone;
	}
}
