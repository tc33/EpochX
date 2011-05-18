package org.epochx.event;

import org.epochx.Config.*;

public class ConfigEvent implements Event {

	private ConfigKey<?> key;
	
	public ConfigEvent(ConfigKey<?> key) {
		this.key = key;
	}
	
	public ConfigKey<?> getKey() {
		return key;
	}
}
