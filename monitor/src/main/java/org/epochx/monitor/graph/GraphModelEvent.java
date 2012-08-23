/* 
 * Copyright 2007-2012
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.monitor.graph;

import java.util.EventObject;


/**
 * 
 */
public class GraphModelEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8121103469487907089L;

	public static int DELATE = -1;
	
	public static int INSERT = 1;
	
	public static int UPDATE = 1;
	
	public static int REFRESH = 10;
	
	private final int firstGeneration;
	
	private final int lastGeneration;
	
	private final int type;
	
	
	/**
	 * Constructs a <code>GraphModelEvent</code>.
	 * 
	 * @param source
	 */
	public GraphModelEvent(GraphModel source) {
		this(source, 0, source.getGenerationCount(), REFRESH);
	}
	
	/**
	 * Constructs a <code>GraphModelEvent</code>.
	 * 
	 * @param source
	 * @param type
	 */
	public GraphModelEvent(GraphModel source, int type) {
		this(source, 0, source.getGenerationCount(), type);
	}
	
	/**
	 * Constructs a <code>GraphModelEvent</code>.
	 * 
	 * @param source
	 * @param generation
	 * @param type
	 */
	public GraphModelEvent(GraphModel source, int generation, int type) {
		this(source, generation, generation, type);
	}
	
	
	/**
	 * Constructs a <code>GraphModelEvent</code>.
	 * 
	 * @param source
	 * @param firstGeneration
	 * @param lastGeneration
	 * @param type
	 */
	public GraphModelEvent(GraphModel source, int firstGeneration, int lastGeneration, int type) {
		super(source);
		this.firstGeneration = firstGeneration;
		this.lastGeneration = lastGeneration;
		this.type = type;
	}

	
	/**
	 * Returns the first generation.
	 * @return the first generation.
	 */
	public int getFirstGeneration() {
		return firstGeneration;
	}

	
	/**
	 * Returns the last generation.
	 * @return the last generation.
	 */
	public int getLastGeneration() {
		return lastGeneration;
	}

	
	/**
	 * Returns the type.
	 * @return the type.
	 */
	public int getType() {
		return type;
	}
	

}
