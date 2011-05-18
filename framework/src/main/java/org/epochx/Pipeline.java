/*
 * $Id: Pipeline.java 609 2011-04-07 10:21:50Z tc33 $
 */

package org.epochx;

import java.util.*;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 609 $ $Date:: 2011-04-07 11:21:50#$
 */
public class Pipeline implements Component
{
    private List<Component> pipeline;

    public Pipeline() {
    	pipeline = new ArrayList<Component>();
    }
    
    @Override
    public Population process(Population population)
    {
        for (Component component : pipeline)
        {
            population = component.process(population);
        }

        return population;
    }
    
    public void add(Component component) {
    	pipeline.add(component);
    }
    
    public void add(int index, Component component) {
    	pipeline.add(index, component);
    }
    
    public boolean remove(Component component) {
    	return pipeline.remove(component);
    }
    
    public Component remove(int index) {
    	return pipeline.remove(index);
    }
}