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
package org.epochx.gr;

import org.epochx.grammar.NonTerminalSymbol;
import org.epochx.source.SourceGenerator;


/**
 * Converts <code>GRIndividual</code>s into source code
 * 
 * @since 2.0
 */
public class GRSourceGenerator implements SourceGenerator<GRIndividual> {

	/**
	 * Returns the source code that the given individual represents. The source of a 
	 * <code>GRIndividual</code> is the string representation of its parse tree, which
	 * typically consists of the terminal nodes output in sequence following a depth-first
	 * traversal. If the given individual has no parse tree set then <code>null</code> 
	 * will be returned.
	 * 
	 * @param individual the individual to return the source code for
	 * @return the source code of the given individual's parse tree or <code>null</code>
	 * if the parse tree is not set 
	 */
	@Override
	public String getSource(GRIndividual individual) {
		NonTerminalSymbol parseTree = individual.getParseTree();
		
		if (parseTree == null) {
			return null;
		}
		
		return parseTree.toString();
	}

}
