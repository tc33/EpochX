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
package org.epochx.ge.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epochx.Breeder;
import org.epochx.Config.ConfigKey;
import org.epochx.BranchedBreeder;
import org.epochx.DoubleFitness;
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
import org.epochx.ge.CodonFactory;
import org.epochx.ge.GEIndividual;
import org.epochx.ge.GESourceGenerator;
import org.epochx.ge.IntegerCodonFactory;
import org.epochx.ge.fitness.GEFitnessFunction;
import org.epochx.ge.fitness.HitsCount;
import org.epochx.ge.init.GrowInitialisation;
import org.epochx.ge.map.DepthFirstMapper;
import org.epochx.ge.map.MappingComponent;
import org.epochx.ge.operator.OnePointCrossover;
import org.epochx.ge.operator.PointMutation;
import org.epochx.grammar.Grammar;
import org.epochx.interpret.EpoxInterpreter;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.tools.BenchmarkSolutions;
import org.epochx.tools.BooleanUtils;

/**
 * This template sets up EpochX to run the 11-bit multiplexer benchmark with the 
 * GE representation. The 11-bit multiplexer problem involves evolving a program
 * which receives an array of 11 boolean values. The first 3 values are address bits, 
 * which the program should convert into an index for which of the remaining data
 * registers to return. {a0, a1, a2, d0, d1, d2, d3, d4, d5, d6, d7}.
 * 
 * <table>
 * <tr>
 * <td>a0</td><td>a1</td><td>a2</td><td>return value</td>
 * <td>false</td><td>false</td><td>false</td><td>d0</td>
 * <td>true</td><td>false</td><td>false</td><td>d1</td>
 * <td>false</td><td>true</td><td>false</td><td>d2</td>
 * <td>true</td><td>true</td><td>false</td><td>d3</td>
 * <td>false</td><td>false</td><td>true</td><td>d4</td>
 * <td>true</td><td>false</td><td>true</td><td>d5</td>
 * <td>false</td><td>true</td><td>true</td><td>d6</td>
 * <td>true</td><td>true</td><td>true</td><td>d7</td>
 * </tr>
 * </table>
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
 * <li>{@link GEFitnessFunction#INTERPRETER}: <code>EpoxInterpreter(GESourceGenerator)</code>
 * <li>{@link MappingComponent#MAPPER}: <code>DepthFirstMapper</code>
 * <li>{@link FitnessEvaluator#FUNCTION}: <code>HitsCount</code>
 * <li>{@link HitsCount#INPUT_IDENTIFIERS}: <code>new String[]{"A0", "A1", "A2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10"}</code>
 * <li>{@link HitsCount#INPUT_VALUE_SETS}: [all possible binary input combinations]
 * <li>{@link HitsCount#EXPECTED_OUTPUTS}: [correct output for input value sets]
 * 
 * <h3>Grammar</h3>
 * 
 * {@code
 * <prog> ::= <node>
 * <node> ::= <function> | <terminal>
 * <function> ::= NOT( <node> )
 * 		| OR( <node> , <node> )
 * 		| AND( <node> , <node> )
 * 		| IF( <node> , <node>, <node> )
 * <terminal> ::= A0 | A1 | A2 | D3 | D4 | D5 | D6 | D7 | D8 | D9 | D10
 * }
 * 
 * @since 2.0
 */
public class GEMultiplexer11Bit extends GenerationalTemplate {
	
	private static final int NO_BITS = 11;
	
	/**
	 * Sets up the given template with the benchmark config settings
	 * 
	 * @param template a map to be filled with the template config
	 */
	@Override
	protected void fill(Map<ConfigKey<?>, Object> template) {
		super.fill(template);
		
		int noAddressBits = BenchmarkSolutions.multiplexerAddressBits(NO_BITS);
		
        template.put(Population.SIZE, 100);
        List<TerminationCriteria> criteria = new ArrayList<TerminationCriteria>();
        criteria.add(new TerminationFitness(new DoubleFitness.Minimise(0.0)));
        criteria.add(new MaximumGenerations());
        template.put(EvolutionaryStrategy.TERMINATION_CRITERIA, criteria);
        template.put(MaximumGenerations.MAXIMUM_GENERATIONS, 50);
        template.put(GEIndividual.MAXIMUM_DEPTH, 17);
        
        template.put(Breeder.SELECTOR, new TournamentSelector());
        template.put(TournamentSelector.TOURNAMENT_SIZE, 7);        
        List<Operator> operators = new ArrayList<Operator>();
        operators.add(new OnePointCrossover());
        operators.add(new PointMutation());
        template.put(Breeder.OPERATORS, operators);
        template.put(OnePointCrossover.PROBABILITY, 0.0);
        template.put(PointMutation.PROBABILITY, 1.0);
        template.put(Initialiser.METHOD, new GrowInitialisation());
        
        RandomSequence randomSequence = new MersenneTwisterFast();
        template.put(RandomSequence.RANDOM_SEQUENCE, randomSequence);
		
        // Setup grammar
        String grammarStr = "<prog> ::= <node>\n"
    			+ "<node> ::= <function> | <terminal>\n"
    			+ "<function> ::= NOT( <node> ) "
    			+ "| OR( <node> , <node> ) "
    			+ "| AND( <node> , <node> ) "
    			+ "| IF( <node> , <node> , <node> )\n"
    			+ "<terminal> ::= ";
        
        // Append inputs to grammar
		String[] inputIdentifiers = new String[NO_BITS];
		for (int i=0; i < noAddressBits; i++) {
			if (i > 0) {
				grammarStr += " | ";
			}
			inputIdentifiers[i] = "A" + i;
			grammarStr += inputIdentifiers[i];
		}
		for (int i = noAddressBits; i < NO_BITS; i++) {
			grammarStr += " | ";
			inputIdentifiers[i] = "D" + i;
			grammarStr += inputIdentifiers[i];
		}
		grammarStr += '\n';

        template.put(Grammar.GRAMMAR, new Grammar(grammarStr));
        template.put(CodonFactory.CODON_FACTORY, new IntegerCodonFactory());
        template.put(GEFitnessFunction.INTERPRETER, new EpoxInterpreter<GEIndividual>(new GESourceGenerator()));
        template.put(MappingComponent.MAPPER, new DepthFirstMapper());
        
        // Generate inputs and expected outputs
        Boolean[][] inputValues = BooleanUtils.generateBoolSequences(NO_BITS);
        Boolean[] expectedOutputs = new Boolean[inputValues.length];
        for (int i=0; i<inputValues.length; i++) {
        	expectedOutputs[i] = BenchmarkSolutions.multiplexer(inputValues[i], noAddressBits);
        }
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new HitsCount());
        template.put(HitsCount.INPUT_IDENTIFIERS, inputIdentifiers);
        template.put(HitsCount.INPUT_VALUE_SETS, inputValues);
        template.put(HitsCount.EXPECTED_OUTPUTS, expectedOutputs);
	}
}
