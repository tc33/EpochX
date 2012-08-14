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
package org.epochx.monitor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

import org.epochx.monitor.dialog.DialogComponentAdder;
import org.epochx.monitor.menu.MenuBar;
import org.epochx.monitor.table.Table;

/**
 * A <code>Monitor</code> instance cans display several visualization components
 * such as
 * tables and charts.
 * 
 * <p>
 * <h3>Construction</h3>
 * The frame name cans be specified in the constructor as well as the number of
 * rows and columns of visualization panes.
 * </p>
 * 
 * 
 * <p>
 * <h3>Component Layout</h3>
 * To add a component to a particular monitor pane, use the
 * {@link #add(JComponent, int, int)} method by specifying the row and column in
 * parameters. <br>
 * It is also possible to register a component in the component list without
 * display it, by using the {@link #add(JComponent)} method. <br>
 * <i> (Note that if the Monitor contains only one pane, the component is
 * automatically add to this one.) </i><br>
 * During the execution, the GUI provides several ways to add and remove
 * components from panes (edit menu, add-button, close-button).<br>
 * <br>
 * Here, there is a sample code which creates a Monitor with <i>one</i> row and
 * <i>two</i> columns and adds a monitoring table to the column <i>one</i> :
 * 
 * <pre>
 * Monitor aMonitor = new Monitor(&quot;Frame_Name&quot;, 1, 2);
 * Table aTable = new Table(&quot;Table_Name&quot;);
 * // .. Table Setting ..
 * monitor.add(aTable, 1, 1);
 * </pre>
 * 
 * To know how to create and set a <code>Table</code> please, see {@link Table}.
 * </p>
 * 
 * <p>
 * <h3>Concurrency</h3>
 * This class is <b>thread-safe</b>, as it has been designed to avoid
 * concurrency issues, between the <i>main thread</i>, the <i>Event Dispached
 * Thread</i> from <code>Swing</code> library, and all the other threads which
 * could access to the shared fields. <br>
 * <br>
 * The <code>Runnable</code> interface is implemented in order to schedule the
 * GUI creation task in the <i>EDT</i> by invoking
 * {@link SwingUtilities#invokeLater(Runnable)} method. <br>
 * <br>
 * However, critical fields (as {@link #componentList}) are accessibles from
 * outside of this class by getter methods. Those fields have to be used <b>very
 * carefully</b>, and always by using the monitor instance as the
 * synchronization <i>lock</i></b>.
 * </p>
 */
public class Monitor extends JFrame implements Runnable {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = 2070510483585647925L;

	/**
	 * The number of created instances.
	 */
	private static int noInstances = 0;

	/**
	 * The <code>MenuBar</code> of the <code>Monitor</code>.
	 */
	private MenuBar menuBar;

	/**
	 * The <code>MonitorContentPane</code> of the <code>Monitor</code>.
	 */
	private final MonitorContentPane contentPane;

	/**
	 * The list of <code>JComponent</code> added to the monitor.
	 * <p>
	 * This list can be accessed from outside of this class by a getter method.
	 * <b>ALL ACCESS</b> have to be <b>synchronized</b> by the
	 * <code>Monitor</code> instance in order to avoid concurrency issues.
	 * </p>
	 */
	private final ArrayList<JComponent> componentList = new ArrayList<JComponent>();

	/**
	 * The grid of <code>JTabbedPane</code>.
	 */
	private final JTabbedPane[][] componentTab;

	/**
	 * Constructs a <code>Monitor</code>.
	 */
	public Monitor() {
		this("MonitorFrame" + noInstances, 1, 1);
	}

	/**
	 * Constructs a <code>Monitor</code>.
	 * 
	 * @param name the name of the frame.
	 */
	public Monitor(String name) {
		this(name, 1, 1);
	}

	/**
	 * Constructs a <code>Monitor</code>.
	 * 
	 * @param rows the number of rows.
	 * @param cols the number of columns.
	 */
	public Monitor(int rows, int cols) {
		this("MonitorFrame" + noInstances, rows, cols);
	}

	/**
	 * Constructs a <code>Monitor</code>.
	 * 
	 * @param name the name of the frame.
	 * @param rows the number of rows.
	 * @param cols the number of columns.
	 */
	public Monitor(String name, int rows, int cols) {
		// Number of Instances incrementation.
		noInstances++;

		// Initializations
		contentPane = new MonitorContentPane(rows, cols);
		componentTab = new JTabbedPane[rows][cols];

		// Frame settings
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and show the frame in the EDT, for thread safety.
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The <code>Runnable</code> inherited method which is invoked in the EDT by
	 * the {@link SwingUtilities#invokeLater(Runnable)} method.
	 */
	public void run() {
		// Add the Menu Bar and the Content Pane.
		menuBar = new MenuBar(this);
		setJMenuBar(menuBar);
		setContentPane(contentPane);

		// componentTab initialization.
		getContentPane().removeAll();
		for (int i = 0; i < componentTab.length; i++)
			for (int j = 0; j < componentTab[i].length; j++) {
				componentTab[i][j] = new JTabbedPane();
				// Add an emptyButton.
				JButton emptyButton = new JButton("Click to add a componant !");
				emptyButton.setPreferredSize(new Dimension(300, 200));
				emptyButton.addActionListener(new DialogComponentAdder(Monitor.this, i + 1, j + 1));
				componentTab[i][j].add(emptyButton);

				// Set the "add-component cross" tab.
				componentTab[i][j].setTabComponentAt(0, new AddButton(i + 1, j + 1));
				getContentPane().add(componentTab[i][j]);
			}

		// Show the frame.
		pack();
		setSize(1400, 800);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Returns the <code>componentList</code>.
	 * 
	 * @return the <code>componentList</code>.
	 */
	public synchronized ArrayList<JComponent> getComponentList() {
		return componentList;
	}

	/**
	 * Returns the number of rows.
	 * 
	 * @return the number of rows.
	 */
	public int getRowCount() {
		return componentTab.length;
	}

	/**
	 * Returns the number of columns.
	 * 
	 * @return the number of columns.
	 */
	public int getColCount() {
		return componentTab[0].length;
	}

	/**
	 * Adds a <code>JComponent</code> on the component list.
	 * <p>
	 * This Component will not be add to the monitor at the start, unless if the
	 * monitor have only one pane.
	 * </p>
	 * 
	 * @param component the JComponent added
	 */
	public synchronized void add(JComponent component) {
		componentList.add(0, component);
		// if the monitor have only one cell, add directly.
		if (getRowCount() == 1 && getColCount() == 1)
			add(component, 1, 1);
		// else refresh the menu bar in the EDT.
		else
			SwingUtilities.invokeLater(menuBar);
	}

	/**
	 * Adds a <code>JComponent</code> on the specified position.
	 * 
	 * @param component the JComponent added
	 * @param row the row position
	 * @param col the column position
	 * 
	 * @throws NullPointerException if row or col is out of range of the
	 *         <code>componentTab</code>.
	 * @throws IllegalArgumentException if <code>component</code> is null.
	 */
	public synchronized void add(JComponent component, int row, int col) throws NullPointerException,
			IllegalArgumentException {
		// Throw NullPointerException if component is null.
		if (component == null) {
			throw new NullPointerException("JComponent is null.");
		}

		row--;
		col--; // Cause tables start at 0.

		// Throw IllegalArgumentException if out of range of the componentTab.
		if (row >= componentTab.length || row < 0)
			throw new IllegalArgumentException("Wrong row index.");
		if (col >= componentTab[0].length || col < 0)
			throw new IllegalArgumentException("Wrong column index.");

		// If the componentList does not contain the component, add it.
		if (!componentList.contains(component))
			componentList.add(component);
		// Else move the component to the end of the list.
		else {
			componentList.remove(component);
			componentList.add(component);
		}

		// Add the component to the tabbed pane and refresh the menu bar in the
		// EDT.
		SwingUtilities.invokeLater(new Adder(component, componentTab[row][col]));
		SwingUtilities.invokeLater(menuBar);
	}

	/**
	 * An <code>Adder</code> is a <code>Runnable</code> implementation which
	 * adds a component to a specific pane.
	 * <p>
	 * Must be run in the EDT and invoked by
	 * {@link SwingUtilities#invokeLater(Runnable)} method.
	 * </p>
	 */
	private class Adder implements Runnable {

		/**
		 * The <code>JComponent</code> to add to the <code>JTabbedPane</code>.
		 */
		private final JComponent component;

		/**
		 * The <code>JTabbedPane</code> in which the <code>JComponent</code>
		 * must
		 * be added.
		 */
		private final JTabbedPane tabbedPane;

		/**
		 * Constructs an <code>Adder</code>.
		 * 
		 * @param component the <code>JComponent</code> to add to the
		 *        <code>JTabbedPane</code>.
		 * @param tabbedPane the <code>JTabbedPane</code> in which the
		 *        <code>JComponent</code> must be added.
		 */
		Adder(JComponent component, JTabbedPane tabbedPane) {
			this.component = component;
			this.tabbedPane = tabbedPane;
		}

		/**
		 * The <code>Runnable</code> inherited method which adds the
		 * <code>JComponent</code> to the <code>JTabbedPane</code>.
		 */
		public void run() {
			synchronized (Monitor.this) {
				// If the component is already here, select it and return.
				if (tabbedPane.indexOfComponent(component) != -1) {
					tabbedPane.setSelectedComponent(component);
					return;
				}

				// Add the component before the "Add a component" tab.
				final int index = tabbedPane.getTabCount() - 1;
				tabbedPane.add(component, index);

				// Create the tab Panel.
				final JPanel buttonTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
				buttonTab.setOpaque(false);
				buttonTab.add(new JLabel(component.getName()));
				buttonTab.add(new CloseButton(component, tabbedPane));

				// Add the tab panel and select it.
				tabbedPane.setTabComponentAt(index, buttonTab);
				tabbedPane.setSelectedComponent(component);

				// Repack the monitor.
				Monitor.this.pack();
			}
		}
	}

	/**
	 * An <code>AddButton</code> paints a cross, and opens a
	 * <code>JDialog</code> to add a component, when pressed.
	 */
	private class AddButton extends JButton {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = 1780280560761515592L;

		/**
		 * The button size.
		 */
		private final int size = 16;

		private AddButton(int row, int col) {
			setPreferredSize(new Dimension(size, size));
			setToolTipText("Add a component");
			// Make the button looks the same for all Laf's.
			setUI(new BasicButtonUI());
			// Make it transparent.
			setContentAreaFilled(false);
			// No need to be focusable.
			setFocusable(false);
			setBorderPainted(false);
			setRolloverEnabled(true);
			addActionListener(new DialogComponentAdder(Monitor.this, row, col));
		}

		// We don't want to update UI for this button.
		@Override
		public void updateUI() {
		}

		// Paint the cross.
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// Shift the image for pressed buttons.
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLUE);
			if (getModel().isRollover()) {
				g2.setColor(Color.GRAY);
			}
			g2.drawLine(4, size / 2, size - 1, size / 2);
			g2.drawLine(size / 2 + 1, 4, size / 2 + 1, size - 4);
			g2.dispose();
		}
	}

	/**
	 * A <code>CloseButton</code> paints a cross to close a tab pane.
	 */
	private class CloseButton extends JButton implements ActionListener {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = -1127235253842674405L;

		/**
		 * The <code>JComponent</code> to remove from the
		 * <code>JTabbedPane</code> .
		 */
		private final JComponent component;

		/**
		 * The <code>JTabbedPane</code> whose the <code>JComponent</code> must
		 * be
		 * removed.
		 */
		private final JTabbedPane tabbedPane;

		/**
		 * The button size.
		 */
		private final int size = 16;

		/**
		 * Constructs a <code>CloseButton</code>.
		 * 
		 * @param component the <code>JComponent</code> to remove from the
		 *        <code>JTabbedPane</code>.
		 * @param tabbedPane the <code>JTabbedPane</code> whose the
		 *        <code>JComponent</code> must be removed.
		 */
		private CloseButton(JComponent component, JTabbedPane tabbedPane) {
			this.tabbedPane = tabbedPane;
			this.component = component;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("Close this tab");
			// Make the button looks the same for all Laf's.
			setUI(new BasicButtonUI());
			// Make it transparent.
			setContentAreaFilled(false);
			// No need to be focusable.
			setFocusable(false);
			setBorderPainted(false);
			setRolloverEnabled(true);
			addActionListener(this);
		}

		/**
		 * Remove the <code>JComponent</code> from the <code>JTabbedPane</code>.
		 * 
		 * @param e the <code>ActionEvent</code>, not used.
		 */
		public void actionPerformed(ActionEvent e) {
			synchronized (Monitor.this) {
				tabbedPane.remove(tabbedPane.indexOfComponent(component));
			}
		}

		// We don't want to update UI for this button.
		@Override
		public void updateUI() {
		}

		@Override
		// Paint the cross.
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			// Shift the image for pressed buttons.
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.BLACK);
			if (getModel().isRollover()) {
				g2.setColor(Color.GRAY);
			}
			g2.drawLine(10, 5, size - 1, size - 6);
			g2.drawLine(size - 1, 5, 10, size - 6);
			g2.dispose();
		}
	}
}
