/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
package org.epochx.ge.fitness;

import static org.epochx.Config.Template.TEMPLATE;

import org.epochx.Config;
import org.epochx.Config.ConfigKey;
import org.epochx.DoubleFitness;
import org.epochx.Individual;
import org.epochx.epox.Variable;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.ge.GEIndividual;
import org.epochx.interpret.Interpreter;
import org.epochx.interpret.MalformedProgramException;

/**
 * A fitness function for <code>STGPIndividual</code>s that calculates and assigns 
 * <code>DoubleFitness.Minimise</code> scores. The fitness scores are calculated by executing
 * the program for each of the provided sets of inputs. The difference between the value 
 * returned by the program and the expected outputs supplied is summed to give the fitness
 * value. 
 * 
 * When using this fitness function the {@link #INPUT_VARIABLES}, {@link #INPUT_VALUE_SETS} 
 * and {@link #EXPECTED_OUTPUTS} config options must be set, or the same values set using the 
 * mutator methods provided. The length of the INPUT_VALUE_SETS array should match the length 
 * of the EXPECTED_OUTPUTS array and the number of values in each set should match the length 
 * of the INPUT_VARIABLES array.
 * 
 * If the program returns NaN for any of the input sets then a fitness
 * score of NaN is assigned by default.
 */
public class SumOfError extends GEFitnessFunction implements Listener<ConfigEvent> {
	
	/**
	 * The key for setting the program's input variables
	 */
	public static final ConfigKey<Variable[]> INPUT_VARIABLES = new ConfigKey<Variable[]>();
	
	/**
	 * The key for setting the sets of values to use as inputs. The length of the array should 
	 * match the length of the EXPECTED_OUTPUTS array and the number of values in each set 
	 * should match the length of the INPUT_VARIABLES array.
	 */
	public static final ConfigKey<Object[][]> INPUT_VALUE_SETS = new ConfigKey<Object[][]>();
	
	/**
	 * The key for setting the expected output values from the programs being evaluated
	 */
	public static final ConfigKey<Double[]> EXPECTED_OUTPUTS = new ConfigKey<Double[]>();
	
	// Configuration settings
	private Interpreter<GEIndividual> interpreter;
	private String[] argNames;
	private Object[][] inputValueSets;
	private Double[] expectedOutputs;
	private Double malformedPenalty;
	
	/**
	 * Constructs a <code>SumOfError</code> fitness function with control parameters
	 * automatically loaded from the config.
	 */
	public SumOfError() {
		this(true);
	}
	
	/**
	 * Constructs a <code>SumOfError</code> fitness function with control parameters initially
	 * loaded from the config. If the <code>autoConfig</code> argument is set to <code>true</code> 
	 * then the configuration will be automatically updated when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public SumOfError(boolean autoConfig) {
		setup();
		
		malformedPenalty = Double.MAX_VALUE;

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link #INPUT_VARIABLES}
	 * <li>{@link #INPUT_VALUE_SETS}
	 * <li>{@link #EXPECTED_OUTPUTS}
	 * <li>{@link #MALFORMED_PENALTY}
	 * <li>{@link #INTERPRETER}
	 * </ul>
	 */
	protected void setup() {
		argNames = Config.getInstance().get(INPUT_IDENTIFIERS);
		inputValueSets = Config.getInstance().get(INPUT_VALUE_SETS);
		expectedOutputs = Config.getInstance().get(EXPECTED_OUTPUTS);
		interpreter = Config.getInstance().get(INTERPRETER);
		malformedPenalty = Config.getInstance().get(MALFORMED_PENALTY, malformedPenalty);
	}
	
	/**
	 * Receives configuration events and triggers this fitness function to 
	 * configure its parameters if the <code>ConfigEvent</code> is for one of 
	 * its required parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, INPUT_IDENTIFIERS, INPUT_VALUE_SETS, EXPECTED_OUTPUTS, INTERPRETER, MALFORMED_PENALTY)) {
			setup();
		}
	}
	
	/**
	 * Calculates the fitness of the given individual. This fitness function only operates
	 * on GEIndividuals with a Double data-type. The fitness returned will be an instance 
	 * of DoubleFitness.Minimise. The fitness score is calculated as the sum of the difference 
	 * between the expected outputs and the actual outputs, for each set of inputs.
	 * 
	 * @param individual the individual to evaluate the fitness of
	 * @return the fitness of the given individual
	 * @throws IllegalArgumentException if the individual is not an STGPIndividual or the 
	 * individual's data-type is not Double.
	 */
	@Override
	public DoubleFitness.Minimise evaluate(Individual individual) {
		if (!(individual instanceof GEIndividual)) {
			throw new IllegalArgumentException("Unsupported representation");
		}

		GEIndividual program = (GEIndividual) individual;
		
		Double errorSum = 0.0;
		
		Object[] results;
		try {
			results = interpreter.eval(program, argNames, inputValueSets);
		} catch (MalformedProgramException e) {
			return new DoubleFitness.Minimise(malformedPenalty);
		}
		
		for (int i = 0; i < expectedOutputs.length; i++) {
			Object result = results[i];
			
			if (result instanceof Double) {
				double d = (Double) result;
				double error = Math.abs(d - expectedOutputs[i]);

				if (Double.isNaN(d)) {
					errorSum += error;
				} else {
					errorSum = malformedPenalty;
					break;
				}
			}
		}
		
		return new DoubleFitness.Minimise(errorSum);
	}
	
	/**
	 * Gets the names of the input variables
	 * 
	 * @return an array of the input variable names
	 */
	public String[] getInputIdentifiers() {
		return argNames;
	}
	
	/**
	 * Sets the names of the input variables.
	 * 
	 * If automatic configuration is enabled then any value set here will be overwritten 
	 * by the {@link #INPUT_IDENTIFIERS} configuration setting on the next config event.
	 * 
	 * @param argNames the names of the input variables
	 */
	public void setInputIdentifiers(String[] argNames) {
		this.argNames = argNames;
	}
	
	/**
	 * Returns the sets of input values.
	 * 
	 * @return the sets of input values
	 */
	public Object[][] getInputValueSets() {
		return inputValueSets;
	}
	
	/**
	 * Sets the sets of input values. The length of the array should match the length
	 * of the expected outputs array. Each set of values should have the same number of
	 * values, equal to the length of the input identifiers array.
	 * 
	 * If automatic configuration is enabled then any value set here will be overwritten 
	 * by the {@link #INPUT_VALUE_SETS} configuration setting on the next config event.
	 * 
	 * @param inputValueSets the sets of input values
	 */
	public void setInputValueSets(Object[][] inputValueSets) {
		this.inputValueSets = inputValueSets;
	}
	
	/**
	 * Returns the expected outputs that the actual outputs will be compared against
	 * 
	 * @return the expected outputs for the input sets
	 */
	public Double[] getExpectedOutputs() {
		return expectedOutputs;
	}
	
	/**
	 * Sets the expected outputs to compare against. The length of the array should 
	 * match the length of the input values array.
	 * 
	 * If automatic configuration is enabled then any value set here will be 
	 * overwritten by the {@link #EXPECTED_OUTPUTS} configuration setting on the 
	 * next config event.
	 * 
	 * @param expectedOutputs the expected outputs to compare against
	 */
	public void setExpectedOutputs(Double[] expectedOutputs) {
		this.expectedOutputs = expectedOutputs;
	}
	
	/**
	 * Returns the fitness score that will be assigned to individuals that are 
	 * malformed
	 * 
	 * @return the penalty to be assigned to malformed individuals
	 */
	public Double getMalformedProgramPenalty() {
		return malformedPenalty;
	}
	
	/**
	 * Sets the fitness score to be assigned to individuals that are malformed.
	 * 
	 * If automatic configuration is enabled then any value set here will be 
	 * overwritten by the {@link #MALFORMED_PENALTY} configuration setting on the 
	 * next config event.
	 * 
	 * @param malformedPenalty
	 */
	public void setMalformedProgramPenalty(Double malformedPenalty) {
		this.malformedPenalty = malformedPenalty;
	}
	
	/**
	 * Returns the interpreter being used to execute individuals
	 * 
	 * @return the interpreter in use
	 */
	public Interpreter<GEIndividual> getInterpreter() {
		return interpreter;		
	}
	
	/**
	 * Sets the interpreter to use to execute individuals
	 * 
	 * @param interpreter the interpreter to execute individuals with
	 */
	public void setInterpreter(Interpreter<GEIndividual> interpreter) {
		this.interpreter = interpreter;
	}
}
