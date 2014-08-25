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
package org.epochx.cfg.benchmark.ant;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.epochx.cfg.CFGIndividual;
import org.epochx.cfg.CFGSourceGenerator;
import org.epochx.cfg.fitness.CFGFitnessFunction;
import org.epochx.cfg.init.Grow;
import org.epochx.cfg.operator.SubtreeCrossover;
import org.epochx.cfg.operator.SubtreeMutation;
import org.epochx.grammar.Grammar;
import org.epochx.interpret.EpoxInterpreter;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.ant.AntLandscape;

/**
 * This template sets up EpochX to run the Santa Fe ant trail benchmark with
 * the GE representation. 
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
 * <li>{@link FitnessEvaluator#FUNCTION}: <code>AntFitnessFunction</code>
 * <li>{@link FoodLocationCount#FOOD_LOCATIONS}: <code>GESantaFeTrail.FOOD_LOCATIONS</code>
 * <li>{@link FoodLocationCount#MAXIMUM_TIMESTEPS}: 600
 * 
 * <h3>Grammar</h3>
 * 
 * {@code
 * <function> ::= IF-FOOD-AHEAD( <var> , <function> , <function> ) 
 * 		| SEQ2( <function> , <function> )
 * 		| SEQ3( <function> , <function> , <function> )
 * 		| MOVE( <var> )
 * 		| TURN-LEFT( <var> )
 * 		| TURN-RIGHT( <var> )
 * <var> ::= ANT
 * }
 * 
 * @since 2.0
 */
public class SantaFeTrail extends GenerationalTemplate {

	/**
	 * The points in the landscape that will be occupied by food.
	 */
	public static final List<Point> FOOD_LOCATIONS = Arrays.asList(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(3, 1),
			new Point(3, 2), new Point(3, 3), new Point(3, 4), new Point(3, 5), new Point(4, 5), new Point(5, 5),
			new Point(6, 5), new Point(8, 5), new Point(9, 5), new Point(10, 5), new Point(11, 5), new Point(12, 5),
			new Point(12, 6), new Point(12, 7), new Point(12, 8), new Point(12, 9), new Point(12, 11),
			new Point(12, 12), new Point(12, 13), new Point(12, 14), new Point(12, 17), new Point(12, 18),
			new Point(12, 19), new Point(12, 20), new Point(12, 21), new Point(12, 22), new Point(12, 23),
			new Point(11, 24), new Point(10, 24), new Point(9, 24), new Point(8, 24), new Point(7, 24),
			new Point(4, 24), new Point(3, 24), new Point(1, 25), new Point(1, 26), new Point(1, 27), new Point(1, 28),
			new Point(2, 30), new Point(3, 30), new Point(4, 30), new Point(5, 30), new Point(7, 29), new Point(7, 28),
			new Point(8, 27), new Point(9, 27), new Point(10, 27), new Point(11, 27), new Point(12, 27),
			new Point(13, 27), new Point(14, 27), new Point(16, 26), new Point(16, 25), new Point(16, 24),
			new Point(16, 21), new Point(16, 20), new Point(16, 19), new Point(16, 18), new Point(17, 15),
			new Point(20, 14), new Point(20, 13), new Point(20, 10), new Point(20, 9), new Point(20, 8),
			new Point(20, 7), new Point(21, 5), new Point(22, 5), new Point(24, 4), new Point(24, 3), new Point(25, 2),
			new Point(26, 2), new Point(27, 2), new Point(29, 3), new Point(29, 4), new Point(29, 6), new Point(29, 9),
			new Point(29, 12), new Point(28, 14), new Point(27, 14), new Point(26, 14), new Point(23, 15),
			new Point(24, 18), new Point(27, 19), new Point(26, 22), new Point(23, 23));
	
	private static final Dimension LANDSCAPE_SIZE = new Dimension(32, 32);
	
	private static final int MAXIMUM_TIMESTEPS = 600;
	
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
        String grammarStr = "<function> ::= IF-FOOD-AHEAD( <var> , <function> , <function> ) "
			+ "| SEQ2( <function> , <function> ) "
			+ "| SEQ3( <function> , <function> , <function> ) "
			+ "| MOVE( <var> ) "
			+ "| TURN-LEFT( <var> ) "
			+ "| TURN-RIGHT( <var> )\n"
			+ "<var> ::= ANT";
        
        // Setup syntax
        AntLandscape landscape = new AntLandscape(LANDSCAPE_SIZE, null);
        Ant ant = new Ant(MAXIMUM_TIMESTEPS, landscape);
		
        template.put(Grammar.GRAMMAR, new Grammar(grammarStr));
        template.put(CFGFitnessFunction.INTERPRETER, new EpoxInterpreter<CFGIndividual>(new CFGSourceGenerator()));
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new FoodLocationCount(ant, landscape));
        template.put(FoodLocationCount.FOOD_LOCATIONS, FOOD_LOCATIONS);
        template.put(FoodLocationCount.MAXIMUM_TIMESTEPS, MAXIMUM_TIMESTEPS);
	}
}
