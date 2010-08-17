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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.tools.util;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * 
 */
public final class ReferenceUtils {

	public static <T> List<T> stripRefs(final Set<WeakReference<T>> refs) {
		final List<T> objs = new ArrayList<T>();
		final Set<WeakReference<T>> removals = new HashSet<WeakReference<T>>();
		for (final WeakReference<T> ref: refs) {
			final T obj = ref.get();
			if (obj == null) {
				removals.add(ref);
			} else {
				objs.add(obj);
			}
		}
		refs.removeAll(refs);
		return objs;
	}

}
