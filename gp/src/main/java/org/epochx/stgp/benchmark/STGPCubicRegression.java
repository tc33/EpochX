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
package org.epochx.stgp.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epochx.Breeder;
import org.epochx.Config.ConfigKey;
import org.epochx.DoubleFitness;
import org.epochx.EvolutionaryStrategy;
import org.epochx.FitnessEvaluator;
import org.epochx.GenerationalTemplate;
import org.epochx.Initialiser;
import org.epochx.MaximumGenerations;
import org.epochx.Operator;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.TerminationCriteria;
import org.epochx.TerminationFitness;
import org.epochx.epox.Node;
import org.epochx.epox.Variable;
import org.epochx.epox.VariableNode;
import org.epochx.epox.math.AddFunction;
import org.epochx.epox.math.DivisionProtectedFunction;
import org.epochx.epox.math.MultiplyFunction;
import org.epochx.epox.math.SubtractFunction;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.stgp.STGPIndividual;
import org.epochx.stgp.fitness.HitsCount;
import org.epochx.stgp.init.FullInitialisation;
import org.epochx.stgp.operator.SubtreeCrossover;
import org.epochx.stgp.operator.SubtreeMutation;
import org.epochx.tools.BenchmarkSolutions;

/**
 * This template sets up EpochX to run the cubic regression benchmark with
 * the STGP representation. Cubic regression involves evolving an equivalent
 * function to the formula: x + x^2 + x^3
 * 
 * The following configuration is used:
 * 
 * <li>Population.SIZE: 100
 * <li>MaximumGenerations.MAXIMUM_GENERATIONS: 50
 */
public class STGPCubicRegression extends GenerationalTemplate {

	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		super.fill(template);
		
        template.put(Population.SIZE, 100);
        List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
        criteria.add(new TerminationFitness(new DoubleFitness.Minimise(0.0)));
        criteria.add(new MaximumGenerations());
        template.put(EvolutionaryStrategy.TERMINATION_CRITERIA, criteria);
        template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);
        template.put(STGPIndividual.MAXIMUM_DEPTH, 6);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new SubtreeCrossover());
        operators.add(new SubtreeMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(SubtreeCrossover.PROBABILITY, 1.0);
        template.put(SubtreeMutation.PROBABILITY, 0.0);
        template.put(Initialiser.METHOD, new FullInitialisation());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
        
        // Setup syntax
        Variable varX = new Variable("X", Double.class);
		Node[] syntax = new Node[]{
			new AddFunction(),
			new SubtractFunction(),
			new MultiplyFunction(),
			new DivisionProtectedFunction(),
			new VariableNode(varX)
		};
        template.put(STGPIndividual.SYNTAX, syntax);
        template.put(STGPIndividual.RETURN_TYPE, Double.class);
        
        // Generate inputs and expected outputs
        int noPoints = 20;
        Double[][] inputs = new Double[noPoints][1];
        Double[] expectedOutputs = new Double[noPoints];
        for (int i=0; i<noPoints; i++) {
        	// Inputs values between -1.0 and +1.0
        	inputs[i][0] = (randomSequence.nextDouble() * 2) - 1;
        	expectedOutputs[i] = BenchmarkSolutions.cubicRegression(inputs[i][0]);
        }
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new HitsCount());
        template.put(HitsCount.POINT_ERROR, 0.01);
        template.put(HitsCount.INPUT_VARIABLES, new Variable[]{varX});
        template.put(HitsCount.INPUT_VALUE_SETS, inputs);
        template.put(HitsCount.EXPECTED_OUTPUTS, expectedOutputs);
	}
}