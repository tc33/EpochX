package org.epochx.gx.tools.util;

import java.util.*;

import org.epochx.gx.representation.*;

public class VariableUtils {

	public static Expression copyVariable(Expression exp, Map<Variable, Variable> variableCopies, VariableHandler vars) {
		if (exp instanceof Variable) {
			Variable v = (Variable) exp;
			if (!vars.isParameter(v)) {
				if (!variableCopies.containsKey(v)) {
					variableCopies.put(v, v.copy());
				}
				exp = variableCopies.get(v);
			}
		} else {
			exp.copyVariables(vars, variableCopies);
		}
		
		return exp;
	}
	
}
