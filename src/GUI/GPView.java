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

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import core.*;
import java.io.*;

/**
 * Privides the GUI for the Epoch X GP Software
 * @author Lawrence Beadle
 */
public class GPView extends JFrame implements ActionListener {
    
    private JTabbedPane tabBase = new JTabbedPane();
    private JMenuItem job1, job2, open, save, exit, basicOps;
    private JMenu tools;
    private JMenuBar menuBar = new JMenuBar();
    private JTextField co, mu, gens, runs, progID, elites, pop, reproduction;
    private JTextArea statOutput, progOutput, stateOutput;
    private JComboBox rsc, msc, cOM, sOM, sPop, modelID, sTypeBox;
    private JPanel statsO, stateO, progsO, input, startPop, gPParams, fileControl, aParams, startRun;
    private JButton cFile, startButton;
    private GPModel model;
    private File fileToLoad;
    private String fName;
    private ProgressMonitor progBar;
    
    /**
     * Creates a new instance of GPView
     * @param base The base GPModel to be controlled via the interface
     */
    public GPView(GPModel base) {
        
        // Create title
        super("Epoch X Genetic Programming Analyser V 1.0");
        
        // link model object
        model = base;
        
        // Resize frame
        this.setBounds(0,0,1024,800);
        
        // Create pane for input section ---------------------------------------
        
        // set up basic panels
        input = new JPanel();
        startPop = new JPanel();
        gPParams = new JPanel();
        fileControl = new JPanel();
        aParams = new JPanel();
        startRun = new JPanel();
        
        // Basic GP Params -----------------------------------------------------
        gPParams.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        gPParams.setBorder(BorderFactory.createTitledBorder("Basic Parameters"));
        
        // crossover label
        JLabel crossover = new JLabel("Probability of Crossover (0-1):  ");
        c.gridx = 0;
        c.gridy = 0;
        gPParams.add(crossover, c);
        
        // crossover text field
        co = new JTextField(10);
        c.gridx = 1;
        c.gridy = 0;
        gPParams.add(co, c);
        
        // mutation label
        JLabel mutation = new JLabel("Probability of Mutation (0-1):  ");
        c.gridx = 0;
        c.gridy = 1;
        gPParams.add(mutation, c);
        
        // mutation text field
        mu = new JTextField(10);
        c.gridx = 1;
        c.gridy = 1;
        gPParams.add(mu, c);
        
        // generations label
        JLabel generations = new JLabel("Generations:  ");
        c.gridx = 0;
        c.gridy = 2;
        gPParams.add(generations, c);
        
        // generations entry field
        gens = new JTextField(10);
        c.gridx = 1;
        c.gridy = 2;
        gPParams.add(gens, c);
        
        // runs label
        JLabel epochsL = new JLabel("Runs:  ");
        c.gridx = 0;
        c.gridy = 3;
        gPParams.add(epochsL, c);
        
        // runs entry field
        runs = new JTextField(10);
        c.gridx = 1;
        c.gridy = 3;
        gPParams.add(runs, c);
        
        // reproduction size label
        JLabel reprodL = new JLabel("Reproduction: ");
        c.gridx = 0;
        c.gridy = 4;
        gPParams.add(reprodL, c);
        
        // reproduction size entry field
        reproduction = new JTextField(10);
        c.gridx = 1;
        c.gridy = 4;
        gPParams.add(reproduction, c);
        
        // elites size label
        JLabel elitesL = new JLabel("Elites:  ");
        c.gridx = 0;
        c.gridy = 5;
        gPParams.add(elitesL, c);
        
        // elites size entry field
        elites = new JTextField(10);
        c.gridx = 1;
        c.gridy = 5;
        gPParams.add(elites, c);
        
        // population size label
        JLabel popsize = new JLabel("Population Size:  ");
        c.gridx = 0;
        c.gridy = 6;
        gPParams.add(popsize, c);
        
        // population size entry       
        pop = new JTextField(10);
        c.gridx = 1;
        c.gridy = 6;
        gPParams.add(pop, c);
        // ---------------------------------------------------------------------
        
        // File Control --------------------------------------------------------
        fileControl.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        fileControl.setBorder(BorderFactory.createTitledBorder("FileControl"));
        
        // Program Input load Label
        JLabel modelInput = new JLabel("Model to Use:  ");
        c.gridx = 0;
        c.gridy = 0;
        fileControl.add(modelInput, c);
        
        // prog input id
        File toL = new File("build/classes/Models");
        String[] fileNames = toL.list();
        // clear .class of file names
        for(int i = 0; i<fileNames.length; i++) {
            fileNames[i] = fileNames[i].replaceAll(".class", "");
        }
        modelID = new JComboBox(fileNames);
        c.gridx = 1;
        c.gridy = 0;
        fileControl.add(modelID, c);
        
        // Program Input load Label
        JLabel progInput = new JLabel("Input States File:  ");
        c.gridx = 0;
        c.gridy = 1;
        fileControl.add(progInput, c);
        
        // prog input id
        progID = new JTextField(20);
        c.gridx = 1;
        c.gridy = 1;
        fileControl.add(progID, c);
        
        // file chooser button
        cFile = new JButton("Choose File");
        cFile.addActionListener(this);
        c.gridx = 2;
        c.gridy = 1;
        fileControl.add(cFile, c);
        // ---------------------------------------------------------------------
        
        // Advanced Parameters -------------------------------------------------
        aParams.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        aParams.setBorder(BorderFactory.createTitledBorder("Adavanced Parameters"));
        
        // crossover method label
        JLabel crossovermethod = new JLabel("Crossover Method:  ");
        c.gridx = 0;
        c.gridy = 0;
        aParams.add(crossovermethod, c);
        
        // crossover type combo box
        String[] cOMe = new String[3];
        cOMe[0] = "Single Point Crossover";
        cOMe[1] = "Standard Crossover (Uniform Distribution)";
        cOMe[2] = "Koza Crossover";
        cOM = new JComboBox(cOMe);
        c.gridx = 1;
        c.gridy = 0;
        aParams.add(cOM, c);
        
        // scoring type label
        JLabel scoringType = new JLabel("Scoring Type:  ");
        c.gridx = 0;
        c.gridy = 1;
        aParams.add(scoringType, c);
        
        // selection type combo box
        String[] sType = new String[5];
        sType[0] = "Input-Output";
        sType[1] = "Semantic";
        sTypeBox = new JComboBox(sType);
        c.gridx = 1;
        c.gridy = 1;
        aParams.add(sTypeBox, c);
        
        // selection method label
        JLabel selectionmethod = new JLabel("Selection Method:  ");
        c.gridx = 0;
        c.gridy = 2;
        aParams.add(selectionmethod, c);
        
        // selection type combo box
        String[] sMe = new String[5];
        sMe[3] = "Total Tournament (Care with Pop size)";
        sMe[1] = "Fitness Proportionate";
        sMe[2] = "Ranked Selection";
        sMe[0] = "Tournament T7";
        sMe[4] = "Tournament T3";
        sOM = new JComboBox(sMe);
        c.gridx = 1;
        c.gridy = 2;
        aParams.add(sOM, c);
        
        // crossover state checker label
        JLabel cStateChecker = new JLabel("Run Crossover State Checker:  ");
        c.gridx = 0;
        c.gridy = 3;
        aParams.add(cStateChecker, c);
        
        // crossover state checker combo box
        String[] cTF = new String[2];
        cTF[1] = "True";
        cTF[0] = "False";
        rsc = new JComboBox(cTF);
        c.gridx = 1;
        c.gridy = 3;
        aParams.add(rsc, c);   
        
        // crossover state checker label
        JLabel mStateChecker = new JLabel("Run Mutation State Checker:  ");
        c.gridx = 0;
        c.gridy = 4;
        aParams.add(mStateChecker, c);
        
        // crossover state checker combo box
        String[] mTF = new String[2];
        mTF[1] = "True";
        mTF[0] = "False";
        msc = new JComboBox(mTF);
        c.gridx = 1;
        c.gridy = 4;
        aParams.add(msc, c); 
        
        // ---------------------------------------------------------------------
        
        // starting population panel -------------------------------------------
        startPop.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        startPop.setBorder(BorderFactory.createTitledBorder("Starting Population"));
        
        JLabel popL = new JLabel("Starting Pop Type:  ");
        c.gridx = 0;
        c.gridy = 0;
        startPop.add(popL, c);
        
        String[] popType = new String[7];
        popType[0] = "Ramped Half & Half (D2:6)";
        popType[1] = "Grow (D6)";
        popType[2] = "Full (D6)";
        popType[3] = "State Differential Boolean";
        popType[4] = "State Differential Ant";
        popType[5] = "H&H (D6)";
        popType[6] = "Random";
        sPop = new JComboBox(popType);
        c.gridx = 1;
        c.gridy = 0;
        startPop.add(sPop, c);
        // ---------------------------------------------------------------------
        
        // start button pane ---------------------------------------------------
        JPanel sBut = new JPanel();
        sBut.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        sBut.setBorder(BorderFactory.createTitledBorder("Start GP"));
        // button for starting GP
        startButton = new JButton("Start GP Run");
        c.gridx = 0;
        c.gridy = 0;
        sBut.add(startButton, c);
        //----------------------------------------------------------------------
        
        // sort out input pane layout ------------------------------------------
        input.setLayout(new GridLayout(3, 0, 10, 10));
        input.setBorder(BorderFactory.createTitledBorder("Input Variables"));
        // add start pop pane
        input.add(startPop);
        // add file control pane
        input.add(fileControl);
        // add GP Params pane
        input.add(gPParams);
        // add advance parameters pane
        input.add(aParams);
        // add blank pane before start button
        input.add(new JPanel());
        // add start button
        input.add(sBut);
        // ---------------------------------------------------------------------
        
        // set up progress monitor        
        progBar = new ProgressMonitor(input, "Progress Monitor", "Intialising...", 0, 100);
        progBar.setProgress(0);
        progBar.setMillisToDecideToPopup(100);
        progBar.setMillisToPopup(200);
        
        // Create pane for raw stats output ------------------------------------       
        statsO = new JPanel();        
        statsO.setBorder(BorderFactory.createTitledBorder("Stats Output"));
        
        // text area for left pane
        statOutput = new JTextArea(35,80);
        JScrollPane textScroll1 = new JScrollPane(statOutput);        
        statsO.add(textScroll1);
        
        //----------------------------------------------------------------------
        
        // Create Pane for program output ------------------------------
        
        progsO = new JPanel();
        progsO.setBorder(BorderFactory.createTitledBorder("Best Program Output"));
        
        // text area for left pane
        progOutput = new JTextArea(35,80);
        JScrollPane textScroll2 = new JScrollPane(progOutput);        
        progsO.add(textScroll2);
                
        //----------------------------------------------------------------------
        
        // Create pane for state output -----------------------------------
        
        stateO = new JPanel();
        stateO.setBorder(BorderFactory.createTitledBorder("State Outputs"));
        // text area for left pane
        stateOutput = new JTextArea(35,80);
        JScrollPane textScroll3 = new JScrollPane(stateOutput);        
        stateO.add(textScroll3);
        
        //----------------------------------------------------------------------  
        
        // Add a menu bar ------------------------------------------------------
        
        open = new JMenuItem("Open Saved Project");
        save = new JMenuItem("Save Project");
        exit = new JMenuItem("Exit System");
        
        basicOps = new JMenu("File");
                
        basicOps.add(open);
        basicOps.add(save);
        basicOps.add(exit);
        
        menuBar.add(basicOps);
        
        job1 = new JMenuItem("Do GP Run");
        job2 = new JMenuItem("Clear Data");
        
        tools = new JMenu("Tools");
        
        tools.add(job1);
        tools.add(job2);
        
        menuBar.add(tools);
               
        //----------------------------------------------------------------------
        
        // Add panes to tabs and tabs to frame ---------------------------------
        
        tabBase.add("Input Data", input);
        tabBase.add("Stats Output", statsO);
        tabBase.add("Winning Programs", progsO);
        tabBase.add("State Output", stateO);
        
        //----------------------------------------------------------------------
        
        // Add everything to JFrame --------------------------------------------
        
        this.setJMenuBar(menuBar);
        this.getContentPane().add(tabBase);
        
        //----------------------------------------------------------------------        
        
        // Define close button on the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
    }
    
    /**
     * Creates a load event listener
     * @param openFile call functionality to open a file
     */
    public void addLoadListener(ActionListener openFile) {
        open.addActionListener(openFile);
    }
    
    /**
     * Creates a save listener
     * @param saveFile Provides functionality to save a file
     */
    public void addSaveListener(ActionListener saveFile) {
        save.addActionListener(saveFile);
    }
    
    /**
     * Creates an exit listener
     * @param exitProg Provides functionaility to exit the program
     */
    public void addExitListener(ActionListener exitProg) {
        exit.addActionListener(exitProg);
    }
    
    /**
     * Creates a GP Listener
     * @param gPRun Provides fucntionality to operate the GP Model
     */
    public void addGPListener(ActionListener gPRun) {
        job1.addActionListener(gPRun);
        startButton.addActionListener(gPRun);
    }
    
    /**
     * Add as clear listener
     * @param clear Provides functionality to clear the information in the GUI
     */
    public void addClearListener(ActionListener clear) {
        job2.addActionListener(clear);
    }
    
    /**
     * Returns the crossover value
     * @return the crossover value
     */
    public String getCrossOverValue() {
        return co.getText();
    }
    
    /**
     * Sets the crossover value
     * @param newValue The new crossover value
     */
    public void setCrossOverValue(String newValue) {
        co.setText(newValue);
    }
    
    /**
     * Returns the mutation value
     * @return The mutation probability
     */
    public String getMutationValue() {
        return mu.getText();
    }
    
    /**
     * Sets the mutation value
     * @param newValue The new mutation value
     */
    public void setMutationValue(String newValue) {
        mu.setText(newValue);
    }
    
    /**
     * Returns crossover state checker parameters
     * @return the statechecker value
     */
    public String getCStateCheckerValue() {
        return (String) rsc.getSelectedItem();
    }
    
    /**
     * Sets the state checker value
     * @param newValue True to run the state checker value
     */
    public void setCStateCheckerValue(String newValue) {
        rsc.setSelectedItem(newValue);
    }
    
    /**
     * Returns mutation state checker parameters
     * @return the statechecker value
     */
    public String getMStateCheckerValue() {
        return (String) msc.getSelectedItem();
    }
    
    /**
     * Sets the mutation state checker value
     * @param newValue True to run the state checker value
     */
    public void setMStateCheckerValue(String newValue) {
        msc.setSelectedItem(newValue);
    }
    
    /**
     * Returns the number of generations from the interface
     * @return Number of generation per epoch
     */
    public String getGenerationsValue() {
        return gens.getText();
    }
    
    /**
     * Sets the genrations
     * @param newValue The new number of generations
     */
    public void setGenerationsValue(String newValue) {
        gens.setText(newValue);
    }
    
    /**
     * Returns the number of runs required in the run
     * @return The number of runs required
     */
    public String getRunsValue() {
        return runs.getText();
    }
    
    /**
     * Sets the number of runs field
     * @param val The value for the field for the number of runs
     */
    public void setRunsValue(String val) {
        runs.setText(val);
    }
    
    /**
     * Gets the population size
     * @return The population size
     */
    public String getPopSizeValue() {
        return pop.getText();
    }
    
    /**
     * Sets the population size
     * @param newValue The new population size
     */
    public void setPopSizeValue(String newValue) {
        pop.setText(newValue);
    }
    
    /**
     * Gets the number of elites
     * @return The number of elites
     */
    public String getElitesValue() {
        return elites.getText();
    }
    
    /**
     * Returns the number of program to be reproduced
     * @return Returns the number of program to be reproduced
     */
    public String getReprodutionValue() {
        return reproduction.getText();
    }
    
    /**
     * Sets the reproduction rate
     * @param newValue The new reproduction rate
     */
    public void setReproductionValue(String newValue) {
        reproduction.setText(newValue);
    }
    
    /**
     * Sets the number of elites
     * @param newValue The new number of elites
     */
    public void setElitesValue(String newValue) {
        elites.setText(newValue);
    }
    
    /**
     * Gets the crossover method
     * @return The crossover method id
     */
    public String getCrossOverMethodValue() {
        return (String) cOM.getSelectedItem();
    }
    
    /**
     * Sets the crossover method
     * @param newValue The new crossover method id
     */
    public void setCrossoverMethodValue(String newValue) {
        cOM.setSelectedItem(newValue);
    }
    
    /**
     * Returns the type of starting population
     * @return The type of starting population
     */
    public String getStartingPopType() {
        return (String) sPop.getSelectedItem();
    }
    
    /**
     * Sets the starting population type
     * @param newValue The new starting population type
     */
    public void setStartingPopType(String newValue) {
        sPop.setSelectedItem(newValue);
    }
    
    /**
     * Returns the selection method
     * @return The selection method to be used
     */
    public String getSelectionMethodValue() {
        return (String) sOM.getSelectedItem();
    }
    
    /**
     * Sets the selection method to be used
     * @param newValue The new selection method
     */
    public void setSeletionMethodValue(String newValue) {
        sOM.setSelectedItem(newValue);
    }
    
    /**
     * Gets the type of scoring system used
     * @return The type of scoring system used
     */
    public String getScoreTypeValue() {
        return (String) sTypeBox.getSelectedItem();
    }
    
    /**
     * Sets the type of scoring method used
     * @param newValue The type of scoring systems used
     */
    public void setScoreTypeMethodValue(String newValue) {
        sTypeBox.setSelectedItem(newValue);
    }
    
    /**
     * Captures an action event
     * @param e The action event to be captured
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if(e.getActionCommand().equals("Choose File")) {

            JFileChooser findFile = new JFileChooser();
            int returnVal = findFile.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                fileToLoad = findFile.getCurrentDirectory();
                System.out.println("Current Input State Directory = " + fileToLoad.toString());
                System.out.println("You loaded input states file: " + findFile.getSelectedFile().getName());
                fName = findFile.getSelectedFile().getName();
                progID.setText(fileToLoad.toString() + "/" + fName);
            }
            
        }
    }
    
    /**
     * Gets the directory name
     * @return The directory name
     */
    public File getFileDir() {
        return fileToLoad;
    }
    
    /**
     * Gets the file name
     * @return The file name
     */
    public String getFName() {
        return fName;
    }
    
    /**
     * Returns the name of the model to use in the GP run
     * @return The name of the model to use in the GP run
     */
    public String getModelName() {
        return (String) modelID.getSelectedItem();
    }
    
    /**
     * Sets the program directory
     * @param newValue The new program directory
     */
    public void setProgDir(String newValue) {
        progID.setText(newValue);
    }
    
    /**
     * Sets the stats output pane
     * @param newValue The stats output
     */
    public void setStatOutput(String newValue) {
        statOutput.setText(newValue);
    }
    
    /**
     * Sets the program output pane
     * @param newValue The program output
     */
    public void setProgOutput(String newValue) {
        progOutput.setText(newValue);
    }
    
    /**
     * Sets the state output pane
     * @param newValue The state output
     */
    public void setStateOutput(String newValue) {
        stateOutput.setText(newValue);
    }
    
    /**
     * Sets the progress
     * @param n The new level of progress
     * @param note A new progress note
     */
    public void setProgress(int n, String note) {
        progBar.setProgress(n);
        progBar.setNote(note);
    }
    
    /**
     * Sets the start button enabled/disabled
     * @param n true if enabled / false if not
     */
    public void setStartButtonEnabled(boolean n) {
        startButton.setEnabled(n);
    }
}
