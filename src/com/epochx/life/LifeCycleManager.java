/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.life;

import java.util.*;

import com.epochx.representation.*;

/**
 * The life cycle manager's job is to keep track of all those objects that want 
 * to be informed about life cycle events and to let them know when events 
 * happen.
 * 
 * TODO This is from LifeCycleListener...
 * Implementations of LifeCycleListener can be used to listen for events 
 * throughout the life of a GP run. The provided methods will be called at the 
 * relevant times during execution, and have the opportunity to confirm the 
 * operation by returning the appropriate argument value, modify the operation 
 * by altering the argument before returning and in some cases it is possible 
 * to trigger a 'reversion' by returning null. A reversion is where the 
 * operation is discarded and rerun. In the case of reversion, the life cycle 
 * method will be recalled in the same way during the rerun, allowing the life 
 * cycle listener the ability to revert indefinitely. It is worth noting that 
 * this leaves room for the potential of an infinite loop if any of these 
 * methods were defined to return null in all possible circumstances. The 
 * number of reversions for crossover and mutation is obtainable from the 
 * crossover stats and mutation stats respectively.
 */
public class LifeCycleManager<TYPE> {
	/*
	 * 
	 * 
	 * TODO Introduce an ordering of the listeners.
	 */
	
	private List<RunListener> runListeners;
	private List<InitialisationListener<TYPE>> initialisationListeners;
	private List<ElitismListener<TYPE>> elitismListeners;
	private List<PoolSelectionListener<TYPE>> poolSelectionListeners;
	private List<CrossoverListener<TYPE>> crossoverListeners;
	private List<MutationListener<TYPE>> mutationListeners;
	private List<ReproductionListener<TYPE>> reproductionListeners;
	private List<GenerationListener> generationListeners;
	private List<TerminationListener> terminationListeners;
	
	public LifeCycleManager() {
		// Initialise listener lists.
		runListeners = new ArrayList<RunListener>();
		initialisationListeners = new ArrayList<InitialisationListener<TYPE>>();
		elitismListeners = new ArrayList<ElitismListener<TYPE>>();
		poolSelectionListeners = new ArrayList<PoolSelectionListener<TYPE>>();
		crossoverListeners = new ArrayList<CrossoverListener<TYPE>>();
		mutationListeners = new ArrayList<MutationListener<TYPE>>();
		reproductionListeners = new ArrayList<ReproductionListener<TYPE>>();
		generationListeners = new ArrayList<GenerationListener>();
		terminationListeners = new ArrayList<TerminationListener>();
	}
	
	public void addRunListener(RunListener listener) {
		runListeners.add(listener);
	}
	
	public void removeRunListener(RunListener listener) {
		runListeners.remove(listener);
	}
	
	public void addInitialisationListener(InitialisationListener<TYPE> listener) {
		initialisationListeners.add(listener);
	}
	
	public void removeInitialisationListener(InitialisationListener<TYPE> listener) {
		initialisationListeners.remove(listener);
	}
	
	public void addElitismListener(ElitismListener<TYPE> listener) {
		elitismListeners.add(listener);
	}
	
	public void removeElitismListener(ElitismListener<TYPE> listener) {
		elitismListeners.remove(listener);
	}
	
	public void addPoolSelectionListener(PoolSelectionListener<TYPE> listener) {
		poolSelectionListeners.add(listener);
	}
	
	public void removePoolSelectionListener(PoolSelectionListener<TYPE> listener) {
		poolSelectionListeners.remove(listener);
	}
	
	public void addCrossoverListener(CrossoverListener<TYPE> listener) {
		crossoverListeners.add(listener);
	}
	
	public void removeCrossoverListener(CrossoverListener<TYPE> listener) {
		crossoverListeners.remove(listener);
	}
	
	public void addMutationListener(MutationListener<TYPE> listener) {
		mutationListeners.add(listener);
	}
	
	public void removeMutationListener(MutationListener<TYPE> listener) {
		mutationListeners.remove(listener);
	}
	
	public void addReproductionListener(ReproductionListener<TYPE> listener) {
		reproductionListeners.add(listener);
	}
	
	public void removeReproductionListener(ReproductionListener<TYPE> listener) {
		reproductionListeners.remove(listener);
	}
	
	public void addGenerationListener(GenerationListener listener) {
		generationListeners.add(listener);
	}
	
	public void removeGenerationListener(GenerationListener listener) {
		generationListeners.remove(listener);
	}
	
	public void addTerminationListener(TerminationListener listener) {
		terminationListeners.add(listener);
	}
	
	public void removeTerminationListener(TerminationListener listener) {
		terminationListeners.remove(listener);
	}

	public void onRunStart() {
		for (RunListener listener: runListeners) {
			listener.onRunStart();
		}
	}
	
	public List<CandidateProgram<TYPE>> onInitialisation(List<CandidateProgram<TYPE>> pop) {
		for (InitialisationListener<TYPE> listener: initialisationListeners) {
			pop = listener.onInitialisation(pop);
			
			if (pop == null) {
				break;
			}
		}
		
		return pop;
	}
	
	public List<CandidateProgram<TYPE>> onElitism(List<CandidateProgram<TYPE>> elites) {
		for (ElitismListener<TYPE> listener: elitismListeners) {
			elites = listener.onElitism(elites);
		}
		
		return elites;
	}

	public List<CandidateProgram<TYPE>> onPoolSelection(List<CandidateProgram<TYPE>> pool) {
		for (PoolSelectionListener<TYPE> listener: poolSelectionListeners) {
			pool = listener.onPoolSelection(pool);
			
			if (pool == null) {
				break;
			}
		}
		
		return pool;
	}
	
	public CandidateProgram<TYPE>[] onCrossover(CandidateProgram<TYPE>[] parents,
			CandidateProgram<TYPE>[] children) {
		for (CrossoverListener<TYPE> listener: crossoverListeners) {
			children = listener.onCrossover(parents, children);
			
			if (children == null) {
				break;
			}
		}
		
		return children;
	}

	public CandidateProgram<TYPE> onMutation(CandidateProgram<TYPE> parent,
			CandidateProgram<TYPE> child) {
		for (MutationListener<TYPE> listener: mutationListeners) {
			child = listener.onMutation(parent, child);
			
			if (child == null) {
				break;
			}
		}
		
		return child;
	}

	public CandidateProgram<TYPE> onReproduction(CandidateProgram<TYPE> child) {
		for (ReproductionListener<TYPE> listener: reproductionListeners) {
			child = listener.onReproduction(child);	
			
			if (child == null) {
				break;
			}
		}
		
		return child;
	}
	
	public void onGenerationStart() {
		for (GenerationListener listener: generationListeners) {
			listener.onGenerationStart();
		}
	}
	
	/*public List<CandidateProgram> onGeneration(List<CandidateProgram> pop) {
		for (GenerationListener listener: generationListeners) {
			pop = listener.onGenerationStart(pop);
			
			if (pop == null) {
				break;
			}
		}
		
		return pop;
	}*/

	public void onFitnessTermination() {
		for (TerminationListener listener: terminationListeners) {
			listener.onFitnessTermination();
		}
	}
	
	public void onGenerationTermination() {
		for (TerminationListener listener: terminationListeners) {
			listener.onGenerationTermination();
		}
	}
	
}
