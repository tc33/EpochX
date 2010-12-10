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
package org.epochx.epox;

public class IntLiteral extends Node {

	private Integer value;

	public IntLiteral(final Integer value) {
		this.value = value;
	}

	@Override
	public Integer evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return Double.toString(value);
	}
	
	@Override
	public Class<?> getReturnType() {
		return Integer.class;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		final boolean inst = (obj instanceof IntLiteral);

		if (!inst) {
			return false;
		}

		final Integer objVal = ((IntLiteral) obj).value;
		final Integer thisVal = value;

		if ((objVal == null) ^ (thisVal == null)) {
			return false;
		}

		return (thisVal == objVal) || thisVal.equals(objVal);
	}

	@Override
	public IntLiteral clone() {
		final IntLiteral clone = (IntLiteral) super.clone();

		clone.value = value;

		return clone;
	}
}
