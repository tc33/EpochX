/* 
 * Copyright 2007-2013
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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.ge.fitness;

import static org.epochx.Config.Template.TEMPLATE;

import org.epochx.Config;
import org.epochx.Config.ConfigKey;
import org.epochx.Individual;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.fitness.DoubleFitness;
import org.epochx.ge.GEIndividual;
import org.epochx.interpret.Interpreter;
import org.epochx.interpret.MalformedProgramException;

/**
 * A fitness function for <code>GEIndividual</code>s that calculates and assigns 
 * <code>DoubleFitness.Minimise</code> scores. The fitness scores are calculated by executing
 * the program with an interpreter for each of the provided sets of inputs. The results 
 * are compared to the expected outputs and a count of the number of correct results is 
 * given as the fitness value. It can work with doubles or other object types. If doubles are 
 * used then the point error option is used, otherwise the objects are just compared for 
 * equality.
 * 
 * When using this fitness function the {@link GEFitnessFunction#INTERPRETER}, 
 * {@link #INPUT_IDENTIFIERS}, {@link #INPUT_VALUE_SETS} and {@link #EXPECTED_OUTPUTS} config 
 * options must be set, or the same values set using the mutator methods provided. The length 
 * of the <code>INPUT_VALUE_SETS</code> array should match the length of the 
 * <code>EXPECTED_OUTPUTS</code> array and the number of values in each set should match the 
 * length of the <code>INPUT_IDENTIFIERS</code> array.
 * 
 * @since 2.0
 */
public class HitsCount extends GEFitnessFunction implements Listener<ConfigEvent> {

	/**
	 * The key for setting the expected output values from the programs being evaluated
	 */
	public static final ConfigKey<Object[]> EXPECTED_OUTPUTS = new ConfigKey<Object[]>();
	
	/**
	 * The key for setting the acceptable error for each point to count as a hit
	 */
	public static final ConfigKey<Double> POINT_ERROR = new ConfigKey<Double>();

	// Configuration settings
	private Interpreter<GEIndividual> interpreter;
	private Object[] expectedOutputs;
	private String[] argNames;
	private Object[][] inputValueSets;
	private Double pointError;
	private Double malformedPenalty;
	
	/**
	 * Constructs a <code>HitsCount</code> fitness function with control parameters
	 * automatically loaded from the config.
	 */
	public HitsCount() {
		this(true);
	}
	
	/**
	 * Constructs a <code>HitsCount</code> fitness function with control parameters initially
	 * loaded from the config. If the <code>autoConfig</code> argument is set to <code>true</code> 
	 * then the configuration will be automatically updated when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public HitsCount(boolean autoConfig) {
		// Default config values
		malformedPenalty = Double.MAX_VALUE;
		pointError = 0.0;
		
		setup();

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link #INPUT_IDENTIFIERS}
	 * <li>{@link #INPUT_VALUE_SETS}
	 * <li>{@link #EXPECTED_OUTPUTS}
	 * <li>{@link #POINT_ERROR}
	 * <li>{@link GEFitnessFunction#INTERPRETER}
	 * <li>{@link #MALFORMED_PENALTY}
	 * </ul>
	 */
	protected void setup() {
		argNames = Config.getInstance().get(INPUT_IDENTIFIERS);
		inputValueSets = Config.getInstance().get(INPUT_VALUE_SETS);
		expectedOutputs = Config.getInstance().get(EXPECTED_OUTPUTS);
		pointError = Config.getInstance().get(POINT_ERROR, pointError);
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
		if (event.isKindOf(TEMPLATE, INPUT_IDENTIFIERS, INPUT_VALUE_SETS, EXPECTED_OUTPUTS, POINT_ERROR, INTERPRETER, MALFORMED_PENALTY)) {
			setup();
		}
	}
	
	/**
	 * Calculates the fitness of the given individual. This fitness function only operates
	 * on <code>GEIndividual</code>s. The fitness returned will be an instance of 
	 * <code>DoubleFitness.Minimise</code>. The fitness score is a count of the number of sets 
	 * of inputs that produce a correct result (or 'hit'). For double types a hit can have an 
	 * error range, specified by the {@link HitsCount#POINT_ERROR} config key.
	 *  
	 * @param individual the program to evaluate
	 * @return the fitness of the given individual
	 * @throws IllegalArgumentException if the individual is not a GEIndividual
	 */
	@Override
	public DoubleFitness.Minimise evaluate(Individual individual) {
		if (!(individual instanceof GEIndividual)) {
			throw new IllegalArgumentException("Unsupported representation");
		}
		
		GEIndividual program = (GEIndividual) individual;		
		
		Object[] results;
		try {
			results = interpreter.eval(program, argNames, inputValueSets);
		} catch (MalformedProgramException e) {
			return new DoubleFitness.Minimise(malformedPenalty);
		}
		
		double noWrong = 0.0;
		for (int i = 0; i < expectedOutputs.length; i++) {
			Object result = results[i];

			if (!isHit(result, expectedOutputs[i])) {
				noWrong++;
			}
		}

		return new DoubleFitness.Minimise(noWrong);
	}

	/**
	 * Decides whether a value returned by a program is considered to be a hit or not, when
	 * compared to the expected result.
	 * 
	 * @param result the result returned by the program
	 * @param expectedResult the correct result
	 * @return true if the result is considered to be a hit and false otherwise
	 */
	protected boolean isHit(Object result, Object expectedResult) {
		if (result instanceof Double && expectedResult instanceof Double) {
			Double dblResult = (Double) result;
			Double dblExpectedResult = (Double) expectedResult;
			
			if (dblResult == Double.NaN && dblExpectedResult == Double.NaN) {
				return true;
			} else {
				double error = Math.abs(dblResult - dblExpectedResult);
				return (error != Double.NaN && error <= pointError);
			}
		} else {
			return result.equals(expectedResult);
		}
	}
	
	/**
	 * Returns the point error which defines the range allowable for double values to
	 * be considered a hit.
	 * 
	 * @return the point error for a double value to be considered a hit
	 */
	public double getPointError() {
		return pointError;
	}
	
	/**
	 * Sets the point error which defines the range allowable for double values to be
	 * considered a hit. This is unused for non-Double fitness values.
	 * 
	 * If automatic configuration is enabled then any value set here will be overwritten 
	 * by the {@link #POINT_ERROR} configuration setting on the next config event.
	 * 
	 * @param pointError the point error for a double value to be considered a hit
	 */
	public void setPointError(double pointError) {
		this.pointError = pointError;
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
	public Object[] getExpectedOutputs() {
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
	public void setExpectedOutputs(Object[] expectedOutputs) {
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
