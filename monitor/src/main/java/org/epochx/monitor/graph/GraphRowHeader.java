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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.epochx.Config;
import org.epochx.MaximumGenerations;
import org.epochx.monitor.graph.GraphViewEvent.Property;

/**
 * A <code>GraphRowHeader</code>.
 */
public class GraphRowHeader extends JComponent implements GraphViewListener {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -5122768373913634322L;

	/**
	 * The <code>GraphViewModel</code>/viewModel.
	 */
	private GraphViewModel viewModel;

	private int maxGenerationNumber;
	
	private int margin;

	private int textWidth;

	/**
	 * Constructs a <code>GraphRowHeader</code>.
	 * 
	 * @param viewModel
	 */
	public GraphRowHeader(GraphViewModel viewModel) {
		super();
		this.viewModel = viewModel;
		this.margin = 3;
		this.maxGenerationNumber = Config.getInstance().get(MaximumGenerations.MAXIMUM_GENERATIONS) + 1;
		this.textWidth = String.valueOf(maxGenerationNumber).length() * 10;
		
		setBorder(BorderFactory.createEtchedBorder());
		
		
		resize();
	}

	/**
	 * 
	 */
	public void viewChanged(GraphViewEvent e) {
		Property property = e.getProperty();

		switch (property) {

			case DIAMETER:
			case VGAP:
			case HGAP:
				resize();
				repaint();
				break;
			default:
				// do nothing
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Rectangle r = g2.getClipBounds();

		int ymin = (int) r.getY();
		int ymax = (int) (r.getY() + r.getHeight());

		int firstGeneration = getGenerationAt(ymin);
		int lastGeneration = getGenerationAt(ymax);

		/*
		 * System.out.println("from : "+firstGeneration+" to : "+lastGeneration);
		 * int c = (int) System.currentTimeMillis()%250;
		 * Color fillColor = new Color(c, 0, c);
		 * g2.setPaint(fillColor);
		 * g2.fill(g2.getClip());
		 * //
		 */

		for (int i = firstGeneration; i <= lastGeneration; i++) {

			String generation = String.valueOf(i);
			FontMetrics metrics = g2.getFontMetrics();
			int textHeight = metrics.getAscent();

			int x = margin;
			int y = (int) (viewModel.getMargins().top + i * (viewModel.getDiameter() + viewModel.getVgap()) - viewModel.getDiameter() / 2.0);
			int height = viewModel.getDiameter() > textHeight ? viewModel.getDiameter() : textHeight;
			height += 2;

			Rectangle2D rectangle = new Rectangle2D.Double(x, y, textWidth, height);
			g2.setPaint(Color.GRAY);
			g2.fill(rectangle);
			g2.setColor(Color.black);

			g2.drawString(generation, margin + 2, y + height - (int) ((height - textHeight) / 2.0));

		}

	}
	
	/**
	 * Returns the generation number corresponding to the specified coordinate.
	 * 
	 * @param y the Y axis coordonate.
	 * @return the generation number corresponding to the y coordinate.
	 */
	public int getGenerationAt(int y) {
		int top = viewModel.getMargins().top;
		int width = viewModel.getDiameter() + viewModel.getVgap();
		int res;
		if (y >= top + viewModel.getDiameter() / 2.0) {

			y = (int) (y - top + viewModel.getDiameter() / 2.0);

			res = y / width + 1;
		} else {
			res = 0;
		}

		return res;
	}

	/**
	 * Computes and sets the size of this view.
	 * 
	 * @return the preferred size computed.
	 */
	public Dimension adjustPreferredSize() {
		
		int width = 2*margin + textWidth;
		int height = viewModel.getMargins().top + viewModel.getMargins().bottom + maxGenerationNumber
				* (viewModel.getDiameter() + viewModel.getVgap());

		Dimension d = new Dimension(width, height);

		setPreferredSize(d);

		return d;
	}

	/**
	 * Computes the preferred size, sets it at the real size and returns it.
	 * 
	 * @return the size.
	 */
	public Dimension resize() {
		Dimension d = adjustPreferredSize();
		setSize(d);
		return d;
	}

}
