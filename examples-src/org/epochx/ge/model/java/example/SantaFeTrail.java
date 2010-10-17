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
package org.epochx.ge.model.java.example;

import static org.epochx.ge.stats.GEStatField.*;
import static org.epochx.stats.StatField.*;

import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.life.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.stats.*;

public class SantaFeTrail extends org.epochx.ge.model.java.SantaFeTrail {

	public SantaFeTrail() {
		final DepthFirstMapper mapper = new DepthFirstMapper(this);
		mapper.setWrapping(true);
		mapper.setRemovingUnusedCodons(false);
		setMapper(mapper);

		setMaxCodonSize(256);
		setNoRuns(100);
		setNoElites(10);
		setNoGenerations(100);
		setPopulationSize(500);
		setMaxDepth(12);
		setMaxInitialDepth(8);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setProgramSelector(new TournamentSelector(this, 3));
		setPoolSelector(null);
		setPoolSize(-1);
		setInitialiser(new RampedHalfAndHalfInitialiser(this));

		Life.get().addGenerationListener(new GenerationAdapter() {

			@Override
			public void onGenerationEnd() {
				Stats.get().printStats(GEN_NUMBER,
						GEN_FITNESS_MIN, GEN_FITNESS_AVE, GEN_DEPTH_AVE,
						GEN_DEPTH_MAX, GEN_FITTEST_PROGRAM);
			}
		});

		Life.get().addRunListener(new RunAdapter() {

			@Override
			public void onRunEnd() {
				Stats.get().printStats(RUN_NUMBER, RUN_FITNESS_MIN,
						RUN_FITTEST_PROGRAM);
			}
		});
	}

	public static void main(final String[] args) {
		new SantaFeTrail().run();
	}
}
