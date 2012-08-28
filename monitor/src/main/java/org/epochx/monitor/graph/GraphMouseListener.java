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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A <code>GraphMouseListener</code> receives mouse events from the graph.
 */
public class GraphMouseListener implements MouseListener, MouseMotionListener {

	public void mouseClicked(MouseEvent e) {

		if (e.getSource() instanceof GraphView) {
			GraphView view = (GraphView) e.getSource();
			GraphViewModel viewModel = view.getViewModel();
			viewModel.select(e.getPoint());
		}

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		System.out.println(e);
	}

	public void mouseMoved(MouseEvent e) {

		if (e.getSource() instanceof GraphView) {
			GraphView view = (GraphView) e.getSource();
			GraphViewModel viewModel = view.getViewModel();
			viewModel.highlight(e.getPoint());
		}
	}

}
