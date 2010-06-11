package org.epochx.gx.representation;


public class BooleanLiteral implements Literal {

	private boolean value;
	
	public BooleanLiteral(boolean value) {
		this.value = value;
	}
	
	public boolean getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(value);
	}
	
	@Override
	public BooleanLiteral clone() {
		BooleanLiteral clone = null;
		try {
			clone = (BooleanLiteral) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.value = this.value;
		
		return clone;
	}

	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}

	@Override
	public void modifyExpression(double probability) {
		//TODO Should use model's RNG.
		//TODO Could modify the boolean value.
		/*double rand = Math.random();
		
		if (rand < probability) {
			value = 
		}*/
	}
	
}
