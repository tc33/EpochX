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
package org.epochx.fitness;

import org.epochx.Individual;
import org.epochx.source.SourceGenerator;


/**
 * 
 */
public class SourceMatchEvaluator<T extends Individual> extends AbstractFitnessEvaluator<T> {

	private SourceGenerator<T> generator;
	private String expectedSource;
	
	public SourceMatchEvaluator(SourceGenerator<T> generator, String expectedSource) {
		this.generator = generator;
		this.expectedSource = expectedSource;
	}
	
	@Override
	public double getFitness(T program) {
		final String src = generator.getSource(program);

		if (src == null) {
			return Integer.MAX_VALUE;
		}

		final int srcLength = src.length();
		final int matchLength = expectedSource.length();
		int score = 0;

		for (int i = 0; i < matchLength; i++) {
			if ((i < srcLength) && (expectedSource.charAt(i) != src.charAt(i))) {
				score++;
			}
		}

		if (srcLength != matchLength) {
			score += Math.abs(matchLength - srcLength);
		}

		return score;
	}

	/**
	 * @return the expectedSource
	 */
	public String getExpectedSource() {
		return expectedSource;
	}
	
	/**
	 * @param expectedSource the expectedSource to set
	 */
	public void setExpectedSource(String expectedSource) {
		this.expectedSource = expectedSource;
	}
}
