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
 * 
 */
public class AntLandscape {

	private Dimension size;
	private ArrayList<Point> foodLocations;
	
	public AntLandscape(Dimension size, ArrayList<Point> foodLocations) {
		this.size = size;
		this.foodLocations = foodLocations;
	}
	
	public void addFoodLocation(Point location) {
		foodLocations.add(location);
	}
	
	public void removeFoodLocation(Point location) {
		foodLocations.remove(location);
	}
	
	public boolean isFoodLocation(Point location) {
		return foodLocations.contains(location);
	}
	
	public void setFoodLocations(ArrayList<Point> foodLocations) {
		this.foodLocations = foodLocations;
	}
	
	public void clearFoodLocations() {
		foodLocations.clear();
	}
	
	public Dimension getSize() {
		return size;
	}
	
	public int getWidth() {
		return size.width;
	}
	
	public int getHeight() {
		return size.height;
	}
	
	public boolean isValidLocation(Point location) {
		return (location.x >= 0) 
			&& (location.x < size.width) 
			&& (location.y >= 0) 
			&& (location.y < size.height);
	}
	
	public Point getNextLocation(Point currentLocation, Orientation orientation) {
		Point newLocation = new Point(currentLocation);
		
		switch (orientation) {
			case NORTH:
				newLocation.y = (currentLocation.y > 0) ? (currentLocation.y-1) : (size.height-1);
				break;
				
			case EAST:
				newLocation.x = (currentLocation.x < size.width-1) ? (currentLocation.x+1) : 0;
				break;
				
			case SOUTH:
				newLocation.y = (currentLocation.y < size.height-1) ? (currentLocation.y+1) : 0;
				break;
				
			case WEST:
				newLocation.x = (currentLocation.x > 0) ? (currentLocation.y-1) : (size.width-1);
				break;
	
			default:
				break;
		}
		
		return newLocation;
	}
}
