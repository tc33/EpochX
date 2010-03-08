/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.life;

import java.util.*;

import org.epochx.representation.CandidateProgram;

public class LifeCycleManager {
	
	private static LifeCycleManager lifeCycle;
	
	private List<RunListener> runListeners;
	private List<InitialisationListener> initialisationListeners;
	private List<ElitismListener> elitismListeners;
	private List<PoolSelectionListener> poolSelectionListeners;
	private List<CrossoverListener> crossoverListeners;
	private List<MutationListener> mutationListeners;
	private List<ReproductionListener> reproductionListeners;
	private List<GenerationListener> generationListeners;
	
	private LifeCycleManager() {
		// Initialise listener lists.
		runListeners = new ArrayList<RunListener>();
		initialisationListeners = new ArrayList<InitialisationListener>();
		elitismListeners = new ArrayList<ElitismListener>();
		poolSelectionListeners = new ArrayList<PoolSelectionListener>();
		crossoverListeners = new ArrayList<CrossoverListener>();
		mutationListeners = new ArrayList<MutationListener>();
		reproductionListeners = new ArrayList<ReproductionListener>();
		generationListeners = new ArrayList<GenerationListener>();
	}
	
	public void addRunListener(RunListener listener) {
		runListeners.add(listener);
	}
	
	public void removeRunListener(RunListener listener) {
		runListeners.remove(listener);
	}
	
	public void addInitialisationListener(InitialisationListener listener) {
		initialisationListeners.add(listener);
	}
	
	public void removeInitialisationListener(InitialisationListener listener) {
		initialisationListeners.remove(listener);
	}
	
	public void addElitismListener(ElitismListener listener) {
		elitismListeners.add(listener);
	}
	
	public void removeElitismListener(ElitismListener listener) {
		elitismListeners.remove(listener);
	}
	
	public void addPoolSelectionListener(PoolSelectionListener listener) {
		poolSelectionListeners.add(listener);
	}
	
	public void removePoolSelectionListener(PoolSelectionListener listener) {
		poolSelectionListeners.remove(listener);
	}
	
	public void addCrossoverListener(CrossoverListener listener) {
		crossoverListeners.add(listener);
	}
	
	public void removeCrossoverListener(CrossoverListener listener) {
		crossoverListeners.remove(listener);
	}
	
	public void addMutationListener(MutationListener listener) {
		mutationListeners.add(listener);
	}
	
	public void removeMutationListener(MutationListener listener) {
		mutationListeners.remove(listener);
	}
	
	public void addReproductionListener(ReproductionListener listener) {
		reproductionListeners.add(listener);
	}
	
	public void removeReproductionListener(ReproductionListener listener) {
		reproductionListeners.remove(listener);
	}
	
	public void addGenerationListener(GenerationListener listener) {
		generationListeners.add(listener);
	}
	
	public void removeGenerationListener(GenerationListener listener) {
		generationListeners.remove(listener);
	}

	public void onRunStart() {
		for (RunListener listener: runListeners) {
			listener.onRunStart();
		}
	}
	
	public void onSuccess() {
		for (RunListener listener: runListeners) {
			listener.onSuccess();
		}
	}
	
	public void onRunEnd() {
		for (RunListener listener: runListeners) {
			listener.onRunEnd();
		}
	}
	
	public void onInitialisationStart() {
		for (InitialisationListener listener: initialisationListeners) {
			listener.onInitialisationStart();
		}
	}
	
	public List<CandidateProgram> onInitialisation(List<CandidateProgram> pop) {
		for (InitialisationListener listener: initialisationListeners) {
			pop = listener.onInitialisation(pop);
			
			if (pop == null) {
				break;
			}
		}
		
		return pop;
	}
	
	public void onInitialisationEnd() {
		for (InitialisationListener listener: initialisationListeners) {
			listener.onInitialisationEnd();
		}
	}
	
	public void onElitismStart() {
		for (ElitismListener listener: elitismListeners) {
			listener.onElitismStart();
		}
	}
	
	public List<CandidateProgram> onElitism(List<CandidateProgram> elites) {
		for (ElitismListener listener: elitismListeners) {
			elites = listener.onElitism(elites);
		}
		
		return elites;
	}
	
	public void onElitismEnd() {
		for (ElitismListener listener: elitismListeners) {
			listener.onElitismEnd();
		}
	}
	
	public void onPoolSelectionStart() {
		for (PoolSelectionListener listener: poolSelectionListeners) {
			listener.onPoolSelectionStart();
		}
	}

	public List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool) {
		for (PoolSelectionListener listener: poolSelectionListeners) {
			pool = listener.onPoolSelection(pool);
			
			if (pool == null) {
				break;
			}
		}
		
		return pool;
	}
	
	public void onPoolSelectionEnd() {
		for (PoolSelectionListener listener: poolSelectionListeners) {
			listener.onPoolSelectionEnd();
		}
	}
	
	public void onCrossoverStart() {
		for (CrossoverListener listener: crossoverListeners) {
			listener.onCrossoverStart();
		}
	}
	
	public CandidateProgram[] onCrossover(CandidateProgram[] parents,
			CandidateProgram[] children) {
		for (CrossoverListener listener: crossoverListeners) {
			children = listener.onCrossover(parents, children);
			
			if (children == null) {
				break;
			}
		}
		
		return children;
	}
	
	public void onCrossoverEnd() {
		for (CrossoverListener listener: crossoverListeners) {
			listener.onCrossoverEnd();
		}
	}

	public void onMutationStart() {
		for (MutationListener listener: mutationListeners) {
			listener.onMutationStart();
		}
	}
	
	public CandidateProgram onMutation(CandidateProgram parent,
			CandidateProgram child) {
		for (MutationListener listener: mutationListeners) {
			child = listener.onMutation(parent, child);
			
			if (child == null) {
				break;
			}
		}
		
		return child;
	}
	
	public void onMutationEnd() {
		for (MutationListener listener: mutationListeners) {
			listener.onMutationEnd();
		}
	}
	
	public void onReproductionStart() {
		for (ReproductionListener listener: reproductionListeners) {
			listener.onReproductionStart();
		}
	}

	public CandidateProgram onReproduction(CandidateProgram child) {
		for (ReproductionListener listener: reproductionListeners) {
			child = listener.onReproduction(child);	
			
			if (child == null) {
				break;
			}
		}
		
		return child;
	}
	
	public void onReproductionEnd() {
		for (ReproductionListener listener: reproductionListeners) {
			listener.onReproductionEnd();
		}
	}
	
	public void onGenerationStart() {
		for (GenerationListener listener: generationListeners) {
			listener.onGenerationStart();
		}
	}
	
	public List<CandidateProgram> onGeneration(List<CandidateProgram> pop) {
		for (GenerationListener listener: generationListeners) {
			pop = listener.onGeneration(pop);
			
			if (pop == null) {
				break;
			}
		}
		
		return pop;
	}
	
	public void onGenerationEnd() {
		for (GenerationListener listener: generationListeners) {
			listener.onGenerationEnd();
		}
	}
	
	/**
	 * Returns the life cycle manager which handles all life cycle events 
	 * throughout execution of the <code>run</code> method. The life cycle 
	 * manager receives details of all events and then informs the necessary 
	 * listeners. Most use is through the <code>addXXXListener</code> methods,
	 * and typically with an anonymous class.
	 * 
	 * <h4>Example use of <code>LifeCycleManager's</code> listener model:</h4>
	 * 
	 * <pre>
     * Controller.getLifeCycleManager().addRunListener(new RunAdapter() {
	 *     public void onRunStart() {
	 *         //... do something ...
	 *     }
	 * });
	 * </pre>
	 * 
	 * @return the life cycle manager instance that manages life cycle events 
	 * throughout execution with this <code>Controller</code>.
	 */
	public static LifeCycleManager getLifeCycleManager() {
		// Ensure our singleton instance has been constructed.
		if (lifeCycle == null) {
			lifeCycle = new LifeCycleManager();
		}

		return lifeCycle;
	}
}
