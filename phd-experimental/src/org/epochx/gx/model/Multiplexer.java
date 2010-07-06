/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gx.model;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.gx.representation.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.util.BoolUtils;

/**
 * Grammar model for the multiplexer problems using a Java grammar.
 * 
 * <h4>Multiplexer problem</h4>
 * 
 * Given n binary inputValues, a program that solves the majority problem will 
 * return true in all circumstances where a majority of the inputValues are true 
 * (or 1), and return false whenever there is not a majority of true values.
 */
public class Multiplexer extends GXModel {
	
	// Java interpreter for performing evaluation.
	private final JavaInterpreter interpreter;
	
	// The boolean input sequences.
	private final boolean[][] inputValues;
	
	// The names of the inputValues used in the grammar.
	private String[] argNames;
	
	// The Variable objects for the arguments.
	private final Variable[] arguments;
	
	// No input bits.
	private int noAddressBits;
	private int noDataBits;
	
	/**
	 * Constructs a Multiplexer model for the given number of inputs.
	 * 
	 * @param noInputBits the number of inputs the multiplexer problem should be
	 * for
	 */
	public Multiplexer(final int noInputBits) {
		interpreter = new JavaInterpreter();

		// Generate the input sequences.
		inputValues = BoolUtils.generateBoolSequences(noInputBits);

		arguments = new Variable[noInputBits];
		
		// Calculate number of address/data bits.
		setBitSizes(noInputBits);
		
		// Determine the input argument names.
		setArgNames(noInputBits);
	}	
	
	/**
	 * Calculates the fitness score for the given program. The fitness of a 
	 * program for the majority problem is calculated by evaluating it 
	 * using each of the possible sets of input values. There are 
	 * <code>2^noInputBits</code> possible sets of inputs. The fitness of the 
	 * program is the quantity of those input sequences that the program 
	 * returned an incorrect response for. That is, a fitness value of
	 * <code>0.0</code> indicates the program responded correctly for every 
	 * possible set of input values.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */
	@Override
	public double getFitness(final CandidateProgram p) {
		final GXCandidateProgram program = (GXCandidateProgram) p;
		
		double score = 0;
        
        // Evaluate all possible inputValues.
		for (final boolean[] vars : inputValues) {
        	// Convert to object array.
        	final Boolean[] objVars = ArrayUtils.toObject(vars);
        	
        	Boolean result = null;
			try {
				result = (Boolean) interpreter.eval(program.getSourceCode(), argNames, objVars);
			} catch (MalformedProgramException e) {
				// Assign worst possible fitness and stop evaluating.
        		score = 0;
        		break;
			}
        	
            if (result != null && result == multiplex(vars)) {
                score++;
            }
        }
        
        return inputValues.length - score;
	}
	
	/*
	 * Calculate and set the number of address and data bits.
	 */
	private void setBitSizes(final int noInputBits) {
		noAddressBits = 1;
		while (true) {
			noDataBits = (int) Math.pow(2, noAddressBits);			
			
			if ((noAddressBits + noDataBits) == noInputBits) {
				break;
			}

			noAddressBits++;
		}
	}
	
	/*
	 * Set the argument names for the inputs.
	 */
	private void setArgNames(final int noInputBits) {
		argNames = new String[noInputBits];
		// Add address inputs.
		for (int i=0; i<noAddressBits; i++) {
			argNames[i] = "a" + i;
			arguments[i] = new Variable(DataType.BOOLEAN, argNames[i]);
		}
		// Add data inputs.
		for (int i=noAddressBits; i<noInputBits; i++) {
			argNames[i] = "d" + i;
			arguments[i] = new Variable(DataType.BOOLEAN, argNames[i]);
		}
	}
	
	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private Boolean multiplex(final boolean[] vars) {
		//TODO This is quite a lot slower than the x-bit specific versions.
		// Calculate which data position to use.
		int dataPosition = 0;
		for (int i=0; i<noAddressBits; i++) {
			if (vars[i]) {
				dataPosition += Math.pow(2, i);
			}
		}

		return vars[noAddressBits + dataPosition];
	}
	
    @Override
    public void run() {
    	getProgramGenerator().setParameters(arguments);
    	
    	super.run();
    }
}
