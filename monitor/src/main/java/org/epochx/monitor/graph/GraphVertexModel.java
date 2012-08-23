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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;


/**
 * 
 */
public class GraphVertexModel {
		

	
	private GraphVertex vertex;
	
	private GraphViewModel viewModel;
	
	private Point centre;
	
	//private Color fillColor;
	
	private int diameter;
	
	private boolean highlighted;
	
	GraphVertexModel(GraphVertex vertex, GraphViewModel viewModel) {
		this.setVertex(vertex);
		//this.fillColor = viewModel.getFitnessColor(vertex.getFitness());
		this.highlighted = false;
		this.viewModel = viewModel;
		this.diameter = viewModel.getDiameter();
		
		viewModel.addFitness(vertex.getFitness());
		
		setDefaultPosition();
	}

	/**
	 * @return the vertex
	 */
	public GraphVertex getVertex() {
		return vertex;
	}

	/**
	 * @param vertex the vertex to set
	 */
	public void setVertex(GraphVertex vertex) {
		this.vertex = vertex;
	}
	
	/**
	 * @return the viewModel
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * @param viewModel the viewModel to set
	 */
	public void setViewModel(GraphViewModel viewModel) {
		this.viewModel = viewModel;
	}

	/**
	 * @return the diameter
	 */
	public int getDiameter() {
		int res;
		if (!highlighted) {
			res = diameter;
		} else {
			res = (int) (diameter * 1.2);
		}
		return res;
	}

	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	/**
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return viewModel.getFitnessColor(vertex.getFitness());
	}
	
	/**
	 * @return the highlighted
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * @param b
	 */
	public void setHighlighted(boolean b) {
		this.highlighted = b;
	}

	
	/**
	 * 
	 * @return the centre point.
	 */
	public Point getCenter() {
		return centre;
	}

	public void setCentre(Point p) {
		this.centre = p;
	}
	

	
	public double getX() {
		return centre.getX();
	}
	
	public double getY() {
		return centre.getY();
	}
	
	/**
	 * 
	 * @return the location point.
	 */
	public Point getLocation() {
		int x = (int) (centre.getX()-getDiameter()/2.0);
		int y = (int) (centre.getY()-getDiameter()/2.0);
		return new Point(x, y);
	}
	
	public Rectangle getBounds() {
		return new Rectangle (getLocation(), new Dimension(diameter, diameter));
	}
	
	public boolean contains(Point p) {
		Rectangle bounds = getBounds();
		return bounds.contains(p);
	}

	public void setDefaultPosition() {
		int x = viewModel.getMargins().left+vertex.getIndex() * (viewModel.getDiameter() + viewModel.getHgap());
		int y = viewModel.getMargins().top+vertex.getGenerationNo() * (viewModel.getDiameter() + viewModel.getVgap());
		centre = new Point(x, y);
	}
}
