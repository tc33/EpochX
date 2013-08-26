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
package org.epochx.monitor.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.epochx.monitor.graph.Graph.GraphView;

/**
 * A <code>GraphRowHeader</code> display the generation's numbers.
 */
public class GraphRowHeader extends JComponent implements ComponentListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -5122768373913634322L;

	/**
	 * The <code>GraphViewModel</code>.
	 */
	private GraphViewModel viewModel;

	/**
	 * Constructs a <code>GraphRowHeader</code>.
	 */
	public GraphRowHeader() {
		super();

		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Constructs a <code>GraphRowHeader</code>.
	 * 
	 * @param viewModel the <code>GraphViewModel</code>.
	 * 
	 * @throws IllegalArgumentException if null argument.
	 */
	public GraphRowHeader(GraphViewModel viewModel) {
		this();
		setViewModel(viewModel);
	}

	/**
	 * Returns the <code>GraphViewModel</code>.
	 * 
	 * @return the <code>GraphViewModel</code>.
	 */
	public GraphViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * Sets the <code>GraphViewModel</code>.
	 * 
	 * @param viewModel the <code>GraphViewModel</code> to set.
	 */
	public void setViewModel(GraphViewModel viewModel) {
		this.viewModel = viewModel;
	}

	/**
	 * Paints the header.
	 * 
	 * @param g the graphics context to use for painting.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (viewModel == null) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle r = g2.getClipBounds();

		int ymin = (int) r.getY();
		int ymax = (int) (r.getY() + r.getHeight());
		int h = (int) getPreferredSize().getHeight();

		ymax = ymax < h ? ymax : h;
		int firstGeneration = getGenerationAt(ymin);
		int lastGeneration = getGenerationAt(ymax);

		boolean resizeNeeded = false;
		int margin = 3;
		int width = getSize().width;

		//		System.out.println("from : " + firstGeneration + " to : " + lastGeneration);
		//		int c = (int) System.currentTimeMillis() % 250;
		//		Color fillColor = new Color(c, 0, c);
		//		g2.setPaint(fillColor);
		//		g2.fill(g2.getClip());
		//		g2.fillRect(0, 0, width, h);

		for (int i = firstGeneration; i <= lastGeneration; i++) {

			String generation = String.valueOf(i);
			FontMetrics metrics = g2.getFontMetrics();
			int textWidth = metrics.stringWidth(generation) + 4;
			int textHeight = metrics.getAscent();

			if (2 * margin + textWidth + 2 > width) {
				width = 2 * margin + textWidth + 2;
				resizeNeeded = true;
			}

			int y = (int) (viewModel.getMargins().top + i * (viewModel.getDiameter() + viewModel.getVgap()) - viewModel.getDiameter() / 2.0);
			int height = viewModel.getDiameter() > textHeight ? viewModel.getDiameter() : textHeight;
			height += 2;

			Rectangle2D rectangle = new Rectangle2D.Double(0, y, width, height);
			g2.setPaint(Color.GRAY);
			g2.fill(rectangle);
			g2.setColor(Color.black);

			g2.drawString(generation, margin + 2, y + height - (int) ((height - textHeight) / 2.0));

			if (resizeNeeded) {
				Dimension d = new Dimension(width, getPreferredSize().height);
				setPreferredSize(d);
				setSize(d);
			}

		}

	}

	/**
	 * Returns the generation number corresponding to the specified coordinate.
	 * 
	 * @param y the Y axis coordonate.
	 * @return the generation number corresponding to the y coordinate.
	 */
	private int getGenerationAt(int y) {
		
		int res = -1;
		
		if (viewModel != null) {
			int top = viewModel.getMargins().top;
			int width = viewModel.getDiameter() + viewModel.getVgap();

			if (y >= top + width) {

				y = (int) (y - (top + width));

				res = y / width + 1;
			} else {
				res = 0;
			}
		}
		return res;
	}

	////////////////////////////////////////////////////////////////////////////
	//             C O M P O N E N T   L I S T E N E R   M E T H O D S        //
	////////////////////////////////////////////////////////////////////////////

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
		repaint();
	}

	public void componentResized(ComponentEvent e) {

		if (e.getSource() instanceof GraphView) {
			GraphView view = (GraphView) e.getSource();
			Dimension d = new Dimension(getSize().width, view.getHeight());
			setPreferredSize(d);
			setSize(d);
		}

	}

	public void componentShown(ComponentEvent e) {
		repaint();
	}

}
