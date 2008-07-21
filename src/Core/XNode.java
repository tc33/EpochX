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

package Core;

/**
 * XNode assist the semantic operations - more specifically back translation of ROBDDs
 * @author Lawrence Beadle
 */
public class XNode {
    
    private int id, tCID, fCID;
    
    /** Creates a new instance of XNode
     * @param idEnter - the ID of the XNode
     */
    public XNode(int idEnter) {
        id = idEnter;
    }
    
    /**
     * Returns the true child id of the node
     * @return The true child of the node
     */
    public int getTCID() {
        return tCID;
    }
    
    /**
     * Returns the false child ID of the node
     * @return The ID of the false child
     */
    public int getFCID() {
        return fCID;
    }
    
    /**
     * Returns the ID of this node
     * @return The ID of this node
     */
    public int getID() {
        return id;
    }
    
    /**
     * Sets the ID of the true child of this node
     * @param tEnter The ID of the true child
     */
    public void setTCID(int tEnter) {
        tCID = tEnter;
    }
    
    /**
     * Sets the ID of the false child of this node
     * @param fEnter The ID of the false child
     */
    public void setFCID(int fEnter) {
        fCID = fEnter;
    }    
}
