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
package org.epochx.core;

import static org.epochx.stats.StatField.REP_REVERSIONS;

import org.epochx.life.*;
import org.epochx.op.ProgramSelector;
import org.epochx.representation.CandidateProgram;

/**
 * This component manages the reproduction operation of selecting a program to
 * survive into the next generation.
 * 
 * <p>
 * Use of the reproduction operation will generate the following events:
 * 
 * <table border="1">
 *     <tr>
 *         <th>Event</th>
 *         <th>Revert</th>
 *         <th>Modify</th>
 *         <th>Raised when?</th>
 *     </tr>
 *     <tr>
 *         <td>onReproductionStart</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>Before the reproduction operation is carried out.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onReproduction</td>
 *         <td><strong>yes</strong></td>
 *         <td><strong>yes</strong></td>
 *         <td>Immediately after a program is selected for reproduction, giving
 *         the listener the opportunity to request a revert which will cause a 
 *         re-selection of a program and this event to be raised again.
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>onReproductionEnd</td>
 *         <td>no</td>
 *         <td>no</td>
 *         <td>After the reproduction operation has been completed.
 *         </td>
 *     </tr>
 * </table>
 */
public class ReproductionManager {

	// The controlling model.
	private Model model;
	
	// The selector to use to choose the program to reproduce.
	private ProgramSelector programSelector;
	
	// The number of times the selected reproduction was rejected.
	private int reversions;
	
	/**
	 * Constructs an instance of ReproductionManager which will setup the 
	 * reproduction operation. The selection of the program to be reproduced is
	 * performed by the parent selector obtained from a call to the model's 
	 * <code>getParentSelector()</code> method.
	 * 
	 * @param model the model which defines the ProgramSelector to use to 
	 * 				select the program to be reproduced.
	 */
	public ReproductionManager(Model model) {
		this.model = model;
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		programSelector = model.getProgramSelector();
	}
	
	/**
	 * Selects a <code>CandidateProgram</code> from the population using the
	 * <code>ProgramSelector</code> returned by a call to 
	 * <code>getProgramSelector()</code> on the model given at construction. 
	 * 
	 * @return a CandidateProgram selected for reproduction.
	 */
	public CandidateProgram reproduce() {
		model.getLifeCycleManager().fireReproductionStartEvent();
		
		CandidateProgram parent = null;
		
		reversions = 0;
		
		do {
			// Choose a parent.
			parent = programSelector.getProgram();
			
			// Allow the life cycle listener to confirm or modify.
			parent = model.getLifeCycleManager().fireReproductionEvent(parent);
			
			if (parent == null) {
				reversions++;
			}
		} while(parent == null);
		
		// Store the stats from the reproduction.
		model.getStatsManager().addGenerationData(REP_REVERSIONS, reversions);
		
		model.getLifeCycleManager().fireReproductionEndEvent();
		
		return parent;
	}

}
