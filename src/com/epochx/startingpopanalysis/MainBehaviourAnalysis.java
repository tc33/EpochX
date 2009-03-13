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

package com.epochx.startingpopanalysis;

import com.epochx.semantics.*;
import com.epochx.util.*;
import java.util.ArrayList;
import java.io.File;
import net.sf.javabdd.*;
import java.lang.reflect.*;

/**
 * Runs a full analysis of a starting population for a specific model for varying sizes
 * @author Lawrence Beadle
 */
public class MainBehaviourAnalysis {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // CODE TO ANALYSE STARTING POPULATIONS
        System.out.println("STARTING POP ANALYSIS - PROGRAM STARTED");
        
        // decide which model
        String modelName = "ArtificialAnt";        
        
        // set up the different starting populations to be analysed
        ArrayList<String> sPops = new ArrayList<String>();
        sPops.add("RH+H");
        //sPops.add("Grow");
        //sPops.add("Full");
        //sPops.add("H+H");
        //sPops.add("SDAB");
        
        // set up the different sizes of population to be analysed
        ArrayList<Integer> sizes = new ArrayList<Integer>();
        
        sizes.add(new Integer(500));
        sizes.add(new Integer(1000));
        sizes.add(new Integer(1500));
        sizes.add(new Integer(2000));        
        sizes.add(new Integer(2500));
        sizes.add(new Integer(3000));
        sizes.add(new Integer(3500));
        sizes.add(new Integer(4000));
        sizes.add(new Integer(4500));
        sizes.add(new Integer(5000));
        /**
        sizes.add(new Integer(5500));
        sizes.add(new Integer(6000));
        sizes.add(new Integer(6500));
        sizes.add(new Integer(7000));
        sizes.add(new Integer(7500));
        sizes.add(new Integer(8000));
        sizes.add(new Integer(8500));
        sizes.add(new Integer(9000));
        sizes.add(new Integer(9500));
        sizes.add(new Integer(10000));        
        sizes.add(new Integer(10500));
        sizes.add(new Integer(11000));
        sizes.add(new Integer(11500));        
        sizes.add(new Integer(12000));
        sizes.add(new Integer(12500));
        sizes.add(new Integer(13000));
        sizes.add(new Integer(13500));
        sizes.add(new Integer(14000));
        sizes.add(new Integer(14500));
        sizes.add(new Integer(15000));
         * **/
        
        // set up equivalence storage        
        ArrayList<Representation> behaviours;
        ArrayList<ArrayList<String>> progs, newPop;
        ArrayList<String> dump;
        int syntaxSame, semanticSame;
        Representation specimin;
        
        // file loaction
        //File place = new File("C:/JavaProjects/EpochX1_0/Results");
        //File place = new File("/home/cug/lb212/EPOCHX/Results");
        File place = new File("U:/home/JavaProjects/EpochX1_0/Results");
        
        // start looping through types and pop sizes
        for(String genType: sPops) {
            
            // progress monitor
            System.out.println("Working on: " + genType);
            
            for(Integer size: sizes) {
                
                // progress monitor
                System.out.println("Working on: " + size.toString());
                
                dump = new ArrayList<String>();
                dump.add("Experiment: " + genType + " - " + size.toString() + "-" + modelName + "\n\n");
                dump.add("Pop_ID\tSyntax_Same\tSyntax_Unique\tSemantic_Same\tSemantic_Unique\tPopulation\n");
                
                // do 100 runs of each type and pop size
                for(int i = 0; i<100; i++) {
                    
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
                    
                    SemanticModule semMod = genProg.getSemanticModel();

                    // generate population                 
                    newPop = genProg.buildFirstPop(size.intValue(), genType);
                    
                    // start equivalence module
                    behaviours = new ArrayList<Representation>();
                    progs = new ArrayList<ArrayList<String>>();                    
                    semMod.start();
                    syntaxSame = 0;
                    semanticSame = 0;                   
                    
                    for(ArrayList<String> testProg: newPop) {
                        
                        // check for syntax equivalence
                        if(progs.contains(testProg)) {
                            syntaxSame++;
                        } else {
                            progs.add(testProg);
                        }                      
                        
                        // check for semantic equivalence
                        specimin = semMod.codeToBehaviour(testProg);
                        boolean marker = false;
                        for(Representation b: behaviours) {
                            if(b.equals(specimin)) {
                                semanticSame++;
                                marker = true;
                                break;
                            }
                        }
                        // add if not found
                        if(!marker) {
                            behaviours.add(specimin);
                        }                      
                    }
                    
                    // store run details
                    dump.add(i + "\t" + syntaxSame + "\t" + progs.size() + "\t" + semanticSame + "\t" + behaviours.size() + "\t" + size.toString() + "\n");
                    
                    // close equivalence module
                    semMod.stop();
                    
                    // dump everything and force GC
                    newPop = null;
                    behaviours = null;
                    progs = null;
                    semMod = null;
                    genProg = null;
                    System.gc();                    
                    
                }
                
                // dump to file
                String name = genType + "-B+S-" + size.toString() + "-" + modelName + ".txt";
                FileManip.doOutput(place, dump, name, false);
                
                // dump files and force gc
                dump = null;
                System.gc();
            }            
        }
        
        // final output
        System.out.println("STARTING POPS ANALYSIS COMPLETE!");
    }    
}
