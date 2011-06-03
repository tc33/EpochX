package org.epochx.event;

import org.epochx.Config.ConfigKey;

public class ConfigEvent implements Event {

	private final ConfigKey<?> key;

	public ConfigEvent(ConfigKey<?> key) {
		this.key = key;
	}

	public ConfigKey<?> getKey() {
		return key;
	}
}
