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
 */
package com.epochx.ant;

import java.awt.*;
import java.util.*;

/**
 * Object represents the landscape in which the ant is operating
 * including storing food locations
 */
/**
 * @author lb212
 *
 */
public class AntLandscape {

	private Dimension size;
	private ArrayList<Point> foodLocations;
	
	/**
	 * AntLandscape Constructor
	 * @param size The size of the landscape
	 * @param foodLocations The food locations upon the landscape
	 */
	public AntLandscape(Dimension size, ArrayList<Point> foodLocations) {
		this.size = size;
		this.foodLocations = foodLocations;
	}
	
	/**
	 * Adds a food location to the landscape
	 * @param location the location of the new item of food
	 */
	public void addFoodLocation(Point location) {
		foodLocations.add(location);
	}
	
	/**
	 * Removes a food location from the ant landscape
	 * @param location The location of the item of food to remove
	 */
	public void removeFoodLocation(Point location) {
		foodLocations.remove(location);
	}
	
	/**
	 * Test whether a location has food on it
	 * @param location The location to test
	 * @return TRUE if food is present
	 */
	public boolean isFoodLocation(Point location) {
		return foodLocations.contains(location);
	}
	
	/**
	 * Sets multiple food locations
	 * @param foodLocations A list of food locations
	 */
	public void setFoodLocations(ArrayList<Point> foodLocations) {
		this.foodLocations = foodLocations;
	}
	
	/**
	 * Clears all food locations
	 */
	public void clearFoodLocations() {
		foodLocations.clear();
	}
	
	/**
	 * Returns the size of the ant landscape
	 * @return the dimensions of the ant landscape
	 */
	public Dimension getSize() {
		return size;
	}
	
	/**
	 * Returns the width of the ant landscape
	 * @return The width of the ant landscape
	 */
	public int getWidth() {
		return size.width;
	}
	
	/**
	 * Returns the height of the ant landscape
	 * @return The height of the ant landscape
	 */
	/**
	 * @return
	 */
	public int getHeight() {
		return size.height;
	}
	
	/**
	 * Tests if a location is valid
	 * @param location The location to test
	 * @return TRUE if location is valid
	 */
	public boolean isValidLocation(Point location) {
		return (location.x >= 0) 
			&& (location.x < size.width) 
			&& (location.y >= 0) 
			&& (location.y < size.height);
	}
	
	/**
	 * Returns the location directly in front of the ant
	 * @param currentLocation The current location
	 * @param orientation The current orientation
	 * @return new location in front of the ant
	 */
	public Point getNextLocation(Point currentLocation, Orientation orientation) {
		Point newLocation = new Point(currentLocation.x, currentLocation.y);
		
		switch (orientation) {
			case NORTH:
				newLocation.y = (newLocation.y > 0) ? (newLocation.y-1) : (size.height-1);
				break;
				
			case EAST:
				newLocation.x = (newLocation.x < size.width-1) ? (newLocation.x+1) : 0;
				break;
				
			case SOUTH:
				newLocation.y = (newLocation.y < size.height-1) ? (newLocation.y+1) : 0;
				break;
				
			case WEST:
				newLocation.x = (newLocation.x > 0) ? (newLocation.y-1) : (size.width-1);
				break;
	
			default:
				break;
		}
		
		return newLocation;
	}
}
