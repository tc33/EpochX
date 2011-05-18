package org.epochx;

import java.util.*;

import org.epochx.Config.*;

public interface Breeder extends Component {

	public static final ConfigKey<List<Operator>> OPERATORS = new ConfigKey<List<Operator>>();
	
	public static final ConfigKey<IndividualSelector> SELECTOR = new ConfigKey<IndividualSelector>();
	
}
