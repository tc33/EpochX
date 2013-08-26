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
package org.epochx.monitor.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.MouseInputAdapter;

import org.epochx.monitor.tree.TreeEvent.TreeProperty;
import org.epochx.refactoring.representation.TreeAble;
import org.epochx.refactoring.representation.TreeNodeAble;

/**
 * A <code>Tree</code>.
 */
public class Tree extends JPanel implements TreeAble, RootAble, Runnable, Iterable<TreeNode> {

	/**
	 * The <code>long</code>/serialVersionUID.
	 */
	private static final long serialVersionUID = -6378399736278409064L;

	/**
	 * Constant representing the post-order iteration.
	 */
	public static final int POST_ORDER = 1;

	/**
	 * Constant representing the pre-order iteration.
	 */
	public static final int PRE_ORDER = 2;

	/**
	 * Constant representing the level-order iteration.
	 */
	public static final int LEVEL_ORDER = 3;

	/**
	 * Constant representing an angle which has not been yet computed.
	 */
	public static final double NO_ANGLE = 666E66;

	/**
	 * Constant representing the default color.
	 */
	public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

	/**
	 * Constant representing the color for selected nodes.
	 */
	public static final Color SELECTED_COLOR = Color.green;

	/**
	 * Constant representing the color for highlighted nodes.
	 */
	public static final Color HIGHLIGHTED_COLOR = Color.ORANGE;

	/**
	 * The <code>EventListenerList</code>.
	 */
	private final EventListenerList listenerList;

	/**
	 * The <code>TreeView</code>.
	 */
	protected final TreeView view;

	/**
	 * The <code>JScrollPane</code>.
	 */
	protected final JScrollPane scrollPane;

	/**
	 * The root <code>TreeNode</code>.
	 */
	private TreeNode root;

	/**
	 * The <code>TreeNode</code> selected.
	 */
	private TreeNode selectedNode;

	/**
	 * The levels.
	 */
	private TreeLevel[] levels;

	/**
	 * The depth of the tree, computed according to the depth of the root.
	 */
	private int depth;

	/**
	 * The distance between two levels.
	 */
	private int d;

	/**
	 * The diameter of nodes.
	 */
	private int diameter;

	/**
	 * The zoom ratio.
	 */
	private double zoom;

	////////////////////////////////////////////////////////////////////////////
	//                    C O N S T R U C T O R S                             //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a <code>Tree</code>.
	 */
	public Tree() {
		super(new BorderLayout());
		setName("Tree");

		this.listenerList = new EventListenerList();

		this.depth = -1;
		this.root = null;
		this.levels = new TreeLevel[0];

		this.d = 40;
		this.diameter = 20;
		this.zoom = 1.0;

		this.view = new TreeView();
		this.scrollPane = new JScrollPane();

		scrollPane.setViewportView(view);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		add(scrollPane, BorderLayout.CENTER);

		TreeComponentListener componentListener = new TreeComponentListener();
		addComponentListener(componentListener);
		scrollPane.addComponentListener(componentListener);

		TreeMouseListener mouseListener = new TreeMouseListener();
		scrollPane.addMouseMotionListener(mouseListener);
		scrollPane.addMouseListener(mouseListener);
		scrollPane.addMouseWheelListener(mouseListener);

		setPreferredSize(new Dimension(600, 600));
		validate();
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
	 * Constructs a <code>Tree</code> witch the specified object as the root.
	 * 
	 * @param root the object to set as the root. Must be "rootable".
	 * 
	 * @throw ClassCastException if the given object is not "rootable".
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
	 * @param root the object to set as the root. Must be "rootable".
	 * 
	 * @throw ClassCastException if the given object is not "rootable".
	 * 
	 * @see #setRoot(Object)
	 */
	public Tree(String name, Object root) throws ClassCastException {
		this();
		setName(name);
		setRoot(root);
	}

	////////////////////////////////////////////////////////////////////////////
	//             R U N N A B L E   M E T H O D                              //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates the levels and computes the nodes' positions.
	 * <code>Runnable</code> implemented method.
	 * 
	 * @throws IllegalStateException if the root node is null.
	 */
	public final synchronized void run() throws IllegalStateException {

		if (root == null) {
			throw new IllegalStateException("The root node is null.");
		}

		synchronized (levels) {
			this.zoom = 1.0;
			this.selectedNode = null;

			this.depth = root.depth();
			levels = new TreeLevel[depth + 1];

			// Root initialization
			root.setDiameter(getDiameter(), false);
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
					children[i].setDiameter(maxDiameter, false);
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
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				view.repaint();
				view.invalidate();
				scrollPane.validate();
			}
		});

	}

	////////////////////////////////////////////////////////////////////////////
	//             G E T T E R S  &  S E T T E R S                            //
	////////////////////////////////////////////////////////////////////////////	

	/**
	 * Returns the view.
	 * 
	 * @return the view.
	 */
	public TreeView getView() {
		return view;
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
	 * <p>
	 * Automatically computes the positions if the setted root is not null.
	 * </p>
	 * 
	 * @param root the rootable object to set as the root of this tree.
	 * 
	 * @throws ClassCastException if the given object is not rootable.
	 */
	public void setRoot(Object root) throws ClassCastException {
		if (root instanceof TreeNode) {
			TreeNode n = (TreeNode) root;
			if (this.root != n) {
				this.root = n;

				if (this.root != null) {
					new Thread(this, "MONITOR-Tree (" + getName() + ")").start();
				}
			}
		} else if (root instanceof RootAble) {
			setRoot(((RootAble) root).getRoot());
		} else if (root instanceof TreeNodeAble) {
			setRoot(new TreeNode((TreeNodeAble) root, null));
		} else if (root instanceof TreeAble) {
			setRoot(((TreeAble) root).getTree());
		} else {
			throw new ClassCastException("This object is not rootable :" + root.toString());
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
	 * Returns an <code>ArrayList</code> of this tree's nodes in
	 * {@link Tree#POST_ORDER post-order}.
	 * 
	 * @return the list of this tree's nodes.
	 * @see #getNodes(int)
	 */
	public ArrayList<TreeNode> getNodes() {
		return getNodes(POST_ORDER);
	}

	/**
	 * Returns an <code>ArrayList</code> of this tree's nodes according to the
	 * specified order.
	 * 
	 * @return the list of this tree's nodes according to the specified order.
	 * 
	 * @see Tree#POST_ORDER
	 * @see Tree#PRE_ORDER
	 * @see Tree#LEVEL_ORDER
	 */
	public ArrayList<TreeNode> getNodes(int iteration) {

		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();

		if (root != null) {
			switch (iteration) {

				case POST_ORDER:
					nodes = root.descendants(POST_ORDER);
					break;

				case PRE_ORDER:
					nodes = root.descendants(PRE_ORDER);
					break;

				case LEVEL_ORDER:
					nodes.add(root);
					synchronized (levels) {
						for (TreeLevel level: levels) {
							nodes.addAll(Arrays.asList(level.getNodes()));
						}
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
	 * {@link Tree#POST_ORDER post-order}.
	 * 
	 * @return the iterator of this tree's nodes.
	 * @see #getNodes()
	 */
	public Iterator<TreeNode> iterator() {
		return getNodes().iterator();
	}

	/**
	 * Returns the selectedNode.
	 * 
	 * @return the selectedNode.
	 */
	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	/**
	 * Sets the selectedNode.
	 * 
	 * @param selectedNode the selectedNode to set.
	 */
	public void setSelectedNode(TreeNode node) {
		TreeNode old = selectedNode;
		this.selectedNode = node;
		view.repaint();
		fireTreeEvent(new TreeEvent(Tree.this, TreeProperty.SELECTED_NODE, old, selectedNode));
	}

	/**
	 * The <code>TreeAble</code> implemented method. Returns the root.
	 * 
	 * @return the root of this tree.
	 */
	public TreeNodeAble getTree() {
		return root;
	}

	////////////////////////////////////////////////////////////////////////////
	//              R O O T   D E L E G A T E   M E T H O D S                 //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.tree.TreeNode#get(int)
	 */
	public TreeNode get(int index) throws IndexOutOfBoundsException {
		if (root != null) {
			return root.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.tree.TreeNode#find(org.epochx.monitor.tree.TreeNode)
	 */
	public TreeNode[] findSubsumers(TreeNode n, boolean onlySelected) {
		if (root != null) {
			return root.findSubsumers(n, onlySelected);
		} else {
			return new TreeNode[0];
		}
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.tree.TreeNode#setSelected(boolean, boolean)
	 */
	public void setSelected(boolean b) {
		if (root != null) {
			root.setSelected(b, true);
		}
	}

	/**
	 * Delegate method.
	 * 
	 * @see org.epochx.monitor.tree.TreeNode#setHighlighted(boolean, boolean)
	 */
	public void setHighlighted(boolean b) {
		if (root != null) {
			root.setHighlighted(b, true);
		}
	}

	////////////////////////////////////////////////////////////////////////////
	//              V I S U A L I Z A T I O N   M E T H O D S                 //
	////////////////////////////////////////////////////////////////////////////

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
		return (int) (d * zoom * (depth + 1));
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

	/**
	 * Colors the node at the specified index by the given <code>Color</code>.
	 * 
	 * @param index the index of the node to color.
	 * @param color the color.
	 * @param recursively if true the sub-tree will be colored.
	 * @throws IndexOutOfBoundsException if the given index is out of
	 *         bounds.
	 */
	public void color(int index, Color color, boolean recursively) throws IndexOutOfBoundsException {
		get(index).setColor(color, recursively);
	}

	/**
	 * Computes the position of each node, according to its angle.
	 * 
	 * @throws NoYetAngleException if a node has no angle yet.
	 */
	public void resetPositions() throws NoYetAngleException {
		for (TreeLevel level: levels) {
			level.resetPositions();
		}
		view.repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return view.getPreferredSize();
	}

	////////////////////////////////////////////////////////////////////////////
	//            L I S T E N E R S   M A N A G E M E N T                     //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds a <code>TreeListener</code> to the listener list.
	 * 
	 * @param l the listener to add.
	 */
	public void addTreeListener(TreeListener l) {
		listenerList.add(TreeListener.class, l);
	}

	/**
	 * Removes the specified <code>TreeListener</code> from the listener
	 * list.
	 * 
	 * @param l the listener to remove.
	 */
	public void removeTreeListener(TreeListener l) {
		listenerList.remove(TreeListener.class, l);
	}

	/**
	 * Forwards the given notification event to all <code>TreeListener</code>
	 * that registered themselves as listeners
	 * for this graph model.
	 * 
	 * @param e the event to be forwarded
	 */
	public void fireTreeEvent(TreeEvent e) {

		TreeListener[] listeners = listenerList.getListeners(TreeListener.class);

		for (int i = 0; i < listeners.length; i++) {
			listeners[i].treeChanged(e);
		}
	}

	////////////////////////////////////////////////////////////////////////////
	//                 V I E W  C O M P O N E N T                             //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The view component.
	 */
	public class TreeView extends JComponent {

		/**
		 * The serialVersionUID.
		 */
		private static final long serialVersionUID = 4177879123120665134L;

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

				//g2.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
				synchronized (levels) {
					for (TreeLevel level: levels) {
						paintLevel(g2, level);
					}
				}
			}

		}

		private void paintLevel(Graphics2D g, TreeLevel level) {

			if(level != null) {
				// Paint the circle first.
				int diam = (int) (getD() * 2 * level.getLevel() * zoom);
				g.setColor(Color.LIGHT_GRAY);
				g.drawOval(getOrigin() - diam / 2, getOrigin() - diam / 2, diam, diam);

				// Paint each nodes.
				for (TreeNode node: level.getNodes()) {
					paintNode(g, node);
				}
			}
			
		}

		private void paintNode(Graphics2D g, TreeNode node) {

			if(node != null) {

				// paintLimits(g, node);

				// Get graphic attributes
				int x = node.getX();
				int y = node.getY();

				// Paint children's links.
				for (TreeNode child: node.getChildren()) {
					int x2 = child.getX();
					int y2 = child.getY();
					g.setPaint(Color.gray);
					g.drawLine(x, y, x2, y2);
				}

				// Paint the node.
				int diameter = (int) (node.getDiameter() * zoom);
				Color color;
				if (node.isSelected()) {
					color = Tree.SELECTED_COLOR;
				} else if (node.isSelected()) {
					color = Tree.HIGHLIGHTED_COLOR;
				} else {
					color = node.getColor();
				}

				g.setPaint(color);
				g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

				// Paint the name.
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

	////////////////////////////////////////////////////////////////////////////
	//             C O M P O N E N T   L I S T E N E R                        //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The <code>TreeMouseListener</code>.
	 */
	private class TreeComponentListener extends ComponentAdapter {

		public void componentResized(ComponentEvent e) {
			scrollPane.setPreferredSize(getSize());
			scrollPane.getViewport().setPreferredSize(getSize());
		}
	}

	////////////////////////////////////////////////////////////////////////////
	//             M O U S E   L I S T E N E R S                              //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The <code>TreeMouseListener</code>.
	 */
	private class TreeMouseListener extends MouseInputAdapter {

		private final Point pp = new Point();

		private boolean moussePressed = false;

		@Override
		public void mouseClicked(MouseEvent e) {

			Point p = e.getPoint();
			Point vp = scrollPane.getViewport().getViewPosition();
			p.translate(vp.x, vp.y);

			TreeNode node = null;
			for (TreeNode n: Tree.this.getNodes()) {
				if (n.contains(p)) {
					node = n;
				}
			}

			if (!e.isControlDown()) {
				setSelected(false);
				setSelectedNode(null);
			}

			if (node != null) {

				if (!node.isSelected()) {
					node.setSelected(true, !e.isShiftDown());
					if (selectedNode != null) {
						try {
							TreeNode commonAncestor = TreeNode.commonAncestor(selectedNode, node);

							while (node != commonAncestor) {
								node.setSelected(true, false);
								node = node.getParent();
							}
							while (selectedNode != commonAncestor) {
								selectedNode.setSelected(true, false);
								selectedNode = selectedNode.getParent();
							}
							commonAncestor.setSelected(true, false);

							setSelectedNode(commonAncestor);
						} catch (IllegalArgumentException ex) {
							System.err.println(ex.getMessage());
							System.out.println(selectedNode);
							System.out.println(node);
						}

					}

					setSelectedNode(node);

				} else {
					node.setSelected(false, true);
					if (node == selectedNode) {
						setSelectedNode(null);
					} else {
						setSelectedNode(selectedNode);
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			pp.setLocation(e.getPoint());

			moussePressed = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			scrollPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			view.repaint();

			moussePressed = false;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

			if (moussePressed) {

				Point mp = e.getPoint();
				Point vp = (Point) scrollPane.getViewport().getViewPosition().clone();

				double z = -.1 * e.getWheelRotation();
				double currentZoom = getZoom();
				double newZoom = currentZoom + z;

				setZoom(newZoom);

				z = newZoom - currentZoom;

				resetPositions();
				scrollPane.getViewport().setViewSize(view.getPreferredSize());

				vp.x = (int) (vp.x + z * mp.x);
				vp.y = (int) (vp.y + z * mp.x);

				scrollPane.getViewport().setViewPosition(vp);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point cp = e.getPoint();

			Point vp = scrollPane.getViewport().getViewPosition();
			vp.translate(pp.x - cp.x, pp.y - cp.y);

			scrollPane.getViewport().setViewPosition(vp);
			pp.setLocation(cp);
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	}

	@Override
	public String toString() {
		return getName();
	}

}
