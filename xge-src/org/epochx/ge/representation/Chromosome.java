/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 *//* 
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
package org.epochx.ge.representation;

import java.util.*;

import org.epochx.ge.codon.*;


/**
 * This class is not currently in use by XGE.
 */
public class Chromosome implements Cloneable {

	private CodonGenerator generator;	
	private List<Integer> codons;
	
	// These are mutually exclusive - one or the other or neither.
	private boolean wrapping;
	private boolean extending;
	
	public Chromosome(CodonGenerator generator, List<Integer> codons) {
		this.generator = generator;
		this.codons = codons;
		
		wrapping = false;
		extending = true;
	}
	
	public Chromosome(CodonGenerator generator) {
		this(generator, new ArrayList<Integer>());
	}
	
	public int getCodon(int index) {
		if (wrapping) {
			index = index % codons.size();
		} else if (index >= codons.size()) {
			return -1;
		}
		
		return codons.get(index);
	}
	
	public void setCodon(int index, int value) {
		if (wrapping) {
			index = index % codons.size();
		}
		
		modified();
	}
	
	public int getNoCodons() {
		return codons.size();
	}
	
	/**
	 * Randomly generate and add a codon to the candidate program.
	 */
	public void appendNewCodon() {
		appendCodon(generator.getCodon());
		
		modified();
	}
	
	/**
	 * Randomly generate and add multiple codons to the candidate program.
	 * @param no the number of codons to generate.
	 */
	public void appendNewCodons(int no) {
		for (int i=0; i<no; i++) {
			appendNewCodon();
		}
		
		modified();
	}
	
	public void insertNewCodon(int index) {
		codons.add(index, generator.getCodon());
		
		modified();
	}
	
	public int removeCodon(int index) {
		int c = codons.remove(index);
	
		modified();
		
		return c;
	}
	
	public List<Integer> removeCodons(int from, int to) {
		List<Integer> removed = new ArrayList<Integer>();
		for (int i=from; i<to; i++) {
			// As we remove them the list will shrink so we use from not i.
			removed.add(codons.remove(from));
		}
		
		modified();
		
		return removed;
	}
	
	/**
	 * Randomly generate and add a codon to the candidate program.
	 */
	public void appendCodon(int newCodon) {
		codons.add(newCodon);
		
		modified();
	}
	
	/**
	 * Randomly generate and add multiple codons to the candidate program.
	 */
	public void appendCodons(List<Integer> newCodons) {
		for (int i=0; i<newCodons.size(); i++) {
			appendCodon(newCodons.get(i));
		}
		
		modified();
	}

	/**
	 * Sets each codon from the startIndex. If the number of codons takes the 
	 * indexes beyond the length of the current codons then the new codons will 
	 * be added to the end of the list of codons. However if the first index is 
	 * greater than the length of the current set of codons then an 
	 * ArrayIndexOutOfBoundsException will be thrown.
	 * @throws ArrayIndexOutOfBoundsException If the first index is beyond the 
	 * end of the current list of codons.
	 */
	public void setCodons(List<Integer> newCodons, int startIndex) {
		// If more than one beyond the end of current codons.
		if (startIndex > codons.size()) {
			throw new ArrayIndexOutOfBoundsException(startIndex);
		}
		
		for (int i=0; i<newCodons.size(); i++) {
			int c = newCodons.get(i);
			if (i+startIndex < codons.size()) {
				codons.set(i+startIndex, c);
			} else {
				appendCodon(c);
			}
		}
		
		modified();
	}
	
	public List<Integer> getCodons(int from, int to) {
		return codons.subList(from, to);
	}
	
	/**
	 * Note that modifying the returned list won't modify the GECandidateProgram's
	 * list of codons.
	 */
	public List<Integer> getCodons() {
		return new ArrayList<Integer>(codons);
	}
	
	private void modified() {
		
	}

	public boolean isWrapping() {
		return wrapping;
	}

	public void setWrapping(boolean wrap) {
		this.wrapping = wrap;
		
		if (wrapping) {
			extending = false;
		}
	}

	public boolean isExtending() {
		return extending;
	}

	public void setExtending(boolean extending) {
		this.extending = extending;
		
		if (extending) {
			wrapping = false;
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Chromosome c = (Chromosome) super.clone();
		
		c.codons = new ArrayList<Integer>(this.codons);
		c.extending = this.extending;
		c.wrapping = this.wrapping;
				
		return c;
	}
	
	@Override
	public String toString() {
		return codons.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Chromosome) {
			Chromosome c = (Chromosome) obj;
			
			if ((c.extending == this.extending) && (c.wrapping == this.wrapping)) {
				return codons.equals(c.getCodons());
			}
		}
		
		return false;
	}
}
