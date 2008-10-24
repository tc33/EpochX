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
import java.util.ArrayList;
import java.io.File;
import net.sf.javabdd.*;
import java.lang.reflect.*;

/**
 * Runs a full analysis of a starting population for a specific model for varying sizes
 * @author Lawrence Beadle
 */
public class MainSizeAndShapeAnalysis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // CODE TO ANALYSE STARTING POPULATIONS
        System.out.println("STARTING SIZE AND SHAPE ANALYSIS - PROGRAM STARTED");

        // decide which model
        String modelName = "ArtificialAnt";

        // set up the different starting populations to be analysed
        ArrayList<String> sPops = new ArrayList<String>();
        //sPops.add("RH+H");
        //sPops.add("Grow");
        //sPops.add("Full");
        //sPops.add("H+H");
        sPops.add("SDIA");
        sPops.add("HSDIA");

        // pull out the fucntions and terminals
        ArrayList<String> funcs, terms;

        // set up the different sizes of population to be analysed
        ArrayList<Integer> sizes = new ArrayList<Integer>();

        //sizes.add(new Integer(500));
        sizes.add(new Integer(1000));
        /**
        sizes.add(new Integer(1500));
        sizes.add(new Integer(2000));        
        sizes.add(new Integer(2500));
        sizes.add(new Integer(3000));
        sizes.add(new Integer(3500));
        sizes.add(new Integer(4000));
        sizes.add(new Integer(4500));
        sizes.add(new Integer(5000));        
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
        // set up storage
        ArrayList<String> dump;
        ArrayList<ArrayList<String>> newPop;
        double[] depths, lengths, functions, terminals, dTerminals;
        String name;

        // file loaction
        //File place = new File("D:/JavaProjects/EpochX1_0/Results");
        //File place = new File("/home/cug/lb212/EPOCHX/Results");
        File place = new File("U:/home/JavaProjects/EpochX1_0/Results");

        // start looping through types and pop sizes
        for (String genType : sPops) {

            // progress monitor
            System.out.println("Working on: " + genType);

            for (Integer size : sizes) {

                // progress monitor
                System.out.println("Working on: " + size.toString());

                dump = new ArrayList<String>();
                dump.add("Experiment: " + genType + " - " + size.toString() + "\tModel = " + modelName + "\n\n");
                dump.add("Pop_Size\tAve_Depth\tAve_Length\tAve_Functions\tAve_Terminals\tAve_D_Terminals\n");

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
                    // set up arrays
                    depths = new double[size.intValue()];
                    lengths = new double[size.intValue()];
                    functions = new double[size.intValue()];
                    terminals = new double[size.intValue()];
                    dTerminals = new double[size.intValue()];

                    // generate population                 
                    newPop = genProg.buildFirstPop(size.intValue(), genType);

                    int j = 0;
                    for (ArrayList<String> testProg : newPop) {
                        // count up size and shape details and store them
                        depths[j] = ProgramAnalyser.getDepthOfTree(testProg);
                        lengths[j] = ProgramAnalyser.getProgramLength(testProg);
                        functions[j] = ProgramAnalyser.getNoOfFunctions(testProg, funcs);
                        terminals[j] = ProgramAnalyser.getNoOfTerminals(testProg, terms);
                        dTerminals[j] = ProgramAnalyser.getDistinctTerminals(testProg, terms);
                        j++;
                    }

                    // store run details
                    dump.add(size.intValue() + "\t" + getAverage(depths) + "\t" + getAverage(lengths) + "\t" + getAverage(functions) + "\t" + getAverage(terminals) + "\t" + getAverage(dTerminals) + "\n");

                    // force a garbage collection
                    depths = null;
                    lengths = null;
                    functions = null;
                    terminals = null;
                    dTerminals = null;
                    newPop = null;
                    genProg = null;
                    System.gc();

                }

                // dump to file
                name = genType + "-S+S-" + size.toString() + "-" + modelName + ".txt";
                FileManip.doOutput(place, dump, name, false);

                // clear overheads
                dump = null;
                System.gc();
            }
        }

        // final output
        System.out.println("STARTING POPS SIZE AND SHAPE ANALYSIS COMPLETE!");
    }

    /**
     * Calculates and returns the average value of a collection of data
     * @param rawData the raw data
     * @return the average of the data
     */
    public static double getAverage(double[] rawData) {

        double ave = 0;
        double tot = 0;
        int len = rawData.length;

        for (int i = 0; i < len; i++) {
            tot = tot + rawData[i];
        }

        ave = tot / len;
        return ave;
    }

    /**
     * Returns the maximum value from a collection of data
     * @param rawData The collection of data
     * @return the maximum value
     */
    public static double getMax(double[] rawData) {
        double max = 0;
        int len = rawData.length;
        for (int i = 0; i < len; i++) {
            if (rawData[i] > max) {
                max = rawData[i];
            }
        }
        return max;
    }

    /**
     * Returns the minimum value from a collection of data
     * @param rawData The collection of data
     * @return The minimum value
     */
    public static double getMin(double[] rawData) {
        double min = 1000000;
        int len = rawData.length;
        for (int i = 0; i < len; i++) {
            if (rawData[i] < min) {
                min = rawData[i];
            }
        }
        return min;
    }
}

