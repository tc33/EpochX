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
package org.epochx.cfg.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epochx.Breeder;
import org.epochx.Config.ConfigKey;
import org.epochx.BranchedBreeder;
import org.epochx.EvolutionaryStrategy;
import org.epochx.FitnessEvaluator;
import org.epochx.GenerationalStrategy;
import org.epochx.GenerationalTemplate;
import org.epochx.Initialiser;
import org.epochx.MaximumGenerations;
import org.epochx.Operator;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.TerminationCriteria;
import org.epochx.TerminationFitness;
import org.epochx.cfg.CFGIndividual;
import org.epochx.cfg.CFGSourceGenerator;
import org.epochx.cfg.fitness.CFGFitnessFunction;
import org.epochx.cfg.fitness.HitsCount;
import org.epochx.cfg.init.Grow;
import org.epochx.cfg.operator.SubtreeCrossover;
import org.epochx.cfg.operator.SubtreeMutation;
import org.epochx.fitness.DoubleFitness;
import org.epochx.grammar.Grammar;
import org.epochx.interpret.EpoxInterpreter;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.tools.BenchmarkSolutions;

/**
 * This template sets up EpochX to run the sextic regression benchmark with
 * the GE representation. Sextic regression involves evolving an equivalent
 * function to the formula: x^6 - (2 * x^4) + x^2
 * 
 * The following configuration is used:
 * 
 * <ul>
 * <li>{@link Population#SIZE}: <code>100</code>
 * <li>{@link GenerationalStrategy#TERMINATION_CRITERIA}: <code>MaximumGenerations</code>, <code>TerminationFitness(0.0)</code>
 * <li>{@link MaximumGenerations#MAXIMUM_GENERATIONS}: <code>50</code>
 * <li>{@link GEIndividual#MAXIMUM_DEPTH}: <code>17</code>
 * <li>{@link BranchedBreeder#SELECTOR}: <code>TournamentSelector</code>
 * <li>{@link TournamentSelector#TOURNAMENT_SIZE}: <code>7</code>
 * <li>{@link Breeder#OPERATORS}: <code>OnePointCrossover</code>, <code>PointMutation</code>
 * <li>{@link OnePointCrossover#PROBABILITY}: <code>0.0</code>
 * <li>{@link PointMutation#PROBABILITY}: <code>1.0</code>
 * <li>{@link Initialiser#METHOD}: <code>GrowInitialiser</code>
 * <li>{@link RandomSequence#RANDOM_SEQUENCE}: <code>MersenneTwisterFast</code>
 * <li>{@link Grammar#GRAMMER}: [Listed below]
 * <li>{@link CodonFactory#CODON_FACTORY}: <code>IntegerCodonFactory</code>
 * <li>{@link CFGFitnessFunction#INTERPRETER}: <code>EpoxInterpreter(GESourceGenerator)</code>
 * <li>{@link MappingComponent#MAPPER}: <code>DepthFirstMapper</code>
 * <li>{@link FitnessEvaluator#FUNCTION}: <code>HitsCount</code>
 * <li>{@link HitsCount#POINT_ERROR}: <code>0.01</code>
 * <li>{@link HitsCount#INPUT_IDENTIFIERS}: <code>new String[]{"X"}</code>
 * <li>{@link HitsCount#INPUT_VALUE_SETS}: [20 random values between -1.0 and +1.0]
 * <li>{@link HitsCount#EXPECTED_OUTPUTS}: [correct output for input value sets]
 * 
 * <h3>Grammar</h3>
 * 
 * {@code
 * <prog> ::= <node>
 * <node> ::= <function> | <terminal>
 * <function> ::= ADD( <node> , <node> )
 * 		| SUB( <node> , <node> )
 * 		| MUL( <node> , <node> )
 * 		| PDIV( <node> , <node> )
 * <terminal> ::= X
 * }
 * 
 * @since 2.0
 */
public class SexticRegression extends GenerationalTemplate {

	/**
	 * Sets up the given template with the benchmark config settings
	 * 
	 * @param template a map to be filled with the template config
	 */
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		super.fill(template);
		
        template.put(Population.SIZE, 100);
        List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
        criteria.add(new TerminationFitness(new DoubleFitness.Minimise(0.0)));
        criteria.add(new MaximumGenerations());
        template.put(EvolutionaryStrategy.TERMINATION_CRITERIA, criteria);
        template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);
        template.put(CFGIndividual.MAXIMUM_DEPTH, 17);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new SubtreeCrossover());
        operators.add(new SubtreeMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(SubtreeCrossover.PROBABILITY, 0.0);
        template.put(SubtreeMutation.PROBABILITY, 1.0);
        template.put(Initialiser.METHOD, new Grow());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
        
        // Setup grammar
        String grammarStr = "<prog> ::= <node>\n"
    			+ "<node> ::= <function> | <terminal>\n"
    			+ "<function> ::= ADD( <node> , <node> ) "
    			+ "| SUB( <node> , <node> ) "
    			+ "| MUL( <node> , <node> ) "
    			+ "| PDIV( <node> , <node> )\n"
    			+ "<terminal> ::= X\n";
        Grammar grammar = new Grammar(grammarStr);
        template.put(Grammar.GRAMMAR, grammar);
        template.put(CFGFitnessFunction.INTERPRETER, new EpoxInterpreter<CFGIndividual>(new CFGSourceGenerator()));
        
        // Generate inputs and expected outputs
        int noPoints = 20;
        Double[][] inputs = new Double[noPoints][1];
        Double[] expectedOutputs = new Double[noPoints];
        for (int i=0; i<noPoints; i++) {
        	// Inputs values between -1.0 and +1.0
        	inputs[i][0] = (randomSequence.nextDouble() * 2) - 1;
        	expectedOutputs[i] = BenchmarkSolutions.sexticRegression(inputs[i][0]);
        }
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new HitsCount());
        template.put(HitsCount.POINT_ERROR, 0.01);
        template.put(HitsCount.INPUT_IDENTIFIERS, new String[]{"X"});
        template.put(HitsCount.INPUT_VALUE_SETS, inputs);
        template.put(HitsCount.EXPECTED_OUTPUTS, expectedOutputs);
	}
}
