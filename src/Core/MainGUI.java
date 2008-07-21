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
import GUI.*;

/**
 * Loads a GUI to operate the genetic program
 * @author Lawrence Beadle
 */
public class MainGUI {
    
    /**
     * Sets up the GUI
     * @param args args
     */
    public static void main(String args[]) {
        
        GPModel model = new GPModel();
        GPView view = new GPView(model);
        GPController control = new GPController(view, model);
        view.setVisible(true);
        
    }
    
}
