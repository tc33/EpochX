package org.epochx.life;

import java.util.*;

import org.epochx.representation.CandidateProgram;

public class LifeCycleManager {
	private List<RunListener> runListeners;
	private List<InitialisationListener> initialisationListeners;
	private List<ElitismListener> elitismListeners;
	private List<PoolSelectionListener> poolSelectionListeners;
	private List<CrossoverListener> crossoverListeners;
	private List<MutationListener> mutationListeners;
	private List<ReproductionListener> reproductionListeners;
	private List<GenerationListener> generationListeners;
	
	public LifeCycleManager() {
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
	
	public void onRunEnd() {
		for (RunListener listener: runListeners) {
			listener.onRunEnd();
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
	
	public List<CandidateProgram> onElitism(List<CandidateProgram> elites) {
		for (ElitismListener listener: elitismListeners) {
			elites = listener.onElitism(elites);
		}
		
		return elites;
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

	public CandidateProgram onReproduction(CandidateProgram child) {
		for (ReproductionListener listener: reproductionListeners) {
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
	
	public void onGenerationEnd() {
		for (GenerationListener listener: generationListeners) {
			listener.onGenerationEnd();
		}
	}
}
