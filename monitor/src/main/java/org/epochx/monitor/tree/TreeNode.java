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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.epochx.monitor.graph.GraphVertex;
import org.epochx.refactoring.representation.TreeAble;
import org.epochx.refactoring.representation.TreeNodeAble;

/**
 * A <code>TreeNodeAble</code>.
 */
public class TreeNode {

	//

	private String name;

	private TreeNode parent;

	private TreeNode[] children;

	private TreeNodeAble node;

	//

	private double angle;

	private double rightLimit;

	private double leftLimit;

	private boolean selected;

	private boolean highlighted;

	private Color color;

	private int diameter;

	private int x;

	private int y;

	/**
	 * Constructs a <code>TreeNode</code>.
	 */
	public TreeNode() {
		this.name = "";
		this.node = null;
		this.parent = null;
		this.children = new TreeNode[0];

		this.angle = Tree.NO_ANGLE;
		this.rightLimit = Tree.NO_ANGLE;
		this.leftLimit = Tree.NO_ANGLE;

		this.selected = false;
		this.highlighted = false;
		this.color = Color.LIGHT_GRAY;
		this.diameter = 0;
		this.x = 0;
		this.y = 0;
	}

	/**
	 * Constructs a <code>TreeNode</code>.
	 * 
	 * @param tree
	 * @param node
	 * @param parent
	 */
	public TreeNode(TreeNodeAble node, TreeNode parent) {
		this();
		this.parent = parent;
		this.node = node;
		if (node != null) {
			this.name = node.getName();
			this.children = createChildren();
		}
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the node.
	 * 
	 * @return the node.
	 */
	public TreeNodeAble getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 * 
	 * @param node the node to set.
	 */
	public void setNode(TreeNodeAble node) {
		this.node = node;
	}

	/**
	 * Returns the parent.
	 * 
	 * @return the parent.
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent the parent to set.
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	/**
	 * Returns the children.
	 * 
	 * @return the children.
	 */
	public TreeNode[] getChildren() {
		return children;
	}

	/**
	 * Sets the children.
	 * 
	 * @param children the children to set.
	 */
	public void setChildren(TreeNode[] children) {
		this.children = children;
	}
	
	/**
	 * Creates the children of this node with the <code>TreeNodeAble</code>
	 * nodes given by the {@link TreeNodeAble#getChildren()} method.
	 * 
	 * @return the <code>TreeNode</code> created.
	 * @throws NullPointerException if the <code>TreeNodeAble</code> node is
	 *         null.
	 */
	public TreeNode[] createChildren() throws NullPointerException {

		if (node == null) {
			throw new NullPointerException("The TreeNodeAble instance is null.");
		}

		TreeNodeAble[] childrenNodeAble = node.getChildren();

		children = new TreeNode[childrenNodeAble.length];

		for (int i = 0; i < children.length; i++) {
			children[i] = new TreeNode(childrenNodeAble[i], this);
			children[i].createChildren();
		}

		return children;
	}

	/**
	 * Returns an <code>ArrayList</code> of this node's progenies in in
	 * {@link Tree#ITERATION_PRE_ORDER preorder}.
	 * 
	 * @return the list of this node's progenies.
	 * 
	 * @see #getNodes(int);
	 * @see Tree#ITERATION_PRE_ORDER
	 */
	public ArrayList<TreeNode> getNodes() {
		return getNodes(Tree.ITERATION_PRE_ORDER);
	}

	/**
	 * Returns an <code>ArrayList</code> of this node's progenies according to
	 * the specified order.
	 * 
	 * @return the list of this node's progenies according to the specified
	 *         order.
	 * @throw IllegalArgumentException if the specified order is not among :
	 *        <ul>
	 *        <li>{@link Tree#ITERATION_POST_ORDER}
	 *        <li>{@link Tree#ITERATION_PRE_ORDER}
	 *        <ul>
	 */
	public ArrayList<TreeNode> getNodes(int iteration) throws IllegalArgumentException {

		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>(size());

		switch (iteration) {

			case Tree.ITERATION_POST_ORDER:
				nodes.add(this);
				for (TreeNode child: children) {
					nodes.addAll(child.getNodes(Tree.ITERATION_POST_ORDER));
				}
				break;

			case Tree.ITERATION_PRE_ORDER:
				for (TreeNode child: children) {
					nodes.addAll(child.getNodes(Tree.ITERATION_PRE_ORDER));
				}
				nodes.add(this);
				break;

			default:
				throw new IllegalArgumentException("Unknown order.");
		}

		return nodes;
	}

	/**
	 * Returns <code>true</code> is this node has no child nodes.
	 * 
	 * @return <code>true</code> is this node has no child nodes; otherwise
	 *         <code>false</code>.
	 */
	public boolean isLeaf() {
		return children.length == 0;
	}

	/**
	 * Returns the depth of this node.
	 * 
	 * @return the depth of this node.
	 */
	public int depth() {
		if (isLeaf()) {
			return 0;
		}

		int maximum = 0;

		for (TreeNode child: children) {
			maximum = Math.max(maximum, child.depth());
		}

		return maximum + 1;
	}

	/**
	 * Returns the level of this node.
	 * 
	 * @return the level of this node (in relation to the derivation tree).
	 */
	public int level() {
		TreeNode root = parent;
		int level = 0;

		while (root != null) {
			root = root.parent;
			level++;
		}

		return level;
	}

	/**
	 * Returns the total number of nodes of the (sub)tree represented by this
	 * node.
	 * 
	 * @return the total number of nodes of the (sub)tree represented by this
	 *         node.
	 */
	public int size() {
		int size = 1;

		for (TreeNode child: children) {
			size += child.size();
		}

		return size;
	}

	/**
	 * Returns the specified node in the (sub-)tree represented by this
	 * node. Nodes are indexed from left to right in a top-down fashion.
	 * 
	 * @param index the index of the node to be found.
	 * 
	 * @return the specified node in the (sub-)tree represented by this
	 *         node.
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 */
	public TreeNode get(int index) throws IndexOutOfBoundsException {
		int current = 1;
		LinkedList<TreeNode> nodes = new LinkedList<TreeNode>();
		nodes.add(this);

		while (!nodes.isEmpty()) {
			TreeNode node = nodes.removeFirst();

			if (index == current) {
				return node;
			} else {
				nodes.addAll(0, Arrays.asList(node.children));
			}

			current++;
		}
		throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size());
	}

	/**
	 * Searches for the first occurence of the given node, testing for
	 * equality using the equals method.
	 * 
	 * @param node the node whose index in this (sub-)tree is to be found.
	 * 
	 * @return the index of the first occurrence of the argument in this
	 *         (sub-)tree; returns -1 if the node is not found.
	 * @see #equals(TreeNode)
	 */
	public int indexOf(Object o) {
		return getNodes().indexOf(o);
	}
	
	public boolean similar(Object o) {
		//TODO 
		if (o instanceof TreeNode) {
			TreeNode n = (TreeNode) o;
		}
		else if (o instanceof TreeAble) {
			return similar(((TreeAble) o).getTree());
		}
		else if (o instanceof GraphVertex) {
			return similar(((GraphVertex) o).getIndividual());
		}
	
		return false;
	}
	
	public boolean equals(Object o) {

		if (o instanceof TreeNode) {
			TreeNode n = (TreeNode) o;

			if (!getName().equals(n.getName())) {
				return false;
			}/*
			 * if( parent != null && n.getParent()!=null) {
			 * if( parent.getName().equals(n.getParent().getName()) {
			 * return false;
			 * }
			 * }
			 */
			if (children.length != n.getChildren().length) {
				return false;
			}
			for (int i = 0; i < children.length; i++) {
				if (!children[i].equals(n.getChildren()[i])) {
					return false;
				}
			}
			return true;
		}
		else if (o instanceof TreeAble) {
			return equals(((TreeAble) o).getTree());
		}
		else if (o instanceof GraphVertex) {
			return equals(((GraphVertex) o).getIndividual());
		}
		else {
			return System.identityHashCode(o) == System.identityHashCode(node);
		}

	}

	// Vizualization Methods //

	/**
	 * Returns the angle.
	 * 
	 * @return the angle.
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Sets the angle.
	 * 
	 * @param angle the angle to set.
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * Returns the rightLimit.
	 * 
	 * @return the rightLimit.
	 */
	public double getRightLimit() {
		return rightLimit;
	}

	/**
	 * Sets the rightLimit.
	 * 
	 * @param rightLimit the rightLimit to set.
	 */
	public void setRightLimit(double rightLimit) {
		this.rightLimit = rightLimit;
	}

	/**
	 * Returns the leftLimit.
	 * 
	 * @return the leftLimit.
	 */
	public double getLeftLimit() {
		return leftLimit;
	}

	/**
	 * Sets the leftLimit.
	 * 
	 * @param leftLimit the leftLimit to set.
	 */
	public void setLeftLimit(double leftLimit) {
		this.leftLimit = leftLimit;
	}

	/**
	 * Returns the selected.
	 * 
	 * @return the selected.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected.
	 * 
	 * @param selected the selected to set.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Returns the highlighted.
	 * 
	 * @return the highlighted.
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * Sets the highlighted.
	 * 
	 * @param highlighted the highlighted to set.
	 */
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	/**
	 * Returns the color.
	 * 
	 * @return the color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the color to set.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the color for all the sub-tree.
	 * 
	 * @param color the color to set.
	 */
	public void setSubTreeColor(Color color) {
		setColor(color);
		for (TreeNode child: children) {
			child.setSubTreeColor(color);
		}
	}

	/**
	 * Returns the diameter.
	 * 
	 * @return the diameter.
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * Sets the diameter.
	 * 
	 * @param diameter the diameter to set.
	 */
	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	/**
	 * Returns the x.
	 * 
	 * @return the x.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 * 
	 * @param x the x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the y.
	 * 
	 * @return the y.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 * 
	 * @param y the y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns the centre location of this node.
	 * 
	 * @return the centre location of this node.
	 */
	public Point getCentre() {
		return new Point(x, y);
	}

	/**
	 * Sets the centre location of this node.
	 * 
	 * @param x the X coordinate.
	 * @param y the Y coordinate.
	 */
	public void setCentre(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the bounds of this node.
	 * 
	 * @return the bounds of this node.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x - diameter / 2, y - diameter / 2, diameter, diameter);
	}

	/**
	 * Checks if the specified point is inside this node's bounds.
	 * 
	 * @param p the specicied point whose location is to be check.
	 * @return true if the specified point is inside this node's bounds; false
	 *         otherwise.
	 */
	public boolean contains(Point p) {
		Rectangle bounds = getBounds();
		return bounds.contains(p);
	}

	@Override
	public String toString() {
		String res = getClass().getSimpleName();
		res += "@" + String.valueOf(System.identityHashCode(this));
		res += "(" + getName() + ")";
		return res;
	}
}
