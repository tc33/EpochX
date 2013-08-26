/*
 * Copyright 2007-2013
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

import org.apache.commons.lang.*;

/**
 * This class provides static utility methods for working with Epox data-types.
 */
public final class DataTypeUtils {

	private DataTypeUtils() {
	}

	/**
	 * Returns the super-type class of the given classes, or <tt>null</tt> if
	 * none of them are a super class of all others
	 * 
	 * @param classes the classes to check for a super-type
	 * @return the super-type if there is one, or <tt>null</tt> otherwise
	 */
	public static Class<?> getSuper(Class<?> ... classes) {
		return getSuper(false, classes);
	}

	/**
	 * Returns the super-type class of the given classes, or <tt>null</tt> if
	 * none of them are a super class of all others
	 * 
	 * @param autobox whether autoboxing should be allowed in the consideration
	 *        of a super-type
	 * @param classes the classes to check for a super-type
	 * @return the super-type if there is one, or <tt>null</tt> otherwise
	 */
	public static Class<?> getSuper(boolean autobox, Class<?> ... classes) {
		outer:for (Class<?> cls1: classes) {
			for (Class<?> cls2: classes) {
				if (!ClassUtils.isAssignable(cls2, cls1, autobox)) {
					continue outer;
				}
			}
			return cls1;
		}

		return null;
	}

	/**
	 * Returns the sub-type class of the given classes, or <tt>null</tt> if
	 * none of them are a sub class of all others
	 * 
	 * @param classes the classes to check for a sub-type
	 * @return the sub-type if there is one, or <tt>null</tt> otherwise
	 */
	public static Class<?> getSub(Class<?> ... classes) {
		return getSuper(false, classes);
	}

	/**
	 * Returns the sub-type class of the given classes, or <tt>null</tt> if
	 * none of them are a sub class of all others
	 * 
	 * @param autobox whether autoboxing should be allowed in the consideration
	 *        of a sub-type
	 * @param classes the classes to check for a sub-type
	 * @return the sub-type if there is one, or <tt>null</tt> otherwise
	 */
	public static Class<?> getSub(boolean autobox, Class<?> ... classes) {
		outer:for (Class<?> cls1: classes) {
			for (Class<?> cls2: classes) {
				if (!ClassUtils.isAssignable(cls1, cls2, autobox)) {
					continue outer;
				}
			}
			return cls1;
		}

		return null;
	}

	/**
	 * Returns <tt>true</tt> if the given collection contains an element which
	 * represents a class which is the same as or is a sub-type of the given
	 * class
	 * 
	 * @param collection the collection to search for a class that is assignable
	 *        to <tt>cls</tt>
	 * @param cls the class that is being searched against the given collection
	 * @return <tt>true</tt> if the given collection contains a class which is
	 *         the same as or a sub-type of the given class parameter. It
	 *         returns <tt>false</tt> otherwise.
	 */
	public static boolean containsSub(Class<?>[] collection, Class<?> cls) {
		for (Class<?> c: collection) {
			if (ClassUtils.isAssignable(c, cls)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns <tt>true</tt> if the given collection contains an element which
	 * represents a class which is the same as or is a super-type of the given
	 * class
	 * 
	 * @param collection the classes to search for one that <tt>cls</tt> is
	 *        assignable to
	 * @param cls the class that is being searched against <tt>collection</tt>
	 * @return <tt>true</tt> if the given collection contains a class which is
	 *         the same as or a super-type of the given class parameter. It
	 *         returns <tt>false</tt> otherwise.
	 */
	public static boolean containsSuper(Class<?>[] collection, Class<?> cls) {
		for (Class<?> c: collection) {
			if (ClassUtils.isAssignable(cls, c)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns <tt>true</tt> if all elements of <tt>collection</tt> are
	 * sub-types of <tt>cls</tt>, or the same type
	 * 
	 * @param collection the classes that must all be sub-types of <tt>cls</tt>
	 * @param cls the type to compare against <tt>collection</tt>
	 * @return <tt>true</tt> if all classes in <tt>collection</tt> are sub-types
	 *         of, or the same type as, <tt>cls</tt> and returns <tt>false</tt>
	 *         otherwise
	 */
	public static boolean allSub(Class<?>[] collection, Class<?> cls) {
		for (Class<?> c: collection) {
			if (!ClassUtils.isAssignable(c, cls)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns <tt>true</tt> if all elements of <tt>collection</tt> are
	 * super-types of <tt>cls</tt>, or the same type
	 * 
	 * @param collection the classes that must all be super-types of
	 *        <tt>cls</tt>
	 * @param cls the type to compare against <tt>collection</tt>
	 * @return <tt>true</tt> if all classes in <tt>collection</tt> are
	 *         super-types of, or the same type as, <tt>cls</tt> and returns
	 *         <tt>false</tt> otherwise
	 */
	public static boolean allSuper(Class<?>[] collection, Class<?> cls) {
		for (Class<?> c: collection) {
			if (!ClassUtils.isAssignable(cls, c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns <tt>true</tt> if all classes in the <tt>collection</tt> array
	 * are equal to <tt>cls</tt>
	 * 
	 * @param collection the classes to compare against <tt>cls</tt> for
	 *        equality
	 * @param cls the type to compare against <tt>collection</tt>
	 * @return <tt>true</tt> if all classes in <tt>collection</tt> are the same
	 *         type as <tt>cls</tt> and <tt>false</tt> otherwise
	 */
	public static boolean allEqual(Class<?>[] collection, Class<?> cls) {
		for (Class<?> c: collection) {
			if (c != cls) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the widest compatible numeric type for two primitive numeric
	 * class types. Any of <tt>Byte</tt>, <tt>Short</tt>, <tt>Integer</tt> will
	 * resolve to <tt>Integer</tt>.
	 * 
	 * @param classes an array of numeric class types, for example,
	 *        <tt>int, long, float, or double</tt>.
	 * @return the compatible numeric type for binary operations involving
	 *         both types, or <tt>null</tt> if there is no compatible numeric
	 *         type
	 */
	public static Class<?> widestNumberType(Class<?> ... classes) {
		if (!isAllNumericType(classes)) {
			return null;
		}

		if (ArrayUtils.contains(classes, Double.class)) {
			return Double.class;
		} else if (ArrayUtils.contains(classes, Float.class)) {
			return Float.class;
		} else if (ArrayUtils.contains(classes, Long.class)) {
			return Long.class;
		} else {
			return Integer.class;
		}
	}

	/**
	 * Tests whether the given class type is for an integer type
	 * (one of <tt>Byte, Short, Integer, Long</tt>)
	 * 
	 * @param type the type to check
	 * @return <tt>true</tt> if it is a primitive integer type, <tt>false</tt>
	 *         otherwise
	 */
	public static boolean isIntegerType(Class<?> type) {
		return ((type == Byte.class) || (type == Short.class) || (type == Integer.class) || (type == Long.class));
	}

	/**
	 * Tests whether the given class type is a numeric type
	 * (one of <tt>Byte, Short, Integer, Long, Float, Double</tt>)
	 * 
	 * @param type the type to check
	 * @return <tt>true</tt> if it is a numeric type, <tt>false</tt> otherwise
	 */
	public static boolean isNumericType(Class<?> type) {
		return ((type == Byte.class) || (type == Short.class) || (type == Integer.class) || (type == Long.class)
				|| (type == Double.class) || (type == Float.class));
	}

	/**
	 * Tests whether all given classes are for numeric types (any of <tt>Byte, 
	 * Short, Integer, Long, Float, Double</tt>)
	 * 
	 * @param classes the types to check for numeric types
	 * @return <tt>true</tt> if all given classes are for numeric types,
	 *         <tt>false</tt> otherwise
	 */
	public static boolean isAllNumericType(Class<?> ... classes) {
		for (Class<?> c: classes) {
			if (!DataTypeUtils.isNumericType(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the primitive class of any numeric object class (any of <tt>Byte,
	 * Short, Integer, Long, Float, Double</tt>). If a primitive type is 
	 * provided then the same type is returned.
	 * 
	 * @param type a numeric type, which is either a primitive or an object
	 * @return the equivalent primitive class type
	 */
	public static Class<?> getPrimitiveType(Class<?> type) {
		if (type.isPrimitive()) {
			return type;
		} else if (Integer.class.equals(type)) {
			return int.class;
		} else if (Long.class.equals(type)) {
			return long.class;
		} else if (Float.class.equals(type)) {
			return float.class;
		} else if (Double.class.equals(type)) {
			return double.class;
		} else if (Byte.class.equals(type)) {
			return byte.class;
		} else if (Short.class.equals(type)) {
			return short.class;
		} else {
			throw new IllegalArgumentException("Input class must be a numeric type");
		}
	}

	/**
	 * Returns the object wrapper class type for a primitive class type
	 * 
	 * @param type a numeric type, which is either a primitive or an object
	 * @return the equivalent object class type
	 */
	public static Class<?> getObjectType(Class<?> type) {
		if (!type.isPrimitive()) {
			return type;
		} else if (int.class == type) {
			return Integer.class;
		} else if (long.class == type) {
			return Long.class;
		} else if (float.class == type) {
			return Float.class;
		} else if (double.class == type) {
			return Double.class;
		} else if (boolean.class == type) {
			return Boolean.class;
		} else if (short.class == type) {
			return Short.class;
		} else if (char.class == type) {
			return Character.class;
		} else if (byte.class == type) {
			return Byte.class;
		} else if (short.class == type) {
			return Short.class;
		} else {
			throw new IllegalArgumentException();
		}
	}

}
