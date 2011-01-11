/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.tools.util;


/**
 * 
 */
public final class MathUtils {

	/**
	 * Arc-cosecant
	 * 
	 * @param d
	 * @return
	 */
	public static double acsc(double d) {
		return Math.asin(1.0/d);
	}
	
	/**
	 * Arc-tangent
	 * 
	 * @param d
	 * @return
	 */
	public static double acot(double d) {
		return Math.atan(1.0/d);
	}
	
	/**
	 * Arc-secant
	 * 
	 * @param d
	 * @return
	 */
	public static double asec(double d) {
		return Math.acos(1.0/d);
	}
	
	/**
	 * Cosecant
	 * 
	 * @param d
	 * @return
	 */
	public static double cosec(double d) {
		return 1 / Math.sin(d);
	}
	
	/**
	 * Secant
	 * 
	 * @param d
	 * @return
	 */
	public static double sec(double d) {
		return 1 / Math.cos(d);
	}
	
	/**
	 * Cotangent
	 * 
	 * @param d
	 * @return
	 */
	public static double cotan(double d) {
		return 1 / Math.tan(d);
	}
}
