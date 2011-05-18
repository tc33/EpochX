package org.epochx;

import java.util.*;

import org.epochx.Config.*;

public interface EvolutionaryStrategy extends Component {

	public static final ConfigKey<List<TerminationCriteria>> TERMINATION_CRITERIA = new ConfigKey<List<TerminationCriteria>>();
	
}
