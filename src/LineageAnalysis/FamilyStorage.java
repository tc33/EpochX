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

package LineageAnalysis;

import CoreN.*;
import java.util.*;
import java.io.*;

/**
 * This class stores the crossover parent and child information for later lineage 
 * analysis
 * @author Lawrence Beadle
 */
public class FamilyStorage {
    
    private HashMap<Integer, ArrayList<String>> progStore;
    private ArrayList<Geneology> geneologies;
    
    /**
     * General FamilyStorage constructor - instantiaites collections
     */
    public FamilyStorage() {
        progStore = new HashMap<Integer, ArrayList<String>>();
        geneologies = new ArrayList<Geneology>();
    }
    
    /**
     * Stores programs and their unique hashIdentity key in a hash map
     * @param hashKey The unique hash key
     * @param prog The program to store
     */
    public void storeProg(int hashKey, ArrayList<String> prog) {
        progStore.put(new Integer(hashKey), prog);
    }
    
    /**
     * Gets a program from the program store via its unique hash key
     * @param id the hash key
     * @return The associated program
     */
    public ArrayList<String> getProgram(int id) {
        return progStore.get(id);
    }
    
    /**
     * Stores a geneology object (two paresnt 2 children)
     * @param fam The Geneology object to be sorted
     */
    public void storeGeneology(Geneology fam) {
        geneologies.add(fam);
    }
    
    /**
     * Returns the collection of Geneology objects
     * @return All the Geneologies stored
     */
    public ArrayList<Geneology> getGeneologies() {
        return geneologies;
    }
    
    /**
     * Rtuens the Geneologies by specific generation
     * @param generation The generation number
     * @return The Geneologies from the specified generation
     */
    public ArrayList<Geneology> getGeneologiesByGeneration(int generation) {
        ArrayList<Geneology> toReturn = new ArrayList<Geneology>();
        for(Geneology thisG: geneologies) {
            if(thisG.getGeneration()==generation) {
                toReturn.add(thisG);
            }
        }
        return toReturn;
    }
    
    /**
     * Returns the program store
     * @return A HashMap conatining all the programs from the GP run
     */
    public HashMap<Integer, ArrayList<String>> getProgStore() {
        return progStore;
    }
    
    /**
     * Stores a geneology object based on all the required information
     * @param parent1 parent 1 program
     * @param parent2 parent 2 program
     * @param child1 child 1 program
     * @param child2 child 2 program
     * @param g the generation number
     */
    public void storeRecord(ArrayList<String> parent1, ArrayList<String> parent2, ArrayList<String> child1, ArrayList<String> child2, int g) {
        int k1 = System.identityHashCode(parent1);
        int k2 = System.identityHashCode(parent2);
        int k3 = System.identityHashCode(child1);
        int k4 = System.identityHashCode(child2);
        Geneology thisG = new Geneology(k1, k2, k3, k4, g);
        this.storeGeneology(thisG);
        this.storeProg(k1, parent1);
        this.storeProg(k2, parent2);
        this.storeProg(k3, child1);
        this.storeProg(k4, child2);
    }
    
    /**
     * Dumps all lineage information to txt file
     * @param dir The directory to dump the file in
     */
    public void dumpLineageToFile(File dir) {
        ArrayList<String> toDump = new ArrayList<String>();
        int key;
        ArrayList<String> thisProg = new ArrayList<String>();
        String assemble;
        for(Geneology g: geneologies) {
            // assemble parent 1
            assemble = new String();
            key = g.getP1();
            thisProg = progStore.get(new Integer(key));
            for(String p: thisProg) {
                assemble = assemble + " " + p;
            }
            toDump.add(assemble + "\n");
            // assemble parent 2
            assemble = new String();
            key = g.getP2();
            thisProg = progStore.get(new Integer(key));
            for(String p: thisProg) {
                assemble = assemble + " " + p;
            }
            toDump.add(assemble + "\n");
            // assemble child 1
            assemble = new String();
            key = g.getC1();
            thisProg = progStore.get(new Integer(key));
            for(String p: thisProg) {
                assemble = assemble + " " + p;
            }
            toDump.add(assemble + "\n");
            // assemble child 2
            assemble = new String();
            key = g.getC2();
            thisProg = progStore.get(new Integer(key));
            for(String p: thisProg) {
                assemble = assemble + " " + p;
            }            
            toDump.add(assemble + "\n");
        }
        FileManip.doOutput(dir, toDump, "lineage.txt", true);
        //System.out.println("LINEAGE FILE DUMPED TO: lineage.txt");
    }
    
    /**
     * Finds the parent of a specific child
     * @param child The identityHashKey of the child
     * @return The identityHashKey of the parent
     */
    public int findParent(int child) {
        for(Geneology g: geneologies) {
            if(g.getC1()==child) {
                return g.getP1();
            }
            if(g.getC2()==child) {
                return g.getP2();
            }
        }
        return 0;
    }    
}
