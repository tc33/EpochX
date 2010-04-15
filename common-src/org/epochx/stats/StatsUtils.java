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
package org.epochx.stats;

import java.util.*;

public class StatsUtils {
	
	private StatsUtils() {}
	
	public static double ave(double[] values) {
		if ((values != null) && (values.length != 0)) {
			double sum = 0;
			for (int i=0; i<values.length; i++) {
				sum += values[i];
			}
			return sum/values.length;
		} else {
			throw new IllegalArgumentException("cannot calculate average of null or empty array of values");
		}		
	}
	
	public static double ave(int[] values) {
		if ((values != null) && (values.length != 0)) {
			int sum = 0;
			for (int i=0; i<values.length; i++) {
				sum += values[i];
			}
			return ((double) sum)/values.length;
		} else {
			throw new IllegalArgumentException("cannot calculate average of null or empty array of values");
		}
	}
	
	public static double stdev(double[] values) {
		return stdev(values, ave(values));
	}
	
	public static double stdev(double[] values, double ave) {
		// Sum the squared differences.
		double sqDiff = 0;
		for (int i=0; i<values.length; i++) {
			sqDiff += Math.pow(values[i] - ave, 2);
		}
		
		// Take the square root of the average.
		return Math.sqrt(sqDiff / values.length);
	}
	
	public static double stdev(int[] values) {
		return stdev(values, ave(values));
	}
	
	public static double stdev(int[] values, double ave) {
		// Sum the squared differences.
		double sqDiff = 0;
		for (int i=0; i<values.length; i++) {
			sqDiff += Math.pow(values[i] - ave, 2);
		}
		
		// Take the square root of the average.
		return Math.sqrt(sqDiff / values.length);
	}
	
	public static double max(double[] values) {
		//TODO No need for this - use apache commons NumberUtils.
		double max = Double.MIN_VALUE;
		for (int i=0; i<values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			}
		}
		return max;
	}
	
	public static int max(int[] values) {
		//TODO No need for this - use apache commons NumberUtils.
		int max = Integer.MIN_VALUE;
		for (int i=0; i<values.length; i++) {
			if (values[i] > max) {
				max = values[i];
			}
		}
		return max;
	}
	
	public static int maxIndex(double[] values) {
		double max = 0;
		int maxIndex = -1;
		for (int i=0; i<values.length; i++) {
			if (values[i] > max) {
				max = values[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static int maxIndex(int[] values) {
		int max = 0;
		int maxIndex = -1;
		for (int i=0; i<values.length; i++) {
			if (values[i] > max) {
				max = values[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static double min(double[] values) {
		//TODO No need for this - use apache commons NumberUtils.
		double min = Double.MAX_VALUE;
		for (int i=0; i<values.length; i++) {
			if (values[i] < min) {
				min = values[i];
			}
		}
		return min;
	}
	
	public static int min(int[] values) {
		//TODO No need for this - use apache commons NumberUtils.
		int min = Integer.MAX_VALUE;
		for (int i=0; i<values.length; i++) {
			if (values[i] < min) {
				min = values[i];
			}
		}
		return min;
	}
	
	public static int minIndex(double[] values) {
		double min = Double.MAX_VALUE;
		int minIndex = -1;
		for (int i=0; i<values.length; i++) {
			if (values[i] < min) {
				min = values[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public static int minIndex(int[] values) {
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for (int i=0; i<values.length; i++) {
			if (values[i] < min) {
				min = values[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public static double median(double[] values) {
		// Sort the array.
		Arrays.sort(values);

		// Pick out the middle value.
		int medianIndex = (int) Math.floor(values.length / 2);
		double median = values[medianIndex-1];
		
		// There might have been an even number - use average of 2 medians.
		if ((values.length % 2) == 0) {
			median += values[medianIndex];
			median = median/2;
		}
		
		return median;
	}
	
	public static double median(int[] values) {
		// Sort the array.
		Arrays.sort(values);
		
		// Pick out the middle value.
		int medianIndex = (int) Math.floor(values.length / 2);
		int median = values[medianIndex-1];
		
		// There might have been an even number - use average of 2 medians.
		if ((values.length % 2) == 0) {
			median += values[medianIndex];
			median = median/2;
		}
		
		return median;
	}
	
	public static double ci95(double[] values) {
		return ci95(values, stdev(values));
	}
	
	public static double ci95(double[] values, double stdev) {
		double ci = 1.96 * (stdev/Math.sqrt(values.length));
		
		return ci;
	}
	
	public static double ci95(int[] values) {
		return ci95(values, stdev(values));
	}
	
	public static double ci95(int[] values, double stdev) {
		double ci = 1.96 * (stdev/Math.sqrt(values.length));
		
		return ci;
	}
}
