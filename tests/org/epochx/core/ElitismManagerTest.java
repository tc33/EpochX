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
package org.epochx.core;

import java.util.*;

import org.epochx.life.ElitismListener;
import org.epochx.representation.CandidateProgram;

import junit.framework.TestCase;

/**
 * 
 */
public class ElitismManagerTest extends TestCase {

	private Model model;
	private ElitismManager elitismManager;
	private List<CandidateProgram> pop;
	
	@Override
	protected void setUp() throws Exception {
		model = new Model() {
			@Override
			public double getFitness(CandidateProgram program) {
				return 0;
			}
		};
		elitismManager = new ElitismManager(model);
		pop = new ArrayList<CandidateProgram>();
	}
	
	public void testElitismEventsOrder() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();
		
		// Listen for the events.
		model.getLifeCycleManager().addElitismListener(new ElitismListener() {
			@Override
			public void onElitismStart() {
				verify.append('1');
			}
			@Override
			public List<CandidateProgram> onElitism(List<CandidateProgram> elites) {
				verify.append('2');
				return elites;
			}
			@Override
			public void onElitismEnd() {
				verify.append('3');
			}
		});
		
		elitismManager.elitism(pop);
		
		assertEquals("elitism events were not called in the correct order", "123", verify.toString());
	}
}
