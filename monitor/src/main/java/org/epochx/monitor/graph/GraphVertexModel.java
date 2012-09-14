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
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A <code>GraphVertexModel</code> provides the visualization model for a
 * specific <code>GraphVertex</code> instance.
 * <p>
 * It registers all the fields related with the displaying, such as the
 * location, the color, the status (selected/highlighted).
 * </p>
 */
public class GraphVertexModel {

	/**
	 * The <code>GraphVertex</code> represented by this model.
	 */
	private GraphVertex vertex;

	/**
	 * The index of the vertex in its generation.
	 */
	private int index;

	/**
	 * The generation number of the vertex.
	 */
	private final int generation;

	/**
	 * The boolean variable which states if the vertex is selected.
	 */
	private boolean selected;

	/**
	 * The boolean variable which states if the vertex is highlighted.
	 */
	private boolean highlighted;

	/**
	 * The centre point of the vertex.
	 */
	private int x;

	/**
	 * The centre point of the vertex.
	 */
	private int y;

	/**
	 * The color of the vertex.
	 */
	private Color color;

	/**
	 * The diameter of the vertex.
	 */
	private int diameter;

	////////////////////////////////////////////////////////////////////////////
	//             C O N S T R U C T O R                                      //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a <code>GraphVertexModel</code>.
	 * 
	 * @param vertex the vertex to match with this model.
	 * 
	 * @throws IllegalArgumentException if the given vertex is null.
	 */
	public GraphVertexModel(GraphVertex vertex) throws IllegalArgumentException {

		if (vertex == null) {
			throw new IllegalArgumentException("The vertex cannot be null.");
		}

		this.setVertex(vertex);
		this.diameter = 0;
		this.highlighted = false;
		this.index = vertex.getIndex();
		this.generation = vertex.getGenerationNo();
		this.color = Color.WHITE;
		this.x = 0;
		this.y = 0;
	}

	////////////////////////////////////////////////////////////////////////////
	//             G E T T E R S  &  S E T T E R S                            //
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the vertex whose this model represents
	 * 
	 * @return the vertex whose this model represents.
	 */
	public GraphVertex getVertex() {
		return vertex;
	}

	
	/**
	 * Sets the vertex whose this model represents.
	 * @param vertex the vertex to set.
	 */
	public void setVertex(GraphVertex vertex) {
		this.vertex = vertex;
	}

	
	/**
	 * Returns the index of the vertex among its generation.
	 * @return the index of the vertex among its generation.
	 */
	public int getIndex() {
		return index;
	}

	
	/**
	 * Sets the index of the vertex among its generation.
	 * @param index the index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	
	/**
	 * Returns the selected state.
	 * @return true if this vertex is selected; false otherwise.
	 */
	public boolean isSelected() {
		return selected;
	}

	
	/**
	 * Sets the selected state.
	 * @param selected the selected state to set.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	
	/**
	 * Returns the highlighted state.
	 * @return true if this vertex is highlighted; false otherwise.
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	
	/**
	 * Sets the highlighted state.
	 * @param highlighted the highlighted state to set.
	 */
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	
	/**
	 * Returns the color of this vertex.
	 * @return the color of this vertex.
	 */
	public Color getColor() {
		return color;
	}

	
	/**
	 * Sets the color of this vertex.
	 * @param color the color to set.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	
	/**
	 * Returns the diameter of this vertex.
	 * @return the diameter of this vertex.
	 */
	public int getDiameter() {
		return diameter;
	}

	
	/**
	 * Sets the diameter of this vertex.
	 * @param diameter the diameter to set.
	 */
	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	
	/**
	 * Returns the generation of this vertex.
	 * @return the generation of this vertex.
	 */
	public int getGeneration() {
		return generation;
	}


	////////////////////////////////////////////////////////////////////////////
	//             L O C A T I O N   R E L A T E D   M E T H O D S            //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the X coordinate.
	 * 
	 * @return the X coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the X coordinate.
	 * 
	 * @param x the X coordinate to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the Y coordinate.
	 * 
	 * @return the Y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the Y coordinate.
	 * 
	 * @param y the Y coordinate to set.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns the centre location of this node.
	 * 
	 * @return the centre location of this node.
	 */
	public Point getCentre() {
		return new Point(x, y);
	}

	/**
	 * Sets the centre location of this node.
	 * 
	 * @param x the X coordinate.
	 * @param y the Y coordinate.
	 */
	public void setCentre(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the bounds of this node.
	 * 
	 * @return the bounds of this node.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x - diameter / 2, y - diameter / 2, diameter, diameter);
	}

	/**
	 * Checks if the specified point is inside this node's bounds.
	 * 
	 * @param p the specicied point whose location is to be check.
	 * @return true if the specified point is inside this node's bounds; false
	 *         otherwise.
	 */
	public boolean contains(Point p) {
		Rectangle bounds = getBounds();
		return bounds.contains(p);
	}

}
