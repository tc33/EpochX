package org.epochx.selection;

import org.epochx.*;

public abstract class AbstractSelector implements IndividualSelector {

	protected Population population;
	
	@Override
	public void setup(Population population) {
		this.population = population;
	}

}
