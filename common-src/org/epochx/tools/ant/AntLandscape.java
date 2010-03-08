/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 *//* 
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
package org.epochx.tools.ant;


import java.awt.*;
import java.util.List;

/**
 * An <code>AntLandscape</code> provides the environment an artificial ant 
 * operates in. The landscape is essentially a torus with a given width/height, 
 * over which the positions wrap back around. The landscape also incorporates a 
 * set of food locations which define the location of food pellets within 
 * that co-ordinate system.
 */
public class AntLandscape {

	// The width/height dimensions of the landscape.
	private Dimension size;
	
	// The locations of food pellets.
	private List<Point> foodLocations;
	
	/**
	 * Constructs an AntLandscape with the given dimensions and food locations.
	 * To be reachable, foodLocations should refer to points inside the size 
	 * dimensions where co-ordinate points are indexed from 0.
	 * @param size the width/height dimensions of the landscape.
	 * @param foodLocations the location of food pellets upon the landscape.
	 */
	public AntLandscape(Dimension size, List<Point> foodLocations) {
		this.size = size;
		this.foodLocations = foodLocations;
	}
	
	/**
	 * Adds a new food location to the landscape. The new point may <b>not</b>
	 * be tested to check it is a valid location within the landscape 
	 * dimensions so care should be taken to provide valid locations.
	 * @param location the x/y co-ordinates of the new item of food.
	 */
	public void addFoodLocation(Point location) {
		foodLocations.add(location);
	}
	
	/**
	 * Removes a food location from the ant landscape. If there are multiple 
	 * food pellets at the same <code>Point</code> location then only one will
	 * be removed. If no food pellet exists at the given location then this 
	 * method will do nothing.
	 * @param location the location of a current food item to be removed.
	 */
	public void removeFoodLocation(Point location) {
		foodLocations.remove(location);
	}
	
	/**
	 * Tests whether a location contains an item of food.
	 * @param location The location in the landscape to test.
	 * @return true if food is present at the given location, false otherwise.
	 */
	public boolean isFoodLocation(Point location) {
		return foodLocations.contains(location);
	}
	
	/**
	 * Replaces the current set of food locations with a new set. To be 
	 * reachable, foodLocations should refer to points inside the size 
	 * dimensions of the landscape where co-ordinate points are indexed 
	 * from 0.
	 * @param foodLocations the location of food pellets upon the landscape.
	 */
	public void setFoodLocations(List<Point> foodLocations) {
		this.foodLocations = foodLocations;
	}
	
	/**
	 * Clears all food locations on the landscape.
	 */
	public void clearFoodLocations() {
		foodLocations.clear();
	}
	
	/**
	 * Returns the size of the ant landscape.
	 * @return the dimensions of the ant landscape.
	 */
	public Dimension getSize() {
		return size;
	}
	
	/**
	 * Returns the width of the ant landscape.
	 * @return the width of the ant landscape.
	 */
	public int getWidth() {
		return size.width;
	}
	
	/**
	 * Returns the height of the ant landscape.
	 * @return the height of the ant landscape.
	 */
	public int getHeight() {
		return size.height;
	}
	
	/**
	 * Tests if a location is a valid position within the landscape's 
	 * dimensions. Locations are indexed from 0.
	 * @param location the location to test.
	 * @return true if the location is a valid position on the landscape.
	 */
	public boolean isValidLocation(Point location) {
		return (location.x >= 0) 
			&& (location.x < size.width) 
			&& (location.y >= 0) 
			&& (location.y < size.height);
	}
	
	/**
	 * Returns the location of one move on from the given location in the 
	 * direction of the provided orientation. The landscape is a torus so this 
	 * method is required to calculate the necessary wrapping.
	 * @param location the current location to calculate the next move from.
	 * @param orientation the direction of the move.
	 * @return the next location one move on from the given location in the 
	 * direction of the provided orientation.
	 */
	public Point getNextLocation(Point location, Orientation orientation) {
		Point newLocation = new Point(location);
		
		switch (orientation) {
			case NORTH:
				newLocation.y = (location.y > 0) ? (location.y-1) : (size.height-1);
				break;
				
			case EAST:
				newLocation.x = (location.x < size.width-1) ? (location.x+1) : 0;
				break;
				
			case SOUTH:
				newLocation.y = (location.y < size.height-1) ? (location.y+1) : 0;
				break;
				
			case WEST:
				newLocation.x = (location.x > 0) ? (location.y-1) : (size.width-1);
				break;
	
			default:
				break;
		}
		
		return newLocation;
	}
}
