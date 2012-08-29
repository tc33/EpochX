/* 
 * Copyright 2007-2012
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.monitor.graph;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.epochx.event.Event;
import org.epochx.event.Listener;


/**
 * A <code>GraphModelWriter</code>.
 */
public class GraphModelWriter implements Listener<Event>, Runnable {
	
	/**
	 * The model to serialize.
	 */
	private GraphModel model;
	
	/**
	 * The file name.
	 */
	private String file;
	
	/**
	 * 
	 * Constructs a <code>GraphModelWriter</code>.
	 * 
	 * @param model the model to serialize.
	 * @param file the file name.
	 */
	public GraphModelWriter(GraphModel model, String file) {
		
		this.model = model;
		this.file = file;
		
	}

	/**
	 * Returns the model.
	 * @return the model.
	 */
	public GraphModel getModel() {
		return model;
	}
	
	/**
	 * Sets the model.
	 * @param model the model to set.
	 */
	public void setModel(GraphModel model) {
		this.model = model;
	}
	
	/**
	 * Returns the file name.
	 * @return the file name.
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * Sets the file name.
	 * @param file the file name to set.
	 */
	public void setFile(String file) {
		this.file = file;
	}

	public void onEvent(Event event) {
		model.onEvent(event);
		write();
	}
	
	public void write(){
		Thread t = new Thread(this, "MONITOR-GraphWriter");
		t.start();
	}
	
	public void run() {
		try {
			ObjectOutputStream stream = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(
									new File(file))));
			
			stream.writeObject(model);
			stream.close();
			
			System.out.println("Model written in file : "+file);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
