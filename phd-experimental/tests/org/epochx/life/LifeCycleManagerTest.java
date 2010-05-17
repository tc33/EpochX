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
package org.epochx.life;

import java.util.*;

import org.epochx.representation.CandidateProgram;

import junit.framework.TestCase;

/**
 * 
 */
public class LifeCycleManagerTest extends TestCase {

	private LifeCycleManager lifeManager;
	
	@Override
	protected void setUp() throws Exception {
		lifeManager = new LifeCycleManager();
	}
	
	/**
	 * Tests that configure event is dispatched correctly.
	 */
	public void testConfigureEvent() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addConfigListener(new ConfigListener() {
			@Override
			public void onConfigure() {
				order.append("1");
			}
		});
		
		lifeManager.fireConfigureEvent();
		
		assertEquals("configure event not dispatched correctly", "1", order.toString());
	}
	
	/**
	 * Tests that run events are dispatched correctly.
	 */
	public void testRunEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addRunListener(new RunListener() {
			@Override
			public void onRunStart() {
				order.append("1");
			}
			@Override
			public void onSuccess() {
				order.append("2");
			}
			@Override
			public void onRunEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireRunStartEvent();
		lifeManager.fireSuccessEvent();
		lifeManager.fireRunEndEvent();
		
		assertEquals("run events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that initialisation events are dispatched correctly.
	 */
	public void testInitialisationEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addInitialisationListener(new InitialisationListener() {
			@Override
			public void onInitialisationStart() {
				order.append("1");
			}
			@Override
			public List<CandidateProgram> onInitialisation(List<CandidateProgram> pop) {
				order.append("2");
				return pop;
			}
			@Override
			public void onInitialisationEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireInitialisationStartEvent();
		lifeManager.fireInitialisationEvent(null);
		lifeManager.fireInitialisationEndEvent();
		
		assertEquals("initialisation events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that elitism events are dispatched correctly.
	 */
	public void testElitismEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addElitismListener(new ElitismListener() {
			@Override
			public void onElitismStart() {
				order.append("1");
			}
			@Override
			public List<CandidateProgram> onElitism(List<CandidateProgram> elites) {
				order.append("2");
				return elites;
			}
			@Override
			public void onElitismEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireElitismStartEvent();
		lifeManager.fireElitismEvent(new ArrayList<CandidateProgram>());
		lifeManager.fireElitismEndEvent();
		
		assertEquals("elitism events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that pool selection events are dispatched correctly.
	 */
	public void testPoolSelectionEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addPoolSelectionListener(new PoolSelectionListener() {
			@Override
			public void onPoolSelectionStart() {
				order.append("1");
			}
			@Override
			public List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool) {
				order.append("2");
				return pool;
			}
			@Override
			public void onPoolSelectionEnd() {
				order.append("3");
			}
		});
		
		lifeManager.firePoolSelectionStartEvent();
		lifeManager.firePoolSelectionEvent(new ArrayList<CandidateProgram>());
		lifeManager.firePoolSelectionEndEvent();
		
		assertEquals("pool selection events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that crossover events are dispatched correctly.
	 */
	public void testCrossoverEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addCrossoverListener(new CrossoverListener() {
			@Override
			public void onCrossoverStart() {
				order.append("1");
			}
			@Override
			public CandidateProgram[] onCrossover(CandidateProgram[] parents, 
												CandidateProgram[] children) {
				order.append("2");
				return children;
			}
			@Override
			public void onCrossoverEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireCrossoverStartEvent();
		lifeManager.fireCrossoverEvent(null, null);
		lifeManager.fireCrossoverEndEvent();
		
		assertEquals("crossover events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that mutation events are dispatched correctly.
	 */
	public void testMutationEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addMutationListener(new MutationListener() {
			@Override
			public void onMutationStart() {
				order.append("1");
			}
			@Override
			public CandidateProgram onMutation(CandidateProgram parent, 
												CandidateProgram child) {
				order.append("2");
				return child;
			}
			@Override
			public void onMutationEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireMutationStartEvent();
		lifeManager.fireMutationEvent(null, null);
		lifeManager.fireMutationEndEvent();
		
		assertEquals("mutation events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that reproduction events are dispatched correctly.
	 */
	public void testReproductionEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addReproductionListener(new ReproductionListener() {
			@Override
			public void onReproductionStart() {
				order.append("1");
			}
			@Override
			public CandidateProgram onReproduction(CandidateProgram program) {
				order.append("2");
				return program;
			}
			@Override
			public void onReproductionEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireReproductionStartEvent();
		lifeManager.fireReproductionEvent(null);
		lifeManager.fireReproductionEndEvent();
		
		assertEquals("reproduction events not dispatched correctly", "123", order.toString());
	}
	
	/**
	 * Tests that generation events are dispatched correctly.
	 */
	public void testGenerationEvents() {
		final StringBuilder order = new StringBuilder();
		
		lifeManager.addGenerationListener(new GenerationListener() {
			@Override
			public void onGenerationStart() {
				order.append("1");
			}
			@Override
			public List<CandidateProgram> onGeneration(List<CandidateProgram> pop) {
				order.append("2");
				return pop;
			}
			@Override
			public void onGenerationEnd() {
				order.append("3");
			}
		});
		
		lifeManager.fireGenerationStartEvent();
		lifeManager.fireGenerationEvent(null);
		lifeManager.fireGenerationEndEvent();
		
		assertEquals("generation events not dispatched correctly", "123", order.toString());
	}
}
