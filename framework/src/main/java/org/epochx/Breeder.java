package org.epochx;

import java.util.List;

import org.epochx.Config.ConfigKey;

public interface Breeder extends Component {

	public static final ConfigKey<List<Operator>> OPERATORS = new ConfigKey<List<Operator>>();

	public static final ConfigKey<IndividualSelector> SELECTOR = new ConfigKey<IndividualSelector>();

}
