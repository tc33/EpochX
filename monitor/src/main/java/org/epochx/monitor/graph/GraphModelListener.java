/* 
 * Copyright 2007-2013
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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.monitor.graph;

import java.util.EventListener;


/**
 * <code>GraphModelListener</code> defines the interface for an object that listens to changes in a <code>GraphModel</code>.
 */
public interface GraphModelListener extends EventListener {

	/**
	 * This fine grain notification tells listeners the exact data that changed.
	 * @param e the <code>GraphModelEvent</code>
	 */
	void graphChanged(GraphModelEvent e);
}
