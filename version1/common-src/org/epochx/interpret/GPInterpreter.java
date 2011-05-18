/* 
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.interpret;

import java.util.*;

import org.epochx.core.*;
import org.epochx.epox.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.life.ConfigListener;


/**
 * 
 */
public class GPInterpreter implements Interpreter<GPCandidateProgram>, ConfigListener {

	private Evolver evolver;
	
	private Map<String, Variable> variables;
	
	public GPInterpreter(List<Node> syntax) {
		variables = new HashMap<String, Variable>();
		
		for (Node n: syntax) {
			if (n instanceof Variable) {
				Variable var = (Variable) n;
				String identifier = var.getIdentifier();

				variables.put(identifier, var);
			}
		}
	}
	
	public GPInterpreter(Evolver evolver) {
		this.evolver = evolver;
		
		variables = new HashMap<String, Variable>();
		
		// Configure parameters from the model.
		evolver.getLife().addConfigListener(this, false);
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	@Override
	public void configure(Model model) {
		if (model instanceof GPModel) {
			variables.clear();
			List<Node> syntax = ((GPModel) model).getSyntax();
			
			for (Node n: syntax) {
				if (n instanceof Variable) {
					Variable var = (Variable) n;
					String identifier = var.getIdentifier();

					variables.put(identifier, var);
				}
			}
		}
	}
	
	@Override
	public Object[] eval(GPCandidateProgram program, Parameters params) {
		int noParamSets = params.getNoParameterSets();
		int noParams = params.getNoParameters();
		
		Object[] results = new Object[noParamSets];
		for (int i=0; i<noParamSets; i++) {
			Object[] paramSet = params.getParameterSet(i);
			
			// Set the values of this round of variables.
			for (int j=0; j<noParams; j++) {
				Variable var = variables.get(params.getIdentifier(j));
				
				if (var != null) {
					var.setValue(paramSet[j]);
				}
			}
			
			// Evaluate the program tree.
			results[i] = program.getRootNode().evaluate();
		}
		
		return results;
	}
	
	@Override
	public void exec(GPCandidateProgram program, Parameters params) {
		eval(program, params);
	}

}
