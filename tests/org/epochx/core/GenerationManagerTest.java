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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.core;

import static org.junit.Assert.*;

import java.util.*;

import org.epochx.gp.model.SantaFeTrail;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;
import org.junit.*;

/**
 * 
 */
public class GenerationManagerTest {

	private Model model;
	private GenerationManager genManager;
	private List<CandidateProgram> pop;
	private int count;

	@Before
	public void setUp() throws Exception {
		model = new Model() {

			@Override
			public double getFitness(final CandidateProgram program) {
				return 0;
			}
		};
		model.setNoElites(0);
		model.setPopulationSize(1);
		model.setCrossoverProbability(0.0);
		model.setMutationProbability(0.0);
		genManager = new GenerationManager(model);
		pop = new ArrayList<CandidateProgram>();
		pop.add(new CandidateProgram() {

			@Override
			public boolean isValid() {
				return true;
			}

			@Override
			public double getFitness() {
				return 0;
			}
		});

		Life.get().fireConfigureEvent();
	}

	/**
	 * Tests that trying to run a generation with a previous population of null
	 * throws an exception.
	 */
	@Test
	public void testGenerationPopNull() {
		pop.clear();
		try {
			genManager.generation(1, null);
			fail("illegal argument exception not thrown for null previous population");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that trying to run a generation with a previous population of size
	 * zero throws an exception.
	 */
	@Test
	public void testGenerationPopSizeZero() {
		pop.clear();
		try {
			genManager.generation(1, pop);
			fail("illegal argument exception not thrown for empty previous population");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the generation events are all triggered and in the correct
	 * order.
	 */
	@Test
	public void testGenerationEventsOrder() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		// Listen for the generation.
		Life.get().addGenerationListener(new GenerationListener() {

			@Override
			public void onGenerationStart() {
				verify.append('1');
			}

			@Override
			public void onGenerationEnd() {
				verify.append('3');
			}
		});
		Life.get().addHook(new AbstractHook() {

			@Override
			public List<CandidateProgram> generationHook(final List<CandidateProgram> genPop) {
				verify.append('2');
				return genPop;
			}
		});

		genManager.generation(1, pop);

		assertEquals("generation events were not called in the correct order", "123", verify.toString());
	}

	/**
	 * Tests that returning null to the generation event will revert the
	 * generation.
	 */
	@Test
	public void testGenerationEventRevert() {
		count = 0;

		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		// Listen for the generation.
		Life.get().addHook(new AbstractHook() {

			@Override
			public List<CandidateProgram> generationHook(final List<CandidateProgram> elites) {
				verify.append('3');
				// Revert 3 times before confirming.
				if (count == 3) {
					return elites;
				} else {
					count++;
				}
				return null;
			}
		});

		genManager.generation(1, pop);

		assertEquals("generation was not correctly reverted", "3333", verify.toString());
	}

	@Test
	public void testGenerationRNGNull() {
		model = new SantaFeTrail() {

			@Override
			public RandomNumberGenerator getRNG() {
				return super.getRNG();
			}
		};
	}
}
