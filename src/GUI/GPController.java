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

package GUI;

import Core.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Provides the control mechanism for the GUI
 * @author Lawrence Beadle
 */
public class GPController {
    
    private GPView view;
    private GPModel model;
    private GPThread process2;
    private TimerThread process1;
    
    /**
     * Creates a new instance of GPController
     * @param viewX The associates GUI the model will control
     * @param modelX The associated GP model the controller will control
     */
    public GPController(GPView viewX, GPModel modelX) {
        
        view = viewX;
        model = modelX;
        
        // add listeners
        view.addLoadListener(new LoadListener());        
        view.addSaveListener(new SaveListener());
        view.addExitListener(new ExitListener());
        view.addGPListener(new GPListener());
        view.addClearListener(new ClearListener());
        
    }
    
    /////// inner class LoadEvent
    // set up load event listener
    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Load work HERE            
            
            // ------------------
        }
    }//end inner class MultiplyListener
    
    //////////////////////////////////////////// inner class SaveListener
    /**  Save Work
     */    
    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Save work in here
            
            //------------------------
        }
    }// end inner class SaveListener
    
    //////////////////////////////////////////// inner class ExitListener
    /**  Exit work
     */    
    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("EXITING SYSTEM...");
            System.exit(1);
        }
    }// end inner class GPListener
    
    //////////////////////////////////////////// inner class GPListener
    /**  GP Code in here
     */    
    class GPListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // GP Control work in here
            
            // disable start buttone whilst process is running
            view.setStartButtonEnabled(false);
            
            // -----------------------------------------------------------------
            // set up another thread to do the progress monitoring
            process1 = new TimerThread(Thread.MAX_PRIORITY);
            process1.start();
            process2 = new GPThread(Thread.MIN_PRIORITY);
            process2.start();            
            // -----------------------------------------------------------------
            
            // re-enable start buttones
            view.setStartButtonEnabled(true);
            
            //------------------------
        }
    }// end inner class GPListener

    //////////////////////////////////////////// inner class ClearListener
    /**
     *   Reset View.
     */    
    class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // clear even work in here
            view.setProgDir("");
            view.setCrossOverValue("");
            view.setMutationValue("");
            view.setCStateCheckerValue("False");
            view.setMStateCheckerValue("False");
            view.setGenerationsValue("");
            view.setRunsValue("");
            view.setPopSizeValue("");
            view.setElitesValue("");
            view.setReproductionValue("");
            view.setCrossoverMethodValue("Koza Crossover");
            view.setSeletionMethodValue("Tournament");
            view.setScoreTypeMethodValue("Input-Output");
            view.setStatOutput("");
            view.setProgOutput("");
            view.setStateOutput("");
            model = new GPModel();
            //------------------------
        }
    }// end inner class ClearListener
    
    // inner class for timer timer thread
    class GPThread extends Thread {        
        
        GPThread(int priority) {
            this.setPriority(priority);
        }
        
        public void run() {            
            
            // GP Control work in here
            
            // delete old output files
            FileManip.deleteOld(view.getFileDir(), "GP-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-State-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-PA-DEPTH-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-PA-NODE-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-PA-TERMINAL-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-PA-DISTINCT-TERMINAL-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-PA-Length-Stats-Output.txt");
            FileManip.deleteOld(view.getFileDir(), "GP-Program-Output.txt");

            // set up variables
            String modelName = view.getModelName();
            // starting pop type
            String genType = "";
            if(view.getStartingPopType().equalsIgnoreCase("Ramped Half & Half (D2:6)")) {
                genType = "RH+H";
            } else if(view.getStartingPopType().equalsIgnoreCase("Grow (D6)")) {
                genType = "Grow";
            } else if(view.getStartingPopType().equalsIgnoreCase("Full (D6)")) {
                genType = "Full";
            } else if(view.getStartingPopType().equalsIgnoreCase("State Differential Boolean")) {
                genType = "SDIB";
            } else if(view.getStartingPopType().equalsIgnoreCase("Random")) {
                genType = "Random";
            } else if(view.getStartingPopType().equalsIgnoreCase("H&H (D6)")) {
                genType = "H+H";
            } else if(view.getStartingPopType().equalsIgnoreCase("State Differential Ant")) {
                genType = "SDIA";
            }
            // number of runs
            String runsTxt = view.getRunsValue();
            int runs = 0;
            try {
                runs = Integer.parseInt(runsTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: runs = NumberFormatException - Has been replaced by 0");
            }
            // pop size
            String popSizeTxt = view.getPopSizeValue();
            int popSize = 0;
            try {
                popSize = Integer.parseInt(popSizeTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: population size = NumberFormatException - Has been replaced by 0");
            }
            // number of generations
            String gensTxt = view.getGenerationsValue();
            int gens = 0;
            try {
                gens = Integer.parseInt(gensTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: generations = NumberFormatException - Has been replaced by 0");
            }
            // get number to be reproduced
            String reproductionTxt = view.getReprodutionValue();
            int reproduction = 0;
            try {
                reproduction = Integer.parseInt(reproductionTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: reproduction = NumberFormatException - Has been replaced by 0");
            }
            // number of elites
            String elitesTxt = view.getElitesValue();
            int elites = 0;
            try {
                elites = Integer.parseInt(elitesTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: elites = NumberFormatException - Has been replaced by 0");
            }
            // prob of crossover
            String pCrossTxt = view.getCrossOverValue();
            double pCross = 0;
            try {
                pCross = Double.parseDouble(pCrossTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: probability of crossover = NumberFormatException - Has been replaced by 0");
            }
            // prob of mutation
            String pMutTxt = view.getMutationValue();
            double pMut = 0;
            try {
                Double.parseDouble(pMutTxt);
            } catch(NumberFormatException e) {
                System.out.println("WARNING: probability of mutation = NumberFormatException - Has been replaced by 0");
            }
            // sort out crossover method
            int cOMethod = 0;
            if(view.getCrossOverMethodValue().equalsIgnoreCase("Single Point Crossover")) {
                cOMethod = 1;
            } else if(view.getCrossOverMethodValue().equalsIgnoreCase("Standard Crossover (Uniform Distribution)")) {
                cOMethod = 2;
            } else if(view.getCrossOverMethodValue().equalsIgnoreCase("Koza Crossover")) {
                cOMethod = 3;
            }
            // sort out scoring method
            int scoreType = 0;
            if(view.getScoreTypeValue().equalsIgnoreCase("Input-Output")) {
                scoreType = 1;
            } else if(view.getScoreTypeValue().equalsIgnoreCase("Semantic")) {
                scoreType = 2;
            }
            // sort out selection method
            int sOMethod = 0;
            if(view.getSelectionMethodValue().equalsIgnoreCase("Total Tournament (Care with Pop size)")) {
                sOMethod = 1;
            } else if(view.getSelectionMethodValue().equalsIgnoreCase("Fitness Proportionate")) {
                sOMethod = 2;
            } else if(view.getSelectionMethodValue().equalsIgnoreCase("Tournament T7")) {
                sOMethod = 3;
            } else if(view.getSelectionMethodValue().equalsIgnoreCase("Ranked Selection")) {
                sOMethod = 4;
            } else if(view.getSelectionMethodValue().equalsIgnoreCase("Tournament T3")) {
                sOMethod = 5;
            }
            // sort out crossover state checker
            boolean cChecker;
            if(view.getCStateCheckerValue().equalsIgnoreCase("True")) {
                cChecker = true;
            } else {
                cChecker = false;
            }
            // sort out crossover state checker
            boolean mChecker;
            if(view.getMStateCheckerValue().equalsIgnoreCase("True")) {
                mChecker = true;
            } else {
                mChecker = false;
            }
            
            // clear output panes
            view.setStatOutput("");
            view.setProgOutput("");
            view.setStateOutput("");         
            
            // drag in input file - THIS WILL NEED TO BE MODIFIED
            model.loadRawData(view.getFileDir(), view.getFName());           
            
            // do GP Run
            model.doGPRun(modelName, runs, popSize, genType, gens, elites, reproduction, pCross, pMut, cChecker, mChecker, cOMethod, sOMethod, scoreType, view.getFileDir());
            
            // load values into output panes
            // stats pane
            ArrayList<String> toUpload = FileManip.loadInput(view.getFileDir(), "GP-Stats-Output.txt");
            String upload = new String();
            for(String s: toUpload) {
                upload = upload + "\n" + s;
            }
            view.setStatOutput(upload);
            
            System.out.println("LOADED STATS");
            
            // program pane
            toUpload = FileManip.loadInput(view.getFileDir(), "GP-Program-Output.txt");
            upload = new String();
            for(String s: toUpload) {
                upload = upload + "\n" + s;
            }
            view.setProgOutput(upload);
            
            System.out.println("LOADED PROGRAMS");
            
            // state pane
            toUpload = FileManip.loadInput(view.getFileDir(), "GP-State-Stats-Output.txt");
            upload = new String();
            for(String s: toUpload) {
                upload = upload + "\n" + s;
            }
            view.setStateOutput(upload);
            
            System.out.println("LOADED STATES OUPUT");
            
            // clear memory
            toUpload = null;
            upload = null;
            
            process1.end();
        }
    }
    /// end inner class
    
    // inner class for timer timer thread
    class TimerThread extends Thread {        
        
        boolean contCheck = true;
        
        TimerThread(int priority) {
            this.setPriority(priority);
        }
        
        public void end() {
            try {
                SwingUtilities.invokeAndWait(new GUIThread());
            } catch(InterruptedException z) {
                // do nothing
            } catch(java.lang.reflect.InvocationTargetException y) {
                // do nothing
            }
            contCheck = false;
        }
        
        @SuppressWarnings("static-access")
        public void run() {            
            
            // timer control work here
            while(contCheck==true) {
                try {
                    SwingUtilities.invokeAndWait(new GUIThread());
                } catch(InterruptedException z) {
                    // do nothing
                } catch(java.lang.reflect.InvocationTargetException y) {
                    // do nothing
                }
                try {
                    process1.sleep(2000);
                } catch(InterruptedException z) {
                    // do nothing
                } catch(IllegalMonitorStateException f) {
                    // do nothing
                }
            }
            
        }
    }
    /// end inner class
    
    // runnable GUI Thread updater
    class GUIThread implements Runnable {
        
        public void run() {
            
            // set variables
            double gensX, gX, checker;
            // decide if it is counting runs or generations
            if(view.getRunsValue().equalsIgnoreCase("1")) {
                gensX = Double.parseDouble(view.getGenerationsValue());
                gX = ((double) model.getGenerationNumber());
                checker = (gX/gensX)*100;
            } else {
                gensX = Double.parseDouble(view.getRunsValue());
                gX = ((double) model.getEpochNumber());
                checker = (gX/gensX)*100;
            }
            int progressX = (int) checker;
            System.out.println("PROGRESS = " + progressX + "%");
            view.setProgress(progressX, "Run is " + progressX + "% complete");
        }
        
    } // end of inner class
}
