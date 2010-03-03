package org.epochx.op;

public interface Operator {
	
	/**
	 * Operator statistics are those statistics which are unique to a specific 
	 * operator. An operator may provide any statistics about its run that it 
	 * wishes. Look at using the OPERATOR_STATS statistics fields for 
	 * requesting these statistics for an operation.
	 * 
	 * @return An Object array of operator specific statistics.
	 */
	public Object[] getOperatorStats();
}
