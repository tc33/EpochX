package org.epochx.gx.representation;

public class IntLiteral implements Literal {

	private int value;
	
	public IntLiteral(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
	@Override
	public IntLiteral clone() {
		IntLiteral clone = null;
		try {
			clone = (IntLiteral) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.value = this.value;
		
		return clone;
	}

	@Override
	public DataType getDataType() {
		return DataType.INT;
	}

	@Override
	public void modifyExpression(double probability) {
		//TODO Could modify the int value.
	}
}
