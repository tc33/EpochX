/*
 * $Id: IndividualSelector.java 609 2011-04-07 10:21:50Z tc33 $
 */

package org.epochx;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 609 $ $Date:: 2011-04-07 11:21:50#$
 */
public interface IndividualSelector
{
    public void setup(Population population);
    
    public Individual select();
}