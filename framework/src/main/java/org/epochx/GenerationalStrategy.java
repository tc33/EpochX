/*
 * $Id: GenerationalStrategy.java 609 2011-04-07 10:21:50Z tc33 $
 */

package org.epochx;

import java.util.*;

import org.epochx.event.*;
import org.epochx.event.GenerationEvent.*;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 609 $ $Date:: 2011-04-07 11:21:50#$
 */
public class GenerationalStrategy
    extends Pipeline implements EvolutionaryStrategy, Listener<ConfigEvent> {
	
    private List<TerminationCriteria> criteria;
    
    public GenerationalStrategy(Component ... components) {
    	for (Component component: components) {
    		add(component);
    	}
    	
    	setup();
    	EventManager.getInstance().add(ConfigEvent.class, this);
    }

    @Override
    public Population process(Population population) {
    	
    	int generation = 1;
        while (!terminate()) {
        	EventManager.getInstance().fire(StartGeneration.class, new StartGeneration(generation));
        	
            population = super.process(population);
            
            EventManager.getInstance().fire(EndGeneration.class, new EndGeneration(generation));
        	generation++;
        }
        
        return population;
    }
    
    protected boolean terminate() {
    	for (TerminationCriteria tc: criteria) {
    		if (tc.terminate()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    protected void setup() {
    	criteria = Config.getInstance().get(EvolutionaryStrategy.TERMINATION_CRITERIA);
	}

	public void onEvent(ConfigEvent event) {
		if (event.getKey() == EvolutionaryStrategy.TERMINATION_CRITERIA) {
			setup();
		}
	}
}