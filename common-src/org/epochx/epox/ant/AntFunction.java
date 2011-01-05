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
package org.epochx.epox.ant;

import org.epochx.epox.Node;
import org.epochx.tools.ant.Ant;


/**
 * Ant functions may wish to extend this class to provide much of the 
 * infrastructure necessary to support both of child ant node, and internal 
 * ant reference.
 */
public abstract class AntFunction extends Node {

	// This may remain null, depending on the constructor used.
	private Ant ant;
	
	/**
	 * {@inheritDoc}
	 */
	public AntFunction(final Node ... children) {
		this(null, children);
	}

	/**
	 * Constructs an <code>AntFunction</code> with an ant and child nodes, but
	 * the given ant which will be held internally. This makes could potentially
	 * make the function a terminal node with arity zero if no child nodes are
	 * provided.
	 * 
	 * @param ant
	 */
	public AntFunction(final Ant ant, final Node ... children) {
		super(children);
		
		this.ant = ant;
	}
	
	/**
	 * 
	 */
	@Override
	public Class<?> getReturnType() {
		/*
		 * TODO It may be possible to remove the need to override this in terminals,
		 * see comment in notebook. In which case this will need removing.
		 */
		if (getArity() > 0) {
			return super.getReturnType();
		} else {
			return Void.class;
		}
	}
	
	/**
	 * Creates a new AntFunction instance which is a copy of this instance. 
	 * Any child nodes will also be cloned, in order to create a new subtree.
	 * If this node contains an internal Ant reference, then the new node will 
	 * refer to the same ant instance.
	 * 
	 * @return a new AntFunction instance which is a clone of this one.
	 */
	@Override
	public AntFunction clone() {
		// If there are any children this will be handled by parent.
		final AntFunction clone = (AntFunction) super.clone();

		clone.ant = ant;

		return clone;
	}
	
	/**
	 * Creates a new AntFunction instance from this instance. If this 
	 * function contains child nodes, these will not be copied, but the number 
	 * of required children will be maintained. If this node contains an 
	 * internal Ant reference, then the new node will refer to the same ant 
	 * instance.
	 * 
	 * @return a new AntFunction instance based upon this one.
	 */
	@Override
	public AntFunction newInstance() {
		AntFunction newInstance = (AntFunction) super.newInstance();
		
		newInstance.ant = ant;
		
		return newInstance;
	}
	
	
	/**
	 * @return the ant
	 */
	protected Ant getAnt() {
		return ant;
	}
}
