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

package StartingPopAnalysis;

import core.*;
import java.util.*;
import net.sf.javabdd.*;
import java.lang.reflect.*;

/**
 * Runs a full analysis of a starting population for a specific model for varying sizes
 * @author Lawrence Beadle
 */
public class MainSPopValidator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // CODE TO ANALYSE STARTING POPULATIONS
        System.out.println("STARTING VALIDAOTOR - PROGRAM STARTED");

        // decide which model
        String modelName = "ArtificialAnt";

        // set up the different starting populations to be analysed
        ArrayList<String> sPops = new ArrayList<String>();
        sPops.add("SDIA");

        // pull out the fucntions and terminals
        ArrayList<String> funcs, terms;

        // set up the different sizes of population to be analysed
        ArrayList<Integer> sizes = new ArrayList<Integer>();

        sizes.add(new Integer(2000));

        // set up storage
        ArrayList<ArrayList<String>> newPop;

        // start looping through types and pop sizes
        for (String genType : sPops) {

            // progress monitor
            System.out.println("Working on: " + genType);

            for (Integer size : sizes) {

                // progress monitor
                System.out.println("Working on: " + size.toString());

                // do 100 runs of each type and pop size
                for (int i = 0; i < 100; i++) {

                    // set up Genetic Program instance - USING REFLECTION
                    GPRunBasic genProg = null;
                    try {
                        genProg = (GPRunBasic) Class.forName("Models." + modelName).getConstructor(new Class[]{}).newInstance(new Object[]{});
                    } catch (ClassNotFoundException e) {
                        System.out.println("CLASS NOT FOUND");
                    } catch (NoSuchMethodException f) {
                        System.out.println("NO SUCH METHOD");
                    } catch (InstantiationException z) {
                        System.out.println("COULD NOT INSTANTIATE CLASS");
                    } catch (IllegalAccessException h) {
                        System.out.println("ILLEGAL ACCESS EXCEPTION");
                    } catch (InvocationTargetException k) {
                        System.out.println("INVOCATION EXCEPTION");
                    } catch (ClassCastException l) {
                        System.out.println(l.toString());
                    }

                    if (genProg == null) {
                        System.out.println("REFLECT FAILED TO LOAD FILE: " + modelName);
                    }
                    
                    funcs = genProg.getFunctions();
                    terms = genProg.getTerminals();

                    System.out.println("ITERATION= " + i);

                    // generate population                 
                    newPop = genProg.buildFirstPop(size.intValue(), genType);

                    int j = 0;
                    boolean test, testIFA;
                    for (ArrayList<String> testProg : newPop) {
                        // bracket count each program
                        test = parseBrackets(testProg);
                        if(test==false) {
                            System.out.println("PARSE BRACKET ERROR DETECTED");
                            System.out.println("PROGRAM " + j);
                            System.out.println(testProg);
                        }
                        testIFA = parseIFA(testProg);
                        if(testIFA==false) {
                            System.out.println("PARSE IFA ERROR DETECTED");
                            System.out.println("PROGRAM " + j);
                            System.out.println(testProg);
                        }
                        if(testProg.size()<1) {
                            System.out.println("SIZE CHECK ERROR DETECTED");
                            System.out.println("PROGRAM " + j);
                            System.out.println(testProg);
                        }
                        j++;
                    }
                    
                    // force a garbage collection
                    newPop = null;
                    genProg = null;
                    System.gc();

                }
                // clear over heads
                System.gc();
            }
        }

        // final output
        System.out.println("STARTING POPS VALIDATOR COMPLETE!");
    }
    
    public static boolean parseBrackets(ArrayList<String> toTest) {
        boolean result = false;
        
        int depth = 0;
        String holder;
        Iterator<String> it = toTest.iterator();
        while(it.hasNext()) {
            holder = it.next();
            if(holder.equalsIgnoreCase("(")) {
                depth++;
            }
            if(holder.equalsIgnoreCase(")")) {
                depth--;
            }
        }
        
        if(depth==0) {
            result = true;
        } else {
            result = false;
        }
        
        return result;
    }
    
    public static boolean parseIFA(ArrayList<String> toTest) {
        
        int depth = 0;
        String holder;
        boolean active = false;
        int allele = 0;
        Iterator<String> it = toTest.iterator();
        while(it.hasNext()) {
            holder = it.next();
            if(holder.equalsIgnoreCase("IF-FOOD-AHEAD")) {
                active = true;
                depth = 1;
                allele = 0;
            }
            if (active) {
                if (holder.equalsIgnoreCase("(") && depth==1) {
                    allele++;
                }
                if (holder.equalsIgnoreCase("(")) {
                    depth++;
                }
                if (holder.equalsIgnoreCase(")")) {
                    depth--;
                }
                if (holder.equalsIgnoreCase("MOVE") && depth==1) {
                    allele++;
                }
                if (holder.equalsIgnoreCase("TURN-LEFT") && depth==1) {
                    allele++;
                }
                if (holder.equalsIgnoreCase("TURN-RIGHT") && depth==1) {
                    allele++;
                }                
            }
            if (active) {
                if (depth==0) {
                    if(!(allele==2)) {
                        return false;
                    }
                    active = false;
                }
            }
        }
        
        return true;
    }
}