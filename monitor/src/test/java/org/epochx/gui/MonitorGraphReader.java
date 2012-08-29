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
package org.epochx.gui;

import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.stat.GenerationAverageDoubleFitness;
import org.epochx.event.stat.GenerationBestFitness;
import org.epochx.event.stat.GenerationNumber;
import org.epochx.event.stat.GenerationWorstFitness;
import org.epochx.monitor.Monitor;
import org.epochx.monitor.graph.Graph;
import org.epochx.monitor.graph.GraphModel;
import org.epochx.monitor.graph.GraphViewModel;
import org.epochx.monitor.table.Table;


/**
 * A <code>MonitorGraphReader</code>.
 */
public class MonitorGraphReader {

	
	public static void main(String[] args) {
		Monitor monitor = new Monitor("Frame Graph Test");
		
		Table table1 = new Table("Fitnesses Table");
		table1.addStat(GenerationNumber.class);
		table1.addStat(GenerationBestFitness.class);
		table1.addStat(GenerationWorstFitness.class);
		table1.addStat(GenerationAverageDoubleFitness.class);
		table1.addListener(EndGeneration.class);

		monitor.add(table1, 1, 1);
		
		GraphModel model = Graph.loadInputModel("backup.ser");
		Graph g = new Graph("Visualization Graph", model, new GraphViewModel());
		monitor.add(g);
		
	}
	
}
