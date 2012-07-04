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
package org.epochx.ge.map;

import org.epochx.ge.GEIndividual;
import org.epochx.grammar.*;

/**
 * A mapper's job is to convert a <tt>GEIndividual</tt> into a parse tree.
 * Typically a mapping process uses the chromosome of the GEIndividual to guide
 * production choices in a grammar.
 */
public interface Mapper {

	/**
	 * Map the given <tt>GEIndividual</tt> to a parse tree
	 * 
	 * @param individual the individual to be converted to a parse tree
	 * @return a <tt>Symbol</tt> which is the root node of a valid parse tree,
	 *         or <tt>null</tt> if no valid parse tree could be created from the
	 *         individual
	 */
	public Symbol map(GEIndividual individual);

}
