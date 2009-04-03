/*  
 *  Copyright 2007-2008 Lawrence Beadle
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

import static com.epochx.ant.Orientation.*;

import java.awt.*;

/**
 * This class represents an ant as an object for the artificial ant simulation
 * @author Lawrence Beadle
 */
public class Ant {
    
    private Orientation orientation;
    private int moves;
    private int xLocation;
    private int yLocation;
    private int foodEaten;
    private int maxMoves;
    private AntLandscape antLandscape;
    
    /**
     * Constructor for the artificial ant model
     * @param timeSteps The maximum number of time steps the ant is allowed to move - both turns and moves count as one step
     */
    public Ant(int timeSteps, AntLandscape antLandscape) {
        orientation = EAST;
        moves = 0;
        xLocation = 0;
        yLocation = 0;
        foodEaten = 0;
        this.maxMoves = timeSteps;
        this.antLandscape = antLandscape;
    }
    
    /**
     * Makes the ant turn left
     */
    public void turnLeft() {
        if(moves>=maxMoves) {
            return;
        }
        if(orientation == EAST) {
            orientation = NORTH;
        } else if(orientation == NORTH) {
            orientation = WEST;
        } else if(orientation == WEST) {
            orientation = SOUTH;
        } else if(orientation == SOUTH) {
            orientation = EAST;
        }
        moves++;
    }
    
    /**
     * Makes the ant turn right
     */
    public void turnRight() {
        if(moves>=maxMoves) {
            return;
        }
        if(orientation == EAST) {
            orientation = SOUTH;
        } else if(orientation == SOUTH) {
            orientation = WEST;
        } else if(orientation == WEST) {
            orientation = NORTH;
        } else if(orientation == NORTH) {
            orientation = EAST;
        }
        moves++;
    }
    
    /**
     * Simulates one move in the ant world
     */
    public void move() {
        if(moves>=maxMoves) {
            return;
        }
        if(orientation == EAST) {
            if(xLocation<31) {
                xLocation++;
            } else {
                xLocation = 0;
            }
        } else if(orientation == NORTH) {
            if(yLocation>0) {
                yLocation--;
            } else {
                yLocation = 31;
            }
        } else if(orientation == WEST) {
            if(xLocation>0) {
                xLocation--;
            } else {
                xLocation = 31;
            }
        } else if(orientation == SOUTH) {
            if(yLocation<31) {
                yLocation++;
            } else {
                yLocation = 0;
            }
        }
        moves++;
        
        // f food pellet present - eat food
        if(antLandscape.isFoodLocation(getLocation())) {
        	this.eatFood();
        	antLandscape.removeFoodLocation(getLocation());
        }
    }
    
    /**
     * Increments the number of food pellets the ant has eaten
     */
    public void eatFood() {
        if(moves>=maxMoves) {
            return;
        }
        foodEaten++;
    }
    
    /**
     * Returns the current location of Ant
     * @return Location of the ant
     */
    public Point getLocation() {
    	return new Point(xLocation, yLocation);
    }
    
    /**
     * Gets the number of moves completed
     * @return The number of moves completed
     */
    public int getMoves() {
        return moves;
    }
    
    /**
     * Returns the number of food pellets eaten
     * @return The number of food pellets eaten
     */
    public int getFoodEaten() {
        return foodEaten;
    }
    
    /**
     * Returns the direction the Ant is facing N S E W
     * @return N S E W
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    /**
     * Sets the location of the ant
     * @param x The x axis location of the ant
     * @param y The Y axis location of the ant
     */
    public void setLocation(int x, int y) {
        xLocation = x;
        yLocation = y;
    }
    
    /**
     * Sets the orientation of the ant
     * @param o The orientation either N S E W
     */
    public void setOrientation(Orientation o) {
        orientation = o;
    }
    
    /**
     * increments moves for skip algorithm required to prevent ant falling into dead ends
     */
    public void skip() {
        moves++;
    }
    
    public int getMaxMoves() {
    	return maxMoves;
    }
    
    public void resetAnt(int timeSteps, AntLandscape antLandscape) {
        orientation = EAST;
        moves = 0;
        xLocation = 0;
        yLocation = 0;
        foodEaten = 0;
        this.maxMoves = timeSteps;
        this.antLandscape = antLandscape;
    }
}
