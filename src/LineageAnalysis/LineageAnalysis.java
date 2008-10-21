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
import java.util.ArrayList;
import java.io.File;
import net.sf.javabdd.*;

/**
 * The lineage analysis class provides functionality to provide post GP run 
 * analysis of crossovers
 * @author Lawrence Beadle
 */
public class LineageAnalysis {
    
    private FamilyStorage fStore;
    private Scorer scorer;
    private int epoch, tn, bn, fibi, fibs, fibd, bifs, bifd, bd, fsbs, u;
    private ArrayList<String> output, outputScores, metrics, positives, semantics, dataIn, p1, p2, c1, c2;
    private ArrayList<String> positiveSemantics, negatives, negativeSemantics;
    private int gens;
    private SemanticModule semMod;
    private File cDir;
    private ArrayList<String> functions, terminals, eveTrace;
    
    /**
     * Lineage Analysis constructor
     * @param fS The familyStorage Object concerned
     * @param s The scorer model used
     * @param dIn The input data
     * @param sMod The semantic module used
     * @param epochNumber the Run number
     * @param generations The number of generations
     * @param cD The directory to dump data to
     * @param funcs the functions used
     * @param terms the terminals used
     */
    public LineageAnalysis(FamilyStorage fS, Scorer s, ArrayList<String> dIn, SemanticModule sMod, int epochNumber, int generations, File cD, ArrayList<String> funcs, ArrayList<String> terms) {
        System.out.println("LINEAGE ANALYSIS RUNNING...");
        fStore = fS;
        scorer = s;
        semMod = sMod;
        epoch = epochNumber;
        output = new ArrayList<String>();
        outputScores = new ArrayList<String>();
        metrics = new ArrayList<String>();
        positives = new ArrayList<String>();
        negatives = new ArrayList<String>();
        negativeSemantics = new ArrayList<String>();
        semantics = new ArrayList<String>();
        positiveSemantics = new ArrayList<String>();
        eveTrace = new ArrayList<String>();
        gens = generations;
        cDir = new File(cD.getPath() + "/Results");
        dataIn = dIn;
        functions = funcs;
        terminals = terms;
        
        // run analysis
        this.countCategories();
        this.eveAnalysis();
        this.dumpLineageAnalysis();
        
        //this.dumpLineageFile();
        System.out.println("LINEAGE ANALYSIS COMPLETE");
    }
    
    /**
     * Categorises scores patterns
     */
    public void countCategories() {
        // add titles to output file
        output.add("Generation\tTrue-Neutral\tBehaviour-Neutral\tBoth-Increase\tFitness-Increase\tFitness-Increase-Behaviour-Decrease\tBehaviour-Increase\tBehaviour-Increase-Fitness-Decrease\tBoth-Decrease\tFitness-Same-Behaviour-Same\tUnclassified\n");
        outputScores.add("G\tP1F\tP1B\tP2F\tP2B\tCF\tCB\n");
        metrics.add("G\tP1D\tP1L\tP1Fs\tP1Ts\tP1DTs\tP1Fit\tP1Behav\tP2D\tP2L\tP2Fs\tP2Ts\tP2DTs\tP2Fit\tP2Behav\tCD\tCL\tCFs\tCTs\tCDTs\tCFit\tCBehav\tMod_D\n");
        positives.add("G\tP1D\tP1L\tP1Fs\tP1Ts\tP1DTs\tP1Fit\tP1Behav\tP2D\tP2L\tP2Fs\tP2Ts\tP2DTs\tP2Fit\tP2Behav\tCD\tCL\tCFs\tCTs\tCDTs\tCFit\tCBehav\tMod_D\n");
        negatives.add("G\tP1D\tP1L\tP1Fs\tP1Ts\tP1DTs\tP1Fit\tP1Behav\tP2D\tP2L\tP2Fs\tP2Ts\tP2DTs\tP2Fit\tP2Behav\tCD\tCL\tCFs\tCTs\tCDTs\tCFit\tCBehav\tMod_D\n");
        semantics.add("G\tP1PT\tP1SC\tP1NC\tP2PT\tP2SC\tP2NC\tCPT\tCSC\tCNC\n");
        positiveSemantics.add("G\tP1PT\tP1SC\tP1NC\tP2PT\tP2SC\tP2NC\tCPT\tCSC\tCNC\n");
        negativeSemantics.add("G\tP1PT\tP1SC\tP1NC\tP2PT\tP2SC\tP2NC\tCPT\tCSC\tCNC\n");
        // cycle through generations
        for(int i = 1; i<=gens; i++) {
            tn = 0;
            bn = 0;
            fibi = 0;
            fibs = 0;
            fibd = 0;
            bifs = 0;
            bifd = 0;
            bd = 0;
            fsbs = 0;
            u = 0;
            ArrayList<Geneology> toDo = fStore.getGeneologiesByGeneration(i);
            for (Geneology thisG : toDo) {
                p1 = fStore.getProgram(thisG.getP1());
                p2 = fStore.getProgram(thisG.getP2());
                c1 = fStore.getProgram(thisG.getC1());
                c2 = fStore.getProgram(thisG.getC2());
                // get all the scores
                scorer.setScoreMethodType(1, semMod);
                double fScoreP1 = scorer.getScore(dataIn, p1);
                double fScoreP2 = scorer.getScore(dataIn, p2);
                double fScoreC1 = scorer.getScore(dataIn, c1);
                double fScoreC2 = scorer.getScore(dataIn, c2);
                scorer.setScoreMethodType(2, semMod);
                double bScoreP1 = scorer.getScore(dataIn, p1);
                double bScoreP2 = scorer.getScore(dataIn, p2);
                double bScoreC1 = scorer.getScore(dataIn, c1);
                double bScoreC2 = scorer.getScore(dataIn, c2);
                // scores output
                outputScores.add(i + "\t" + fScoreP1 + "\t" + bScoreP1 + "\t" + fScoreP2 + "\t" + bScoreP2 + "\t" + fScoreC1 + "\t" + bScoreC1 + "\n");
                outputScores.add(i + "\t" + fScoreP1 + "\t" + bScoreP1 + "\t" + fScoreP2 + "\t" + bScoreP2 + "\t" + fScoreC2 + "\t" + bScoreC2 + "\n");
                // metrics output
                int p1D = ProgramAnalyser.getDepthOfTree(p1);
                int p1L = ProgramAnalyser.getProgramLength(p1);
                int p1Fs = ProgramAnalyser.getNoOfFunctions(p1, functions);
                int p1Ts = ProgramAnalyser.getNoOfTerminals(p1, terminals);
                int p1DTs = ProgramAnalyser.getDistinctTerminals(p1, terminals);
                int p2D = ProgramAnalyser.getDepthOfTree(p2);
                int p2L = ProgramAnalyser.getProgramLength(p2);
                int p2Fs = ProgramAnalyser.getNoOfFunctions(p2, functions);
                int p2Ts = ProgramAnalyser.getNoOfTerminals(p2, terminals);
                int p2DTs = ProgramAnalyser.getDistinctTerminals(p2, terminals);
                int cD1 = ProgramAnalyser.getDepthOfTree(c1);
                int cL1 = ProgramAnalyser.getProgramLength(c1);
                int cFs1 = ProgramAnalyser.getNoOfFunctions(c1, functions);
                int cTs1 = ProgramAnalyser.getNoOfTerminals(c1, terminals);
                int cDTs1 = ProgramAnalyser.getDistinctTerminals(c1, terminals);
                int cD2 = ProgramAnalyser.getDepthOfTree(c2);
                int cL2 = ProgramAnalyser.getProgramLength(c2);
                int cFs2 = ProgramAnalyser.getNoOfFunctions(c2, functions);
                int cTs2 = ProgramAnalyser.getNoOfTerminals(c2, terminals);
                int cDTs2 = ProgramAnalyser.getDistinctTerminals(c2, terminals);
                int modD1 = ProgramAnalyser.getModificationDepth(p1, c1);
                int modD2 = ProgramAnalyser.getModificationDepth(p2, c2);
                String met1 = i+"\t"+p1D+"\t"+p1L+"\t"+p1Fs+"\t"+p1Ts+"\t"+p1DTs+"\t"+fScoreP1+"\t"+bScoreP1;
                met1 = met1 + "\t"+p2D+"\t"+p2L+"\t"+p2Fs+"\t"+p2Ts+"\t"+p2DTs+"\t"+fScoreP2+"\t"+bScoreP2;
                met1 = met1 + "\t"+cD1+"\t"+cL1+"\t"+cFs1+"\t"+cTs1+"\t"+cDTs1+"\t"+fScoreC1+"\t"+bScoreC1+"\t"+modD1+"\n";
                String met2 = i+"\t"+p1D+"\t"+p1L+"\t"+p1Fs+"\t"+p1Ts+"\t"+p1DTs+"\t"+fScoreP1+"\t"+bScoreP1;
                met2 = met2 + "\t"+p2D+"\t"+p2L+"\t"+p2Fs+"\t"+p2Ts+"\t"+p2DTs+"\t"+fScoreP2+"\t"+bScoreP2;
                met2 = met2 + "\t"+cD2+"\t"+cL2+"\t"+cFs2+"\t"+cTs2+"\t"+cDTs2+"\t"+fScoreC2+"\t"+bScoreC2+"\t"+modD2+"\n";
                metrics.add(met1);
                metrics.add(met2);
                // semantic output
                semMod.start();
                BDD p1BDD = semMod.createRep(p1).getBDD();
                BDD p2BDD = semMod.createRep(p2).getBDD();
                BDD c1BDD = semMod.createRep(c1).getBDD();
                BDD c2BDD = semMod.createRep(c2).getBDD();
                int nodesP1 = p1BDD.nodeCount();
                double pTTP1 = p1BDD.pathCount();
                double sCP1 = p1BDD.satCount();
                int nodesP2 = p2BDD.nodeCount();
                double pTTP2 = p2BDD.pathCount();
                double sCP2 = p2BDD.satCount();
                int nodesC1 = c1BDD.nodeCount();
                double pTTC1 = c1BDD.pathCount();
                double sCC1 = c1BDD.satCount();
                int nodesC2 = c2BDD.nodeCount();
                double pTTC2 = c2BDD.pathCount();
                double sCC2 = c2BDD.satCount();
                String sem1 = i+"\t"+pTTP1+"\t"+sCP1+"\t"+nodesP1+"\t"+pTTP2+"\t"+sCP2+"\t"+nodesP2+"\t"+pTTC1+"\t"+sCC1+"\t"+nodesC1+"\n";
                String sem2 = i+"\t"+pTTP1+"\t"+sCP1+"\t"+nodesP1+"\t"+pTTP2+"\t"+sCP2+"\t"+nodesP2+"\t"+pTTC2+"\t"+sCC2+"\t"+nodesC2+"\n";
                semantics.add(sem1);
                semantics.add(sem2);
                semMod.finish();
                // marks state changes
                // CONSIDER C1
                // test for truly neutral
                if (c1.equals(p1)) {
                    tn++;
                    // test for behavioural neutrality
                } else if(!ProgramAnalyser.testStateChange(p1, c1, semMod)) {
                    bn++;
                } else {
                    // test for all other combinations
                    if(fScoreC1<fScoreP1&&bScoreC1<bScoreP1) {
                        fibi++;
                        positives.add(met1);
                        positiveSemantics.add(sem1);
                    } else if(fScoreC1<fScoreP1&&bScoreC1==bScoreP1) {
                        fibs++;
                    } else if(fScoreC1<fScoreP1&&bScoreC1>bScoreP1) {
                        fibd++;
                    } else if(fScoreC1==fScoreP1&&bScoreC1<bScoreP1) {
                        bifs++;
                    } else if(fScoreC1>fScoreP1&&bScoreC1<bScoreP1) {
                        bifd++;
                    } else if(fScoreC1>fScoreP1&&bScoreC1>bScoreP1) {
                        bd++;
                        negatives.add(met1);
                        negativeSemantics.add(sem1);
                    } else if(fScoreC1==fScoreP1&&bScoreC1==bScoreP1) {
                        fsbs++;
                    } else {
                        u++;
                    }
                }
                // CONSIDER C2
                // test for truly neutral
                if (c2.equals(p2)) {
                    tn++;
                    // test for behavioural neutrality
                } else if(!ProgramAnalyser.testStateChange(p2, c2, semMod)) {
                    bn++;
                } else {
                    // test for all other combinations
                    if(fScoreC2<fScoreP2&&bScoreC2<bScoreP2) {
                        fibi++;
                        positives.add(met2);
                        positiveSemantics.add(sem2);
                    } else if(fScoreC2<fScoreP2&&bScoreC2==bScoreP2) {
                        fibs++;
                    } else if(fScoreC2<fScoreP2&&bScoreC2>bScoreP2) {
                        fibd++;
                    } else if(fScoreC2==fScoreP2&&bScoreC2<bScoreP2) {
                        bifs++;
                    } else if(fScoreC2>fScoreP2&&bScoreC2<bScoreP2) {
                        bifd++;
                    } else if(fScoreC2>fScoreP2&&bScoreC2>bScoreP2) {
                        bd++;
                        negatives.add(met2);
                        negativeSemantics.add(sem2);
                    } else if(fScoreC2==fScoreP2&&bScoreC2==bScoreP2) {
                        fsbs++;
                    } else {
                        u++;
                    }
                }
            }
            output.add(i + "\t" + tn + "\t" + bn + "\t" + fibi + "\t" + fibs + "\t" + fibd + "\t" + bifs + "\t" + bifd + "\t" + bd + "\t" + fsbs + "\t" + u + "\n");
            System.out.println("Analysed " + i + " out of " + gens + " generations...");
        }
    }
    
    /**
     * Dumps lineage analysis information to file
     */
    public void dumpLineageAnalysis() {
        String negativeSemanticsName = "negativesemantics-E" + epoch + ".txt";
        FileManip.doOutput(cDir, negativeSemantics, negativeSemanticsName, false);
        String negativesName = "negativesanalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, negatives, negativesName, false);
        String semanticsName = "semanticanalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, semantics, semanticsName, false);
        String positiveSemanticsName = "positivesemanticanalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, positiveSemantics, positiveSemanticsName, false);
        String positivesName = "positivesanalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, positives, positivesName, false);
        String metricsName = "metricssanalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, metrics, metricsName, false);
        String sName = "scoreanalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, outputScores, sName, false);
        String fName = "lineageAnalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, output, fName, false);
        String eveName = "eveAnalysis-E" + epoch + ".txt";
        FileManip.doOutput(cDir, eveTrace, eveName, false);
    }
    
    /**
     * Calls the fStore dump lineage to file method
     */
    public void dumpLineageFile() {
        fStore.dumpLineageToFile(cDir);
    }
    
    /**
     * Performs an eve Analysis of the GP crossovers
     */
    public void eveAnalysis() {
        eveTrace.add("Generation\tProgID\tFitness\tBScore\tSatCount\tNodeCount\tSimilarity%\tWaste%\tDepth\tLength\tFunctions\tTerminals\tDTerminals\tModD\n");
        ArrayList<Geneology> finalProgs = fStore.getGeneologiesByGeneration(gens);
        for (Geneology thisG : finalProgs) {
            ArrayList<String> prog1 = fStore.getProgram(thisG.getC1());
            ArrayList<String> prog2 = fStore.getProgram(thisG.getC2());
            // check C1 first            
            // trace the family
            traceParent(thisG.getC1(), prog1, gens);
            // check C2
            // trace the family
            traceParent(thisG.getC2(), prog2, gens);
        }
    }
    
    private void traceParent(int hashID, ArrayList<String> base, int generation) {
        // retrieve the program in question
        ArrayList<String> p = fStore.getProgram(hashID);
        // do the scoring for semantic and then input-output
        scorer.setScoreMethodType(2, semMod);
        double scB = scorer.getScore(dataIn, p);
        scorer.setScoreMethodType(1, semMod);
        double scF = scorer.getScore(dataIn, p);
        // do bdd analysis
        semMod.start();
        BDD pBDD = semMod.createRep(p).getBDD();
        double satCount = pBDD.satCount();
        int nodes = pBDD.nodeCount();
        ArrayList<String> optimised = semMod.repToCode(new BehaviourRepresentation(pBDD));
        int waste;
        try {
            waste = ProgramAnalyser.getProgramLength(optimised);
        } catch(NullPointerException e) {
            System.out.println("CATCHER = " + optimised);
        }
        waste = ProgramAnalyser.getProgramLength(p) - ProgramAnalyser.getProgramLength(optimised);
        if(waste<0) {
            waste = 0;
        }
        double wastePerc = (((double) waste)/((double) ProgramAnalyser.getProgramLength(p))) * 100;
        semMod.finish();
        // pull out metrics
        double simPerc = ProgramAnalyser.getSyntaxPercentageSimilarity(p, base);
        int d = ProgramAnalyser.getDepthOfTree(p);
        int l = ProgramAnalyser.getProgramLength(p);
        int f = ProgramAnalyser.getNoOfFunctions(p, functions);
        int t = ProgramAnalyser.getNoOfTerminals(p, terminals);
        int dt = ProgramAnalyser.getDistinctTerminals(p, terminals);
        // pull out parent for modification depth
        int parID = fStore.findParent(hashID);
        int modD;
        if (parID > 0) {
            ArrayList<String> par = fStore.getProgram(parID);
            modD = ProgramAnalyser.getModificationDepth(p, par);
        } else {
            modD = 20;
        }
        // add to results
        eveTrace.add(generation+"\t"+hashID+"\t"+scF+"\t"+scB+"\t"+satCount+"\t"+nodes+"\t"+simPerc+"\t"+wastePerc+"\t"+d+"\t"+l+"\t"+f+"\t"+t+"\t"+dt+"\t"+modD+"\n");
        // find parent
        int parent = fStore.findParent(hashID);
        if(parent!=0) {
            traceParent(parent, base, (generation-1));
        }
    }
}
