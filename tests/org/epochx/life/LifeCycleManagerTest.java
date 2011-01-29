/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
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
package org.epochx.life;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.epochx.representation.CandidateProgram;
import org.junit.*;

/**
 * 
 */
public class LifeCycleManagerTest {

	@Before
	public void setUp() {
		evolver.getLife().clearListeners();
		evolver.getLife().clearHooks();
	}

	/**
	 * Tests that configure event is dispatched correctly.
	 */
	@Test
	public void testConfigureEvent() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addConfigListener(new ConfigListener() {

			@Override
			public void onConfigure() {
				order.append("1");
			}
		});

		evolver.getLife().fireConfigureEvent();

		assertEquals("configure event not dispatched correctly", "1", order.toString());
	}

	/**
	 * Tests that run events are dispatched correctly.
	 */
	@Test
	public void testRunEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addRunListener(new RunListener() {

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

		evolver.getLife().fireRunStartEvent();
		evolver.getLife().fireSuccessEvent();
		evolver.getLife().fireRunEndEvent();

		assertEquals("run events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that initialisation events are dispatched correctly.
	 */
	@Test
	public void testInitialisationEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addInitialisationListener(new InitialisationListener() {

			@Override
			public void onInitialisationStart() {
				order.append("1");
			}

			@Override
			public void onInitialisationEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public List<CandidateProgram> initialisationHook(final List<CandidateProgram> pop) {
				order.append("2");
				return pop;
			}
		});

		evolver.getLife().fireInitialisationStartEvent();
		evolver.getLife().runInitialisationHooks(null);
		evolver.getLife().fireInitialisationEndEvent();

		assertEquals("initialisation events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that elitism events are dispatched correctly.
	 */
	@Test
	public void testElitismEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addElitismListener(new ElitismListener() {

			@Override
			public void onElitismStart() {
				order.append("1");
			}

			@Override
			public void onElitismEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public List<CandidateProgram> elitismHook(final List<CandidateProgram> elites) {
				order.append("2");
				return elites;
			}
		});

		evolver.getLife().fireElitismStartEvent();
		evolver.getLife().runElitismHooks(new ArrayList<CandidateProgram>());
		evolver.getLife().fireElitismEndEvent();

		assertEquals("elitism events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that pool selection events are dispatched correctly.
	 */
	@Test
	public void testPoolSelectionEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addPoolSelectionListener(new PoolSelectionListener() {

			@Override
			public void onPoolSelectionStart() {
				order.append("1");
			}

			@Override
			public void onPoolSelectionEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public List<CandidateProgram> poolSelectionHook(final List<CandidateProgram> pool) {
				order.append("2");
				return pool;
			}
		});

		evolver.getLife().firePoolSelectionStartEvent();
		evolver.getLife().runPoolSelectionHooks(new ArrayList<CandidateProgram>());
		evolver.getLife().firePoolSelectionEndEvent();

		assertEquals("pool selection events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that crossover events are dispatched correctly.
	 */
	@Test
	public void testCrossoverEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addCrossoverListener(new CrossoverListener() {

			@Override
			public void onCrossoverStart() {
				order.append("1");
			}

			@Override
			public void onCrossoverEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public CandidateProgram[] crossoverHook(final CandidateProgram[] parents, final CandidateProgram[] children) {
				order.append("2");
				return children;
			}
		});

		evolver.getLife().fireCrossoverStartEvent();
		evolver.getLife().runCrossoverHooks(null, null);
		evolver.getLife().fireCrossoverEndEvent();

		assertEquals("crossover events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that mutation events are dispatched correctly.
	 */
	@Test
	public void testMutationEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addMutationListener(new MutationListener() {

			@Override
			public void onMutationStart() {
				order.append("1");
			}

			@Override
			public void onMutationEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public CandidateProgram mutationHook(final CandidateProgram parent, final CandidateProgram child) {
				order.append("2");
				return child;
			}
		});

		evolver.getLife().fireMutationStartEvent();
		evolver.getLife().runMutationHooks(null, null);
		evolver.getLife().fireMutationEndEvent();

		assertEquals("mutation events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that reproduction events are dispatched correctly.
	 */
	@Test
	public void testReproductionEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addReproductionListener(new ReproductionListener() {

			@Override
			public void onReproductionStart() {
				order.append("1");
			}

			@Override
			public void onReproductionEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public CandidateProgram reproductionHook(final CandidateProgram program) {
				order.append("2");
				return program;
			}
		});

		evolver.getLife().fireReproductionStartEvent();
		evolver.getLife().runReproductionHooks(null);
		evolver.getLife().fireReproductionEndEvent();

		assertEquals("reproduction events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that generation events are dispatched correctly.
	 */
	@Test
	public void testGenerationEvents() {
		final StringBuilder order = new StringBuilder();

		evolver.getLife().addGenerationListener(new GenerationListener() {

			@Override
			public void onGenerationStart() {
				order.append("1");
			}

			@Override
			public void onGenerationEnd() {
				order.append("3");
			}
		});
		evolver.getLife().addHook(new AbstractHook() {

			@Override
			public List<CandidateProgram> generationHook(final List<CandidateProgram> pop) {
				order.append("2");
				return pop;
			}
		});

		evolver.getLife().fireGenerationStartEvent();
		evolver.getLife().runGenerationHooks(null);
		evolver.getLife().fireGenerationEndEvent();

		assertEquals("generation events not dispatched correctly", "123", order.toString());
	}
}
