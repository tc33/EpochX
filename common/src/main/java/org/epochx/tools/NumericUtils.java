/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.tools;

/**
 * This class provides static utility methods for working with different
 * number types.
 */
public class NumericUtils {

	private NumericUtils() {}

	/**
	 * Converts the given object to a <tt>Double</tt>
	 * 
	 * @param o an object of some <tt>Number</tt> type
	 * @return a <tt>Double</tt> that is equivalent to the given object or
	 *         <tt>null</tt> if the object is not an instance of
	 *         <tt>Number</tt>
	 */
	public static Double asDouble(Object o) {
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}

		return null;
	}

	/**
	 * Converts the given object to a <tt>Float</tt>
	 * 
	 * @param o an object of some <tt>Number</tt> type
	 * @return a <tt>Float</tt> that is equivalent to the given object or
	 *         <tt>null</tt> if the object is not an instance of
	 *         <tt>Number</tt>
	 */
	public static Float asFloat(Object o) {
		if (o instanceof Number) {
			return ((Number) o).floatValue();
		}

		return null;
	}

	/**
	 * Converts the given object to an <tt>Integer</tt>
	 * 
	 * @param o an object of some <tt>Number</tt> type
	 * @return an <tt>Integer</tt> that is equivalent to the given object or
	 *         <tt>null</tt> if the object is not an instance of
	 *         <tt>Number</tt>
	 */
	public static Integer asInteger(Object o) {
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}

		return null;
	}

	/**
	 * Converts the given object to a <tt>Long</tt>
	 * 
	 * @param o an object of some <tt>Number</tt> type
	 * @return a <tt>Long</tt> that is equivalent to the given object or
	 *         <tt>null</tt> if the object is not an instance of
	 *         <tt>Number</tt>
	 */
	public static Long asLong(Object o) {
		if (o instanceof Number) {
			return ((Number) o).longValue();
		}

		return null;
	}

	/**
	 * Converts the given object to a <tt>Short</tt>
	 * 
	 * @param o an object of some <tt>Number</tt> type
	 * @return a <tt>Short</tt> that is equivalent to the given object or
	 *         <tt>null</tt> if the object is not an instance of
	 *         <tt>Number</tt>
	 */
	public static Short asShort(Object o) {
		if (o instanceof Number) {
			return ((Number) o).shortValue();
		}

		return null;
	}

	/**
	 * Converts the given object to a <tt>Byte</tt>
	 * 
	 * @param o an object of some <tt>Number</tt> type
	 * @return a <tt>Byte</tt> that is equivalent to the given object or
	 *         <tt>null</tt> if the object is not an instance of
	 *         <tt>Number</tt>
	 */
	public static Byte asByte(Object o) {
		if (o instanceof Number) {
			return ((Number) o).byteValue();
		}

		return null;
	}
}
