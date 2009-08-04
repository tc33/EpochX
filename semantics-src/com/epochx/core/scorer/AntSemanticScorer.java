/*  
 *  Copyright 2007-2009 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.epochxge.core.scorer;

import java.util.ArrayList;

import com.epochxge.representation.CandidateProgram;
import com.epochxge.semantics.AntRepresentation;
import com.epochxge.semantics.SemanticModule;

/**
 * The Ant Semantic Scorer allows users to compare to representations of 
 * Ant programs and return a value denoting relative difference. Zero would 
 * be the best score possible, i.e. denoting equivalence.
 */
public class AntSemanticScorer extends SemanticScorer {
	
	/**
	 * Constructor for ant semantic scorer
	 * @param semanticModule The ant semantic module
	 */
	public AntSemanticScorer(SemanticModule semanticModule) {
		super(semanticModule);
	}

	/* (non-Javadoc)
	 * @see com.epochx.core.scorer.SemanticScorer#doScore(com.epochx.representation.CandidateProgram, com.epochx.representation.CandidateProgram)
	 */
	public double doScore(CandidateProgram program1, CandidateProgram program2) {
		// generate 2 behaviours
		AntRepresentation rep1 = (AntRepresentation) getSemanticModule().codeToBehaviour(program1);
		AntRepresentation rep2 = (AntRepresentation) getSemanticModule().codeToBehaviour(program2);
		
		// copy them into strings
		String string1 = stringBuilder(rep1.getAntRepresentation());
		String string2 = stringBuilder(rep2.getAntRepresentation());
		
		// perform levenshtein distance calculation
		double score = levenshteinDistance(string1, string2);
		
		// return score
		return score;
	}
	
	private String stringBuilder(ArrayList<String> inputList) {
		String word = "";
		for(String part: inputList) {
			word = word + part;
		}
		return word;
	}

	private int minimum(int a, int b, int c) {
		int mi;
		mi = a;
		if (b < mi) {
			mi = b;
		}
		if (c < mi) {
			mi = c;
		}
		return mi;
	}

	private int levenshteinDistance(String s, String t) {
		int d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1
		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];

		// Step 2
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// Step 3
		for (i = 1; i <= n; i++) {

			s_i = s.charAt(i - 1);

			// Step 4
			for (j = 1; j <= m; j++) {

				t_j = t.charAt(j - 1);

				// Step 5
				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}

				// Step 6
				d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
						d[i - 1][j - 1] + cost);
			}
		}

		// Step 7
		return d[n][m];
	}
}
