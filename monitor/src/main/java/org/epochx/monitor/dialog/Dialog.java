/*
 * Copyright 2007-2013
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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.monitor.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.epochx.monitor.Monitor;

/**
 * A <code>Dialog</code> is abstract class which defines a monitor dialog.
 * <p>
 * This class extend <code>JDialog</code>, implements <code>ActionLister</code>
 * to be added as a listener of a <code>JComponent</code> and
 * <code>Runnable</code> interface.
 * </p>
 * There are three way of implementing the
 * <code>actionPerformed(ActionEvent e)</code> method :<br>
 * <br>
 * <ul>
 * <li>Call the <code>run()</code> method in the current thread (by default) :
 * 
 * <pre>
 * this.run();
 * </pre>
 * 
 * <i>Note that almost always, the current thread will be the EDT.</i><br>
 * <br>
 * <li>Invoked the <code>run()</code> method by using a separate thread :
 * 
 * <pre>
 * new Thread(this).start();
 * </pre>
 * 
 * <li>Invoked the <code>run()</code> method in the EDT by invoking
 * {@link SwingUtilities#invokeLater(Runnable)} method :
 * 
 * <pre>
 * SwingUtilities.invokeLater(this);
 * </pre>
 * 
 * </ul>
 * 
 * <b>In any case, the implementation of a <code>Dialog</code> inherited class have
 * to take care of concurrency issues.</b>
 */
public abstract class Dialog extends JDialog implements ActionListener, Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 1941994409050918497L;
	/**
	 * The owner <code>Monitor</code>.
	 */
	protected final Monitor monitor;

	/**
	 * Constructs a <code>Dialog</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 */
	Dialog(Monitor monitor) {
		this(monitor, "Dialog");
	}

	/**
	 * Constructs a <code>Dialog</code>.
	 * 
	 * @param monitor the owner <code>Monitor</code>.
	 * @param name the name of the <code>Dialog</code> frame.
	 */
	Dialog(Monitor monitor, String name) {
		super(monitor, name);
		this.monitor = monitor;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * The <b>ActionListener</b> inherited method.
	 * <p>
	 * Be carrefull : by default the run method is almost launch in the Event
	 * Dispatch Thread, that could result freeze if the <code>run()</code>
	 * method is long to compute.
	 * </p>
	 */
	public void actionPerformed(ActionEvent e) {
		// To run in the current thread.
		run();

		// To run in a new thread.
		// new Thread(this).start();

		// To run in the EDT.
		// javax.swing.SwingUtilities.invokeLater(this);
	}

	/**
	 * The <code>Runnable</code> inherited method to override.
	 */
	public void run() {
	}

}
