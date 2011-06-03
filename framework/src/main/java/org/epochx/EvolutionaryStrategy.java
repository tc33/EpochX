package org.epochx;

import java.util.List;

import org.epochx.Config.ConfigKey;

public interface EvolutionaryStrategy extends Component {

	public static final ConfigKey<List<TerminationCriteria>> TERMINATION_CRITERIA = new ConfigKey<List<TerminationCriteria>>();

}
