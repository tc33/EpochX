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

package Mux37bitCode;

import java.util.ArrayList;

/**
 * The Syntax37bit class conatins the basic syntax for the boolean programs
 * 
 * 
 * @author Lawrence Beadle
 */
public class Syntax37bit implements Core.Syntax {
    
    private ArrayList<ArrayList<String>> syntax = new ArrayList<ArrayList<String>>();
    private ArrayList<String> part = new ArrayList<String>();
    private ArrayList<ArrayList<String>> terminals = new ArrayList<ArrayList<String>>();
    private ArrayList<String> eStart = new ArrayList<String>();
    private ArrayList<String> funcs = new ArrayList<String>();
    private ArrayList<String> terms = new ArrayList<String>();
    
    /**
     * The constructor defines all the syntax for the 11 bit multiplexer.
     */
    public Syntax37bit() {
        
        // load functions & structure ------------------------------------------
        
        // load IF
        part = new ArrayList<String>();
        part.add("(");
        part.add("IF");
        part.add("*");
        part.add("*");
        part.add("*");
        part.add(")");
        syntax.add(part);
        
        // load AND
        part = new ArrayList<String>();
        part.add("(");
        part.add("AND");
        part.add("*");
        part.add("*");
        part.add(")");
        syntax.add(part);
        
        // load OR
        part = new ArrayList<String>();
        part.add("(");
        part.add("OR");
        part.add("*");
        part.add("*");
        part.add(")");
        syntax.add(part);
        
        // load NOT
        part = new ArrayList<String>();
        part.add("(");
        part.add("NOT");
        part.add("*");
        part.add(")");
        syntax.add(part);
        
        // load terminals ------------------------------------------------------
        part = new ArrayList<String>();
        part.add("A0");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("A1");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("A2");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("A3");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("A4");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D0");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D1");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D2");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D3");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D4");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D5");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D6");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D7");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D8");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D9");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D10");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D11");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D12");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D13");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D14");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D15");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D16");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D17");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D18");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D19");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D20");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D21");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D22");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D23");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D24");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D25");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D26");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D27");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D28");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D29");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D30");
        syntax.add(part);
        terminals.add(part);
        
        part = new ArrayList<String>();
        part.add("D31");
        syntax.add(part);
        terminals.add(part);
        
        // sort out expression starting ----------------------------------------     
        eStart.add("A0");
        eStart.add("A1");
        eStart.add("A2");
        eStart.add("A3");
        eStart.add("A4");
        eStart.add("D0");
        eStart.add("D1");
        eStart.add("D2");
        eStart.add("D3");
        eStart.add("D4");
        eStart.add("D5");
        eStart.add("D6");
        eStart.add("D7");
        eStart.add("D8");
        eStart.add("D9");
        eStart.add("D10");
        eStart.add("D11");
        eStart.add("D12");
        eStart.add("D13");
        eStart.add("D14");
        eStart.add("D15");
        eStart.add("D16");
        eStart.add("D17");
        eStart.add("D18");
        eStart.add("D19");
        eStart.add("D20");
        eStart.add("D21");
        eStart.add("D22");
        eStart.add("D23");
        eStart.add("D24");
        eStart.add("D25");
        eStart.add("D26");
        eStart.add("D27");
        eStart.add("D28");
        eStart.add("D29");
        eStart.add("D30");
        eStart.add("D31");
        eStart.add("(");
        
        // make a list of fucntions --------------------------------------------
        funcs.add("AND");
        funcs.add("OR");
        funcs.add("IF");
        funcs.add("NOT");
        
        // make a list of terminals --------------------------------------------        
        terms.add("A0");
        terms.add("A1");
        terms.add("A2");
        terms.add("A3");
        terms.add("A4");
        terms.add("D0");
        terms.add("D1");
        terms.add("D2");
        terms.add("D3");
        terms.add("D4");
        terms.add("D5");
        terms.add("D6");
        terms.add("D7");
        terms.add("D8");
        terms.add("D9");
        terms.add("D10");
        terms.add("D11");
        terms.add("D12");
        terms.add("D13");
        terms.add("D14");
        terms.add("D15");
        terms.add("D16");
        terms.add("D17");
        terms.add("D18");
        terms.add("D19");
        terms.add("D20");
        terms.add("D21");
        terms.add("D22");
        terms.add("D23");
        terms.add("D24");
        terms.add("D25");
        terms.add("D26");
        terms.add("D27");
        terms.add("D28");
        terms.add("D29");
        terms.add("D30");
        terms.add("D31");
        
    }
    
    /**
     * Calls a syntax list to be returned
     * @return An ArrayList<String> representing all the terminals and function within the GP
     */
    public ArrayList<ArrayList<String>> getSyntax() {       
        return syntax;
    }
    
    /**
     * Returns a list of terminals
     * @return A list of terminals
     */
    public ArrayList<ArrayList<String>> getTerms() {        
        return terminals;
    }
    
    /**
     * Returns a list of items that can start an expression
     * @return A List of items that can start an expression - either a terminal or a '('
     */
    public ArrayList<String> getEStart() {        
        return eStart;
    }
    
    /**
     * Returns a list of functions
     * @return A list of Functions
     */
    public ArrayList<String> getFunctions() {        
        return funcs;
    }
    
    /**
     * Retrns a list of terminals
     * @return A list of terminals
     */
    public ArrayList<String> getTerminals() {        
        return terms;
    }
    
}
