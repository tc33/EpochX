/*
 * $Id: Config.java 609 2011-04-07 10:21:50Z tc33 $
 */

package org.epochx;

import java.util.HashMap;

import org.epochx.event.*;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 609 $ $Date:: 2011-04-07 11:21:50#$
 */
public class Config
{
    private static final Config singleton = new Config();

    private HashMap<ConfigKey<?>, Object> mapping =
            new HashMap<ConfigKey<?>, Object>();

    private Config()
    {
    }

    public static Config getInstance()
    {
        return singleton;
    }

    public <T> void set(ConfigKey<T> key, T value)
    {
        mapping.put(key, value);
        EventManager.getInstance().fire(ConfigEvent.class, new ConfigEvent(key));
    }

    @SuppressWarnings("unchecked")
	public <T> T get(ConfigKey<T> key)
    {
        return (T) mapping.get(key);
    }

    public static class ConfigKey<T>
    {
    }
}