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
package org.epochx.stgp.fitness;

import static org.epochx.Config.Template.TEMPLATE;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.epochx.Config;
import org.epochx.Config.ConfigKey;
import org.epochx.DoubleFitness;
import org.epochx.Individual;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.stgp.STGPIndividual;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.ant.AntLandscape;


/**
 * A fitness function for <code>STGPIndividual</code>s that calculates and assigns 
 * <code>DoubleFitness.Minimise</code> scores. The program is executed until the maximum 
 * number of timesteps are used. The fitness score returned is the number of food items
 * that are <b>not</b> consumed by the controlled ant.
 * 
 * When using this fitness function the {@link #FOOD_LOCATIONS} and 
 * {@link #MAXIMUM_TIMESTEPS} config options must be set, or the same values set using the 
 * mutator methods provided.
 * 
 * @since 2.0
 */
public class AntFitnessFunction extends STGPFitnessFunction implements Listener<ConfigEvent> {

	/**
	 * The key for setting the food locations in the landscape
	 */
	public static final ConfigKey<List<Point>> FOOD_LOCATIONS = new ConfigKey<List<Point>>();
	
	/**
	 * The key for setting the maximum number of timesteps allowed for an ant
	 */
	public static final ConfigKey<Integer> MAXIMUM_TIMESTEPS = new ConfigKey<Integer>();
	
	// The ant environment
	private AntLandscape landscape;
	private Ant ant;
	
	// Configuration settings
	private List<Point> foodLocations;
	private Integer timesteps;
	
	/**
	 * Constructs a <code>AntFitnessFunction</code> fitness function with control parameters
	 * automatically loaded from the config.
	 * 
	 * @param ant the ant that is being controlled
	 * @param landscape the ant landscape that the ant is navigating
	 */
	public AntFitnessFunction(Ant ant, AntLandscape landscape) {
		this(ant, landscape, true);
	}
	
	/**
	 * Constructs a <code>AntFitnessFunction</code> fitness function with control parameters 
	 * initially loaded from the config. If the <code>autoConfig</code> argument is set to 
	 * <code>true</code> then the configuration will be automatically updated when the config 
	 * is modified.
	 * 
	 * @param ant the ant that is being controlled
	 * @param landscape the ant landscape that the ant is navigating
	 * @param autoConfig whether this fitness function should automatically update its
	 *        configuration settings from the config
	 */
	public AntFitnessFunction(Ant ant, AntLandscape landscape, boolean autoConfig) {
		setup();
		
		this.ant = ant;
		this.landscape = landscape;

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link #FOOD_LOCATIONS}
	 * <li>{@link #MAXIMUM_TIMESTEPS}
	 * </ul>
	 */
	protected void setup() {
		foodLocations = Config.getInstance().get(FOOD_LOCATIONS);
		timesteps = Config.getInstance().get(MAXIMUM_TIMESTEPS);
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
		if (event.isKindOf(TEMPLATE, FOOD_LOCATIONS, MAXIMUM_TIMESTEPS)) {
			setup();
		}
	}
	
	/**
	 * Calculates the fitness of the given individual. This fitness function only operates
	 * on STGPIndividuals. The fitness returned will be an instance of DoubleFitness.Minimise. 
	 * The fitness score is a count of the number of food items left in the landscape after
	 * the given individual is executed for the number of timesteps specified by the 
	 * {@link #MAXIMUM_TIMESTEPS} config key.
	 * 
	 * @param individual the program to evaluate
	 * @return the fitness of the given individual
	 * @throws IllegalArgumentException if the individual is not an STGPIndividual
	 */
	@Override
	public DoubleFitness.Minimise evaluate(Individual individual) {
		if (!(individual instanceof STGPIndividual)) {
			throw new IllegalArgumentException("Unsupported representation");
		}
		
		STGPIndividual program = (STGPIndividual) individual;
		
		landscape.setFoodLocations(new ArrayList<Point>(foodLocations));
		ant.reset(timesteps, landscape);

		while (ant.getTimesteps() < ant.getMaxMoves()) {
			program.evaluate();
		}

		return new DoubleFitness.Minimise(foodLocations.size() - ant.getFoodEaten());
	}
	
	/**
	 * Returns the location of the food items in the ant landscape
	 * 
	 * @return the location of the food items
	 */
	public List<Point> getFoodLocations() {
		return foodLocations;
	}
	
	/**
	 * Sets the location of food items in the ant landscape.
	 * 
	 * If automatic configuration is enabled then any value set here will be 
	 * overwritten by the {@link #FOOD_LOCATIONS} configuration setting on the 
	 * next config event.
	 * 
	 * @param foodLocations the location of food items in the ant landscape
	 */
	public void setFoodLocations(List<Point> foodLocations) {
		this.foodLocations = foodLocations;
	}
	
	/**
	 * Returns the maximum number of timesteps the ant will be allowed
	 * 
	 * @return the maximum number of timesteps
	 */
	public int getMaximumTimesteps() {
		return timesteps;
	}
	
	/**
	 * Sets the maximum number of timesteps the ant will be allowed.
	 * 
	 * If automatic configuration is enabled then any value set here will be 
	 * overwritten by the {@link #MAXIMUM_TIMESTEPS} configuration setting on the 
	 * next config event.
	 * 
	 * @param timesteps the maximum number of timesteps
	 */
	public void setMaximumTimesteps(int timesteps) {
		this.timesteps = timesteps;
	}
}
