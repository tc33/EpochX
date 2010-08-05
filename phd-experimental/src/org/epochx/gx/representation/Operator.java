package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public class Operator {

	private String op;
	private OperatorType opType;
	private DataType dataType;
	
	public enum OperatorType {
		BINARY, UNARY_PRE, UNARY_POST;
	}
	
	public Operator(OperatorType opType, DataType dataType, String op) {
		this.op = op;
		this.opType = opType;
		this.dataType = dataType;
	}

	/**
	 * @return the op
	 */
	public String getOp() {
		return op;
	}

	/**
	 * @return the type
	 */
	public OperatorType getOperatorType() {
		return opType;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public static Operator getBinaryOperator(RandomNumberGenerator rng, DataType dataType) {
		String[] operators = null;
		
		if (dataType == DataType.BOOLEAN) {
			operators = new String[]{"&&", "||", "^"}; // ^ not supported by bsh.
		} else if (dataType == DataType.INT) {
			operators = new String[]{"+", "/", "%", "*", "-"};
		} else if (dataType == DataType.DOUBLE) {
			operators = new String[]{"+", "/", "%", "*", "-"};
		} else {
			// Broken.
		}
		
		int ran = rng.nextInt(operators.length);
		
		return new Operator(OperatorType.BINARY, dataType, operators[ran]);
	}
	
	public static Operator getUnaryOperator(RandomNumberGenerator rng, DataType dataType) {
		String[] operators = null;
		if (dataType == DataType.BOOLEAN) {
			operators = new String[]{"!"};
		} else if (dataType == DataType.INT) {
			operators = new String[]{"++", "--"};
		} else if (dataType == DataType.DOUBLE) {
			//operators = new String[]{"++", "--"};
		} else {
			// Broken.
		}
		
		if (operators != null && operators.length > 0) {
			int ran = rng.nextInt(operators.length);
		
			return new Operator(OperatorType.UNARY_PRE, dataType, operators[ran]);	
		} else {
			return null;
		}
	}
	
	public Object evaluateBinaryOperator(Object operand1, Object operand2) {
		if (op.equals("&&")) {
			return (Boolean) operand1 && (Boolean) operand2;
		} else if (op.equals("||")) {
			return (Boolean) operand1 || (Boolean) operand2;
		} else if (op.equals("^")) {
			return (Boolean) operand1 ^ (Boolean) operand2;
		} else if ((dataType == DataType.INT) && op.equals("+")) {
			return (Integer) operand1 + (Integer) operand2;
		} else if ((dataType == DataType.INT) && op.equals("/")) {
			if ((Integer) operand2 == 0) {
				return 0;
			} else {
				return (Integer) operand1 / (Integer) operand2;
			}
		} else if ((dataType == DataType.INT) && op.equals("%")) {
			if ((Integer) operand2 == 0) {
				return 0;
			} else {
				return (Integer) operand1 % (Integer) operand2;
			}
		} else if ((dataType == DataType.INT) && op.equals("*")) {
			return (Integer) operand1 * (Integer) operand2;
		} else if ((dataType == DataType.INT) && op.equals("-")) {
			return (Integer) operand1 - (Integer) operand2;
		} else if ((dataType == DataType.DOUBLE) && op.equals("+")) {
			return (Double) operand1 + (Double) operand2;
		} else if ((dataType == DataType.DOUBLE) && op.equals("/")) {
			if ((Double) operand2 == 0.0) {
				return 0.0;
			} else {
				return (Double) operand1 / (Double) operand2;
			}
		} else if ((dataType == DataType.DOUBLE) && op.equals("%")) {
			if ((Double) operand2 == 0.0) {
				return 0.0;
			} else {
				return (Double) operand1 % (Double) operand2;
			}
		} else if ((dataType == DataType.DOUBLE) && op.equals("*")) {
			return (Double) operand1 * (Double) operand2;
		} else if ((dataType == DataType.DOUBLE) && op.equals("-")) {
			return (Double) operand1 - (Double) operand2;
		} else {
			// Broken.
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return op;
	}
}
