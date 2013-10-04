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

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.epochx.epox.ant.AntMoveFunction;
import org.epochx.epox.ant.AntTurnLeftFunction;
import org.epochx.epox.ant.AntTurnRightFunction;
import org.epochx.epox.ant.IfFoodAheadFunction;
import org.epochx.epox.lang.Seq2Function;
import org.epochx.epox.lang.Seq3Function;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.selection.TournamentSelector;
import org.epochx.stgp.STGPIndividual;
import org.epochx.stgp.fitness.AntFitnessFunction;
import org.epochx.stgp.init.FullInitialisation;
import org.epochx.stgp.operator.SubtreeCrossover;
import org.epochx.stgp.operator.SubtreeMutation;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.ant.AntLandscape;

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
public class STGPJohnMuirTrail extends GenerationalTemplate {

	/**
	 * The points in the landscape that will be occupied by food.
	 */
	public static final List<Point> FOOD_LOCATIONS = Arrays.asList(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0),
			new Point(5, 0), new Point(6, 0), new Point(7, 0), new Point(8, 0), new Point(9, 0), new Point(10, 0),
			new Point(10, 1), new Point(10, 2), new Point(10, 3), new Point(10, 4), new Point(10, 5), new Point(10, 6),
			new Point(10, 7), new Point(10, 8), new Point(10, 9), new Point(10, 10), new Point(9, 10),
			new Point(8, 10), new Point(7, 10), new Point(6, 10), new Point(5, 10), new Point(4, 10), new Point(3, 10),
			new Point(3, 9), new Point(3, 8), new Point(3, 7), new Point(3, 6), new Point(3, 5), new Point(2, 5),
			new Point(1, 5), new Point(0, 5), new Point(31, 5), new Point(30, 5), new Point(29, 5), new Point(28, 5),
			new Point(27, 5), new Point(26, 5), new Point(25, 5), new Point(24, 6), new Point(24, 7), new Point(24, 8),
			new Point(24, 9), new Point(24, 10), new Point(23, 11), new Point(22, 11), new Point(21, 11),
			new Point(20, 11), new Point(19, 11), new Point(18, 12), new Point(18, 13), new Point(18, 14),
			new Point(18, 15), new Point(18, 16), new Point(18, 17), new Point(18, 20), new Point(18, 21),
			new Point(18, 22), new Point(18, 23), new Point(18, 24), new Point(18, 25), new Point(17, 27),
			new Point(16, 27), new Point(15, 27), new Point(14, 27), new Point(13, 27), new Point(12, 27),
			new Point(10, 27), new Point(9, 27), new Point(8, 27), new Point(7, 27), new Point(4, 27),
			new Point(4, 26), new Point(4, 25), new Point(4, 24), new Point(5, 22), new Point(7, 21), new Point(8, 18),
			new Point(11, 17), new Point(12, 15), new Point(15, 14), new Point(14, 12), new Point(11, 11),
			new Point(12, 8), new Point(14, 7), new Point(15, 4));
	
	private static final Dimension LANDSCAPE_SIZE = new Dimension(32, 32);
	
	private static final int MAXIMUM_TIMESTEPS = 100;
	
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
        AntLandscape landscape = new AntLandscape(LANDSCAPE_SIZE, null);
        Ant ant = new Ant(MAXIMUM_TIMESTEPS, landscape);
		Node[] syntax = new Node[]{
			new IfFoodAheadFunction(),
			new Seq2Function(),
			new Seq3Function(),
			new AntMoveFunction(ant),
			new AntTurnLeftFunction(ant),
			new AntTurnRightFunction(ant)
		};
        template.put(STGPIndividual.SYNTAX, syntax);
        template.put(STGPIndividual.RETURN_TYPE, Void.class);
        
        // Setup fitness function
        template.put(FitnessEvaluator.FUNCTION, new AntFitnessFunction(ant, landscape));
        template.put(AntFitnessFunction.FOOD_LOCATIONS, FOOD_LOCATIONS);
        template.put(AntFitnessFunction.MAXIMUM_TIMESTEPS, MAXIMUM_TIMESTEPS);
	}
}
