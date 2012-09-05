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
package org.epochx.monitor.tree;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.epochx.monitor.graph.GraphVertex;
import org.epochx.refactoring.representation.TreeAble;
import org.epochx.refactoring.representation.TreeNodeAble;

/**
 * A <code>Tree</code>.
 */
public class Tree extends JScrollPane implements Rootable, Runnable, Iterable<TreeNode> {

	/**
	 * The <code>long</code>/serialVersionUID.
	 */
	private static final long serialVersionUID = -6378399736278409064L;

	/**
	 * Constant representing an angle which has not been yet computed.
	 */
	public static final double NO_ANGLE = 666E66;

	/**
	 * Constant representing the post-order iteration.
	 */
	public static final int ITERATION_POST_ORDER = 1;

	/**
	 * Constant representing the pre-order iteration.
	 */
	public static final int ITERATION_PRE_ORDER = 2;

	/**
	 * Constant representing the level-order iteration.
	 */
	public static final int ITERATION_LEVEL_ORDER = 3;

	/**
	 * The number of created instances.
	 */
	private static int noInstances = 0;

	/**
	 * The <code>TreeView</code>.
	 */
	private TreeView view;

	/**
	 * The <code>JViewport</code>.
	 */
	private JViewport viewPort;

	/**
	 * The root <code>TreeNode</code>.
	 */
	private TreeNode root;

	/**
	 * The levels.
	 */
	private TreeLevel[] levels;

	/**
	 * The depth of the tree, computed according to the depth of the root.
	 */
	int depth;

	/**
	 * The distance between two levels.
	 */
	int d;

	/**
	 * The diameter of nodes.
	 */
	int diameter;

	/**
	 * Constructs a <code>Tree</code>.
	 */
	public Tree() {
		super();

		setName("Tree " + noInstances);

		this.d = 40;
		this.diameter = 20;
		this.depth = -1;
		this.root = null;

		this.view = new TreeView();

		this.viewPort = new JViewport();
		viewPort.setView(view);

		TreeMouseListener mouseListener = new TreeMouseListener();
		viewPort.addMouseMotionListener(mouseListener);
		viewPort.addMouseListener(mouseListener);
		viewPort.addMouseWheelListener(mouseListener);

		setViewport(viewPort);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(700, 700));

		noInstances++;

	}

	/**
	 * Constructs a <code>Tree</code> with a specified name.
	 * 
	 * @param name the name of the tree.
	 */
	public Tree(String name) {
		this();
		setName(name);
	}

	/**
	 * Constructs a <code>Tree</code> according to the specified
	 * <code>TreeNodeAble</code> instance as the root.
	 * 
	 * @param rootNodeAble the <code>TreeNodeAble</code> instance.
	 * 
	 * @throw ClassCastException if the vertex's individual is not an
	 *        instance of TreeAble.
	 * 
	 * @see #setRoot(Object)
	 */
	public Tree(Object root) {
		this();
		setRoot(root);
	}

	/**
	 * Constructs a <code>Tree</code> with a specified name.
	 * 
	 * @param name the name of the tree.
	 * @param rootNodeAble the <code>TreeNodeAble</code> instance.
	 * 
	 * @throw ClassCastException if the vertex's individual is not an
	 *        instance of TreeAble.
	 */
	public Tree(String name, Object root) throws ClassCastException {
		this();
		setName(name);
		setRoot(root);
	}

	/**
	 * Creates the levels and computes the nodes' positions.
	 * <code>Runnable</code> implemented method.
	 * 
	 * @throws IllegalStateException if the root node is null.
	 */
	public void run() throws IllegalStateException {

		if (root == null) {
			throw new IllegalStateException("The root node is null.");
		}

		this.depth = root.depth();
		levels = new TreeLevel[depth + 1];

		// Root initialization
		root.setDiameter(getDiameter());
		root.setAngle(0);
		root.setLeftLimit(0);
		root.setRightLimit(2 * Math.PI);
		root.setX(getOrigin());
		root.setY(getOrigin());
		levels[0] = new TreeLevel(this, 0);
		levels[0].addNodes(root);

		// First level initialization
		if (depth > 0) {
			levels[1] = new TreeLevel(this, 1);

			TreeNode[] children = root.getChildren();
			levels[1].addNodes(children);

			double n = children.length;
			int maxDiameter = Math.min(getDiameter(), (int) (getD() * 2 * Math.PI / n));
			int origin = getOrigin();
			for (int i = 0; i < children.length; i++) {
				double angle = i / n * 2 * Math.PI;
				children[i].setAngle(angle);
				children[i].setDiameter(maxDiameter);
				children[i].setX((int) (d * (Math.cos(angle)) + origin));
				children[i].setY((int) (d * (Math.sin(angle)) + origin));
			}
			levels[1].computeLimits();
			levels[1].computeChildrenAttributes();
		}

		// Others levels initialization
		for (int i = 2; i < levels.length; i++) {

			levels[i] = new TreeLevel(this, i);
			for (TreeNode node: levels[i - 1].getNodes()) {
				levels[i].addNodes(node.getChildren());
			}

			levels[i].computeLimits();
			levels[i].computeChildrenAttributes();
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				viewPort.doLayout();
				viewPort.setPreferredSize(getPreferredSize());
			}
		});

	}

	/**
	 * Returns the view.
	 * 
	 * @return the view.
	 */
	public TreeView getView() {
		return view;
	}

	/**
	 * Sets the view.
	 * 
	 * @param view the view to set.
	 */
	public void setView(TreeView view) {
		this.view = view;
	}

	/**
	 * Returns the viewPort.
	 * 
	 * @return the viewPort.
	 */
	public JViewport getViewPort() {
		return viewPort;
	}

	/**
	 * Sets the viewPort.
	 * 
	 * @param viewPort the viewPort to set.
	 */
	public void setViewPort(JViewport viewPort) {
		this.viewPort = viewPort;
	}

	/**
	 * Returns the root.
	 * 
	 * @return the root.
	 */
	public TreeNode getRoot() {
		return root;
	}

	/**
	 * Sets the root according to the specified object which must be "rootable"
	 * ; otherwise a <code>ClassCastException</code> is thrown.
	 * 
	 * @param o the rootable object to set as the root of this tree.
	 * @throws ClassCastException if the given object is not rootable.
	 */
	public void setRoot(Object o) throws ClassCastException {
		if (o instanceof TreeNode) {
			TreeNode root = (TreeNode) o;
			if (this.root != root) {
				this.root = root;
				if (this.root != null) {
					new Thread(this, "MONITOR-Tree (" + getName() + ")").start();
				}

			}
		} else if (o instanceof Tree) {
			setRoot(((Tree) o).getRoot());
		}
		else if (o instanceof TreeNodeAble) {
			setRoot(new TreeNode((TreeNodeAble) o, null));
		}
		else if (o instanceof TreeAble) {
			setRoot(((TreeAble) o).getTree());
		}
		else if (o instanceof GraphVertex) {
			setRoot(((GraphVertex) o).getIndividual());
		}
		else {
			throw new ClassCastException("This object is not rootable :" + o.toString());
		}
	}

	/**
	 * Returns the levels.
	 * 
	 * @return the levels.
	 */
	public TreeLevel[] getLevels() {
		return levels;
	}

	/**
	 * Returns the depth.
	 * 
	 * @return the depth.
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Returns the distance beetween two levels.
	 * 
	 * @return the distance beetween two levels.
	 */
	public int getD() {
		return d;
	}

	/**
	 * Sets the distance beetween two levels.
	 * 
	 * @param d the distance to set.
	 * @throw IllegalArgumentException if d < diameter.
	 */
	public void setD(int d) throws IllegalArgumentException {
		if (d < diameter) {
			throw new IllegalArgumentException("d must be > diameter : " + d + "<" + diameter);
		}
		this.d = d;
	}

	/**
	 * Returns the origin point.
	 * 
	 * @return the origin point, i.e. the center of the root.
	 */
	public int getOrigin() {
		return d * (depth + 1);
	}

	/**
	 * Returns the diameter of each nodes.
	 * 
	 * @return the diameter of each nodes.
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Sets the diameter of each nodes.
	 * 
	 * @param diameter the diameter to set.
	 * @throw IllegalArgumentException if diameter > d.
	 */
	public void setDiameter(int diameter) {
		if (diameter > d) {
			throw new IllegalArgumentException("diameter must be < d, yet : " + diameter + ">" + d);
		}
		this.diameter = diameter;
	}

	public TreeNode get(int position) {
		return root.get(position);
	}

	/**
	 * Returns an <code>ArrayList</code> of this tree's nodes in
	 * {@link Tree#ITERATION_PRE_ORDER preorder}.
	 * 
	 * @return the list of this tree's nodes.
	 * @see #getNodes(int)
	 */
	public ArrayList<TreeNode> getNodes() {
		return getNodes(Tree.ITERATION_PRE_ORDER);
	}

	/**
	 * Returns an <code>ArrayList</code> of this tree's nodes according to the
	 * specified order.
	 * 
	 * @return the list of this tree's nodes according to the specified order.
	 * 
	 * @see Tree#ITERATION_PRE_ORDER
	 * @see Tree#ITERATION_POST_ORDER
	 * @see Tree#ITERATION_LEVEL_ORDER
	 */
	public ArrayList<TreeNode> getNodes(int iteration) {

		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();

		if (root != null) {
			switch (iteration) {

				case ITERATION_POST_ORDER:
					nodes = root.getNodes(ITERATION_POST_ORDER);
					break;

				case ITERATION_PRE_ORDER:
					nodes = root.getNodes(ITERATION_PRE_ORDER);
					break;

				case ITERATION_LEVEL_ORDER:
					nodes.add(root);
					for (TreeLevel level: levels) {
						nodes.addAll(Arrays.asList(level.getNodes()));
					}
					break;

				default:
					throw new IllegalArgumentException("Unknown order.");

			}
		}

		return nodes;
	}

	/**
	 * The <code>Iterable</code> implemented method ; Returns an
	 * <code>Iterator</code> visiting the tree's nodes in
	 * {@link Tree#ITERATION_PRE_ORDER preorder}.
	 * 
	 * @return the iterator of this tree's nodes.
	 * @see #getNodes()
	 */
	public Iterator<TreeNode> iterator() {
		return getNodes().iterator();
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * The view component.
	 */
	class TreeView extends JComponent {

		/**
		 * The serialVersionUID.
		 */
		private static final long serialVersionUID = 4177879123120665134L;

		/**
		 * The zoom ratio.
		 */
		private double zoom = 1.0;

		/**
		 * Returns the zoom.
		 * 
		 * @return the zoom.
		 */
		public double getZoom() {
			return zoom;
		}

		/**
		 * Sets the zoom.
		 * 
		 * @param zoom the zoom to set.
		 */
		public void setZoom(double zoom) {
			this.zoom = zoom > 0.1 ? zoom : this.zoom;
		}

		@Override
		public Dimension getPreferredSize() {
			int size = (int) (d * 2.0 * (depth + 1) * zoom);
			return new Dimension(size, size);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			if (root == null) {

			} else {

				// g2.fillRect(0, 0, getPreferredSize().width,
				// getPreferredSize().height);

				g2.scale(zoom, zoom);

				for (TreeLevel level: levels) {
					paintLevel(g2, level);
				}
			}

		}

		private void paintLevel(Graphics2D g, TreeLevel level) {

			// Paint the circle first.
			int diam = getD() * 2 * level.getLevel();
			g.setColor(Color.LIGHT_GRAY);
			g.drawOval(getOrigin() - diam / 2, getOrigin() - diam / 2, diam, diam);

			// Paint each nodes.
			for (TreeNode node: level.getNodes()) {
				paintNode(g, node);
			}
		}

		private void paintNode(Graphics2D g, TreeNode node) {

			// paintLimits(g, node);

			// Get graphic attributes
			int x = node.getX();
			int y = node.getY();
			int diameter = node.getDiameter();

			// Paint children's links.
			for (TreeNode child: node.getChildren()) {
				int x2 = child.getX();
				int y2 = child.getY();
				g.setPaint(Color.gray);
				g.drawLine(x, y, x2, y2);
			}

			g.setPaint(node.getColor());
			g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

			String name = node.getName();

			int fontSize = diameter / name.length();
			Font font = new Font("Arial", Font.BOLD, fontSize);
			g.setFont(font);

			FontMetrics metrics = g.getFontMetrics();
			int textWidth = metrics.stringWidth(name);
			int textHeight = metrics.getAscent();
			g.setColor(Color.black);
			g.drawString(name, x - textWidth / 2, y + textHeight / 2);

		}

		@SuppressWarnings("unused")
		private void paintLimits(Graphics2D g, TreeNode node) {

			if (!node.isLeaf()) {
				g.setPaint(Color.blue);
				int x2 = (int) ((getD() * (node.level() + 1)) * (Math.cos(node.getLeftLimit())) + getOrigin());
				int y2 = (int) ((getD() * (node.level() + 1)) * (Math.sin(node.getLeftLimit())) + getOrigin());
				g.drawLine(node.getX(), node.getY(), x2, y2);

				g.setPaint(Color.red);
				x2 = (int) ((getD() * (node.level() + 1)) * (Math.cos(node.getRightLimit())) + getOrigin());
				y2 = (int) ((getD() * (node.level() + 1)) * (Math.sin(node.getRightLimit())) + getOrigin());
				g.drawLine(node.getX(), node.getY(), x2, y2);

			}
		}
	}

	/**
	 * The <code>TreeMouseListener</code>.
	 */
	class TreeMouseListener extends MouseInputAdapter {

		private final Point pp = new Point();

		private boolean moussePressed = false;

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			view.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			pp.setLocation(e.getPoint());

			moussePressed = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			view.repaint();

			moussePressed = false;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			viewPort.validate();

			if (moussePressed) {

				double z = .1 * e.getWheelRotation();
				view.setZoom(view.getZoom() + z);
				view.invalidate();

				viewPort.validate();
				view.repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point cp = e.getPoint();

			Point vp = viewPort.getViewPosition();
			vp.translate(pp.x - cp.x, pp.y - cp.y);

			viewPort.setViewPosition(vp);
			pp.setLocation(cp);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			for (TreeNode node: Tree.this) {
				if (node.contains(e.getPoint())) {
					// node.setColor(Color.GREEN);
				}
			}
			view.repaint();
		}
	}

}
