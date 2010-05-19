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
package org.epochx.stats;

import org.epochx.core.Model;
import org.epochx.gp.model.GPModelDummy;

import junit.framework.TestCase;

/**
 * 
 */
public class StatsManagerTest extends TestCase {

	private StatsManager statsManager;
	private Model model;
	
	@Override
	protected void setUp() throws Exception {
		model = new GPModelDummy();
		statsManager = new StatsManager(model);
	}
	
	/**
	 * Test that if a run statistic is not found then null is returned.
	 */
	public void testRunStatNotFound() {
		String field = "runtest";
		assertNull("null not returned for missing run stat", statsManager.getRunStat(field));
	}
	
	/**
	 * Test that if a generation statistic is not found then null is returned.
	 */
	public void testGenStatNotFound() {
		String field = "gentest";
		assertNull("null not returned for missing generation stat", statsManager.getGenerationStat(field));
	}
	
	/**
	 * Test that if a crossover statistic is not found then null is returned.
	 */
	public void testCrossoverStatNotFound() {
		String field = "xotest";
		assertNull("null not returned for missing crossover stat", statsManager.getCrossoverStat(field));
	}
	
	/**
	 * Test that if a mutation statistic is not found then null is returned.
	 */
	public void testMutationStatNotFound() {
		String field = "muttest";
		assertNull("null not returned for missing mutation stat", statsManager.getMutationStat(field));
	}
	
	/**
	 * Tests that a run statistic that is added to a stats manager is 
	 * remembered.
	 */
	public void testAddRunStat() {
		String field = "runtest";
		String value = "value";
		statsManager.addRunData(field, value);
		assertSame("stats manager not storing run stats", value, statsManager.getRunStat(field));		
	}
	
	/**
	 * Tests that a generation statistic that is added to a stats manager is 
	 * remembered.
	 */
	public void testAddGenerationStat() {
		String field = "gentest";
		String value = "value";
		statsManager.addGenerationData(field, value);
		assertSame("stats manager not storing generation stats", value, statsManager.getGenerationStat(field));		
	}
	
	/**
	 * Tests that a crossover statistic that is added to a stats manager is 
	 * remembered.
	 */
	public void testAddCrossoverStat() {
		String field = "xotest";
		String value = "value";
		statsManager.addCrossoverData(field, value);
		assertSame("stats manager not storing crossover stats", value, statsManager.getCrossoverStat(field));		
	}
	
	/**
	 * Tests that a mutation statistic that is added to a stats manager is 
	 * remembered.
	 */
	public void testAddMutationStat() {
		String field = "muttest";
		String value = "value";
		statsManager.addMutationData(field, value);
		assertSame("stats manager not storing mutation stats", value, statsManager.getMutationStat(field));		
	}
	
	/**
	 * Tests that run stats get cleared at the start of a new run.
	 */
	public void testRunStatsCleared() {
		String field = "runtest";
		String value = "value";
		statsManager.addRunData(field, value);
		model.getLifeCycleManager().fireRunStartEvent();
		assertNull("run stats not cleared when a run start event is fired", statsManager.getRunStat(field));
	}
	
	/**
	 * Tests that generation stats get cleared at the start of a new generation.
	 */
	public void testGenerationStatsCleared() {
		String field = "gentest";
		String value = "value";
		statsManager.addGenerationData(field, value);
		model.getLifeCycleManager().fireGenerationStartEvent();
		assertNull("generation stats not cleared when a generation start event is fired", statsManager.getGenerationStat(field));
	}
	
	/**
	 * Tests that crossover stats get cleared at the start of a new crossover.
	 */
	public void testCrossoverStatsCleared() {
		String field = "crossovertest";
		String value = "value";
		statsManager.addCrossoverData(field, value);
		model.getLifeCycleManager().fireCrossoverStartEvent();
		assertNull("crossover stats not cleared when a crossover start event is fired", statsManager.getCrossoverStat(field));
	}
	
	/**
	 * Tests that mutation stats get cleared at the start of a new mutation.
	 */
	public void testMutationStatsCleared() {
		String field = "mutationtest";
		String value = "value";
		statsManager.addMutationData(field, value);
		model.getLifeCycleManager().fireMutationStartEvent();
		assertNull("mutation stats not cleared when a mutation start event is fired", statsManager.getMutationStat(field));
	}
}
