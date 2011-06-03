/*
 * $Id: Operator.java 614 2011-04-12 11:58:53Z tc33 $
 */

package org.epochx;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 614 $ $Date:: 2011-04-12 12:58:53#$
 */
public interface Operator {

	public int inputSize();

	public Individual[] apply(Individual ... individuals);

	public double probability();

}
