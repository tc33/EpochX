package org.epochx.gp.util;

import java.util.*;

import org.epochx.gp.representation.*;

public final class NodeUtils {

	public static List<DoubleLiteral> doubleRange(int start, int end) {
		List<DoubleLiteral> range = new ArrayList<DoubleLiteral>();
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;			
		}
		
		while (start<=end) {
			start++;
			
			range.add(new DoubleLiteral((double) start));
		}
		
		return range;
	}
	
	public static List<BooleanVariable> booleanVariables(String ... variableNames) {
		List<BooleanVariable> variables = new ArrayList<BooleanVariable>();
		
		for (String name: variableNames) {
			variables.add(new BooleanVariable(name));
		}
		
		return variables;
	}
	
	public static List<DoubleVariable> doubleVariables(String ... variableNames) {
		List<DoubleVariable> variables = new ArrayList<DoubleVariable>();
		
		for (String name: variableNames) {
			variables.add(new DoubleVariable(name));
		}
		
		return variables;
	}
}
