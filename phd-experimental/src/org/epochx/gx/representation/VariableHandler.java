package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.model.*;
import org.epochx.life.*;
import org.epochx.tools.random.*;

public class VariableHandler {

	// The controlling model.
	private GXModel model;
	
	// The variables that are currently in scope.
	private Stack<Variable> activeVariables;
	
	// The parameters which are available as variables.
	private Set<Variable> parameters;
	
	// All variables that are used throughout the program, in scope or otherwise.
	private Set<Variable> allVariables;
	
	private RandomNumberGenerator rng;
	
	public VariableHandler(final GXModel model) {
		this.model = model;
		
		activeVariables = new Stack<Variable>();
		parameters = new HashSet<Variable>();
		allVariables = new HashSet<Variable>();
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	private void configure() {
		rng = model.getRNG();
	}
	
	/**
	 * Arguments are essentially variables but they are available at the 
	 * start of each program that is generated with the assumption that they
	 * have already been declared.
	 * 
	 * @param var
	 */
	public void setParameters(Variable ... parameters) {
		this.parameters.clear();
		for (Variable v: parameters) {
			this.parameters.add(v);
			add(v);
		}
	}
	
	public void setParameterValue(String varName, Object value) {
		for (Variable v: parameters) {
			if (v.getVariableName().equals(varName)) {
				v.setValue(value);
				break;
			}
		}
	}
	
	public boolean isParameter(Variable var) {
		return parameters.contains(var);
	}
	
	public void reset() {
		activeVariables.clear();
		activeVariables.addAll(parameters);
		allVariables.clear();
	}
	
	public Variable getActiveVariable() {
		Variable var = null;
		
		if (!activeVariables.isEmpty()) {
			int choice = rng.nextInt(activeVariables.size());
			
			var = activeVariables.get(choice);
		}
		
		return var;
	}
	
	public Variable getActiveVariable(DataType dataType) {
		Variable var = null;
		List<Variable> variables = getActiveVariables(dataType);
		
		if (!variables.isEmpty()) {
			int choice = rng.nextInt(variables.size());
			
			var = variables.get(choice);
		}
		
		return var;
	}
	
	/**
	 * Returns a copy of the list of active variables. The variable instances 
	 * themselves are not cloned.
	 * @return
	 */
	public List<Variable> getActiveVariables() {
		return new ArrayList<Variable>(activeVariables);
	}
	
	public List<Variable> getActiveVariablesCopy() {
		List<Variable> activeVars = getActiveVariables();
		for (int i=0; i<activeVars.size(); i++) {
			activeVars.set(i, activeVars.get(i).clone());
		}
		return activeVars;
	}
	
	public List<Variable> getActiveVariables(DataType dataType) {
		List<Variable> typeVariables = new ArrayList<Variable>();
		for (Variable v: activeVariables) {
			if (v.getDataType() == dataType) {
				typeVariables.add(v);
			}
		}
		return typeVariables;
	}
	
	public String getNewVariableName() {
		String name;
		
		int i = 0;
		do {
			name = "var" + i++;
		} while (variablesContains(name));
		
		return name;
	}
	
	public boolean variablesContains(String varName) {
		for (Variable v: allVariables) {
			if (v.getVariableName().equals(varName)) {
				return true;
			}
		}
		return false;
	}
	
	public void add(Variable variable) {
		allVariables.add(variable);
		activeVariables.push(variable);
	}
	
	public void removeActiveVariable(Variable variable) {
		activeVariables.remove(variable);
	}
	
	public int getNoActiveVariables() {
		return activeVariables.size();
	}
	
	public void setNoActiveVariables(int size) {
		activeVariables.setSize(size);
	}
	
	public Set<Variable> getAllVariables() {
		return allVariables;
	}
	
	public void setAllVariables(Set<Variable> allVariables) {
		this.allVariables = allVariables;
	}
}
