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
package org.epochx.gx.stats;

import static org.epochx.stats.StatField.GEN_POP;
import static org.epochx.stats.Stats.ExpiryEvent.GENERATION;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.epochx.gp.representation.*;
import org.epochx.gx.node.*;
import org.epochx.stats.*;

/**
 * Provides constants to be used as keys to request statistics from the
 * Stats specific to XGX.
 */
public class GXStatField {
	
	/**
	 * Returns an <code>int[]</code> which contains the number of statements in
	 * each <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_NO_STATEMENTS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] noStatements = null;

			// Request the population from the stats manager.
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the depths of each program.
			if (pop != null) {
				noStatements = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					ASTNode node = (ASTNode) pop.get(i).getRootNode();
					noStatements[i] = node.getTotalNoStatements();
				}
			}

			return noStatements;
		}
	};
	
	/**
	 * Returns a <code>Double</code> which is the average number of statements
	 * in all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_STATEMENTS_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveNoStatements = null;

			// Request the population's number of statements from the stats
			// manager.
			final int[] noStatements = (int[]) Stats.get().getStat(GEN_NO_STATEMENTS);

			// Calculate the average number of statements.
			if (noStatements != null) {
				aveNoStatements = StatsUtils.ave(noStatements);
			}

			return aveNoStatements;
		}
	};
	
	/**
	 * Returns an <code>Integer</code> which is the minimum number of statements
	 * in all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_STATEMENTS_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minNoStatements = null;

			// Request the population's number of statements from the stats
			// manager.
			final int[] noStatements = (int[]) Stats.get().getStat(GEN_NO_STATEMENTS);

			// Calculate the average number of statements.
			if (noStatements != null) {
				minNoStatements = NumberUtils.min(noStatements);
			}

			return minNoStatements;
		}
	};
	
	/**
	 * Returns an <code>Integer</code> which is the maximum number of statements
	 * in all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_STATEMENTS_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxNoStatements = null;

			// Request the population's number of statements from the stats
			// manager.
			final int[] noStatements = (int[]) Stats.get().getStat(GEN_NO_STATEMENTS);

			// Calculate the average number of statements.
			if (noStatements != null) {
				maxNoStatements = NumberUtils.max(noStatements);
			}

			return maxNoStatements;
		}
	};
}
