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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.epochx.monitor.graph.GraphVertex;
import org.epochx.refactoring.representation.TreeAble;
import org.epochx.refactoring.representation.TreeNodeAble;

/**
 * A <code>TreeNodeAble</code>.
 */
public class TreeNode implements TreeNodeAble, RootAble, Iterable<TreeNode> {

	////////////////////////////////////////////////////////////////////////////
	//                D A T A   F I E L D S                                   //
	////////////////////////////////////////////////////////////////////////////
	
	private String name;

	private TreeNode parent;

	private TreeNode[] children;

	private TreeNodeAble nodeAbleInstance;

	////////////////////////////////////////////////////////////////////////////
	//            V I S U A L I Z I T I O N   F I E L D S                     //
	////////////////////////////////////////////////////////////////////////////

	private transient double angle;

	private transient double rightLimit;

	private transient double leftLimit;

	private transient boolean selected;

	private transient boolean highlighted;

	private transient Color color;

	private transient int diameter;

	private transient int x;

	private transient int y;

	////////////////////////////////////////////////////////////////////////////
	//                    C O N S T R U C T O R S                             //
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a <code>TreeNode</code>.
	 */
	public TreeNode() {
		this.name = "";
		this.nodeAbleInstance = null;
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
	 * Constructs a <code>TreeNode</code> with a specified name.
	 * 
	 * @param nodeAble the object to set as the nodeAbleInstance. Must be
	 *        "nodeable".
	 * 
	 * @throw ClassCastException if the given object is not "nodeable".
	 * 
	 * @see #setNodeAbleInstance(Object)
	 */
	public TreeNode(Object nodeAble) throws ClassCastException {
		this();
		setNodeAbleInstance(nodeAble);
		if (this.nodeAbleInstance != null) {
			this.name = this.nodeAbleInstance.getName();
			this.children = createChildren();
		}
	}

	/**
	 * Constructs a <code>TreeNode</code> with a specified name.
	 * 
	 * @param root the object to set as the root. Must be "node-able".
	 * @parem parent the parent of the nodeAbleInstance.
	 * 
	 * @throw ClassCastException if the given object is not "node-able".
	 * 
	 * @see #setNodeAbleInstance(Object)
	 */
	public TreeNode(Object nodeAble, TreeNode parent) throws ClassCastException {
		this(nodeAble);
		this.parent = parent;
	}

	////////////////////////////////////////////////////////////////////////////
	//                 G E T T E R S  &  S E T T E R S                        //
	////////////////////////////////////////////////////////////////////////////

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
	 * Returns the nodeAbleInstance.
	 * 
	 * @return the nodeAbleInstance.
	 */
	public TreeNodeAble getNodeAbleInstance() {
		return nodeAbleInstance;
	}

	/**
	 * Sets the <code>TreeNodeAble</code> instance of this node according to the
	 * specified object which must be "nodeable" ; otherwise a
	 * <code>ClassCastException</code> is thrown.
	 * 
	 * @param o the "nodeable" object to set as the <code>TreeNodeAble</code>
	 *        instance of this nodeAbleInstance.
	 * @throws ClassCastException if the given object is not rootable.
	 */
	public void setNodeAbleInstance(Object o) throws ClassCastException {
		if (o instanceof TreeNodeAble) {
			this.nodeAbleInstance = (TreeNodeAble) o;
		} else if (o instanceof TreeAble) {
			setNodeAbleInstance(((TreeAble) o).getTree());
		} else {
			throw new ClassCastException("This object is not an instance of TreeNodeAble :" + o.toString());
		}
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
	 * Returns the root of this node.
	 * 
	 * @return the root of this node.
	 */
	public TreeNode getRoot() {
		if (parent == null) {
			return this;
		} else {
			return parent.getRoot();
		}
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
	 * @throws NullPointerException if the <code>TreeNodeAble</code> instance is
	 *         null.
	 */
	public TreeNode[] createChildren() throws NullPointerException {

		if (nodeAbleInstance == null) {
			throw new NullPointerException("The TreeNodeAble instance is null.");
		}

		TreeNodeAble[] childrenNodeAble = nodeAbleInstance.getChildren();

		children = new TreeNode[childrenNodeAble.length];

		for (int i = 0; i < children.length; i++) {
			children[i] = new TreeNode(childrenNodeAble[i], this);
			children[i].createChildren();
		}

		return children;
	}

	/**
	 * Returns an <code>ArrayList</code> of this node's ancestors.
	 * 
	 * @return the list of this node's ancestors from this
	 *         node to the root.
	 */
	public TreeNode[] ancestors() {

		TreeNode[] res = new TreeNode[level()];

		TreeNode n = getParent();

		int i = 0;
		while (n != null) {
			res[i++] = n;
			n = n.getParent();
		}

		return res;
	}

	/**
	 * Returns an <code>ArrayList</code> of this node's progenies in
	 * in {@link Tree#PRE_ORDER preorder}.
	 * 
	 * @return the list of this node's progenies.
	 * 
	 * @see #getNodes(int);
	 * @see Tree#PRE_ORDER
	 */
	public ArrayList<TreeNode> descendants() {
		return descendants(Tree.PRE_ORDER);
	}

	/**
	 * Returns an <code>ArrayList</code> of this node's progenies
	 * according to the specified order.
	 * 
	 * @return the list of this node's progenies according to the
	 *         specified
	 *         order.
	 * @throw IllegalArgumentException if the specified order is not among :
	 *        <ul>
	 *        <li>{@link Tree#POST_ORDER}
	 *        <li>{@link Tree#PRE_ORDER}
	 *        <ul>
	 */
	public ArrayList<TreeNode> descendants(int iteration) throws IllegalArgumentException {

		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>(size());

		switch (iteration) {

			case Tree.POST_ORDER:
				nodes.add(this);
				for (TreeNode child: children) {
					nodes.addAll(child.descendants(Tree.POST_ORDER));
				}
				break;

			case Tree.PRE_ORDER:
				for (TreeNode child: children) {
					nodes.addAll(child.descendants(Tree.PRE_ORDER));
				}
				nodes.add(this);
				break;

			default:
				throw new IllegalArgumentException("Unknown order.");
		}

		return nodes;
	}

	/**
	 * The <code>Iterable</code> implemented method ; Returns an
	 * <code>Iterator</code> visiting the subtree's nodes in
	 * {@link Tree#POST_ORDER post-order}.
	 * 
	 * @return the iterator of this tree's nodes.
	 * @see #descendants()
	 */
	public Iterator<TreeNode> iterator() {
		return descendants().iterator();
	}

	/**
	 * Returns <code>true</code> is this node has no child nodes.
	 * 
	 * @return <code>true</code> is this node has no child nodes;
	 *         otherwise <code>false</code>.
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
	 * @return the level of this node (in relation to the derivation
	 *         tree).
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
	 * Returns the specified node in the (sub-)tree represented by
	 * this
	 * node. Nodes are indexed from left to right in a top-down
	 * fashion.
	 * 
	 * @param index the index of the node to be found.
	 * 
	 * @return the specified node in the (sub-)tree represented by
	 *         this node.
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
		throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
	}

	/**
	 * Searches for the first occurence of the given object, testing
	 * for equality using the equals method.
	 * 
	 * @param o the object whose index in this (sub-)tree is to be found.
	 * 
	 * @return the index of the first occurrence of the argument in this
	 *         (sub-)tree; returns -1 if the object is not found.
	 * @see #equals(Object)
	 */
	public int indexOf(Object o) {
		return descendants().indexOf(o);
	}

	public TreeNode[] find(Object o) {

		if (o instanceof TreeNode) {
			TreeNode n = (TreeNode) o;
			ArrayList<TreeNode> list = new ArrayList<TreeNode>();

			for (TreeNode node: descendants()) {
				if (node.subsumes(n)) {
					list.add(node);
				}
			}
			TreeNode[] res = new TreeNode[list.size()];
			list.toArray(res);
			return res;

		} else if (o instanceof TreeNodeAble) {
			return find(new TreeNode((TreeNodeAble) o, null));
		} else if (o instanceof TreeAble) {
			return find(((TreeAble) o).getTree());
		}

		return new TreeNode[0];
	}

	public boolean subsumes(TreeNode n) {

		if (n.getName() != getName()) {
			return false;
		} else {
			LinkedList<TreeNode> thisChildren = new LinkedList<TreeNode>(Arrays.asList(this.children));

			for (TreeNode child: n.getChildren()) {

				boolean found = !child.isSelected();
				ListIterator<TreeNode> iterator = thisChildren.listIterator();

				while (iterator.hasNext() && found == false) {
					TreeNode thisChild = iterator.next();
					if (thisChild.subsumes(child)) {
						iterator.remove();
						found = true;
					}
				}
				if (!found) {
					return false;
				}
			}
			return true;
		}

	}

	public boolean isAncestor(TreeNode node) {
		TreeNode n = node;

		while (n != null) {
			if (n == this) {
				return true;
			} else {
				n = n.getParent();
			}
		}
		return false;
	}

	public boolean isDescendant(TreeNode node) {
		TreeNode n = this;

		while (n != null) {
			if (n == node) {
				return true;
			} else {
				n = n.getParent();
			}
		}
		return false;
	}

	public static TreeNode commonAncestor(TreeNode node1, TreeNode node2) {
		if (node1 == null || node2 == null) {
			return null;
		} else if (node1 == node2) {
			return node1;
		} else if (node1.isAncestor(node2)) {
			return node1;
		} else if (node2.isDescendant(node1)) {
			return node2;
		} else if (node1.getRoot() == node2.getRoot()) {

			if (node1.level() > node2.level()) {
				return commonAncestor(node1.getParent(), node2);
			} else {
				return commonAncestor(node1, node2.getParent());
			}

		} else {
			return null;
		}
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
		} else if (o instanceof TreeAble) {
			return equals(((TreeAble) o).getTree());
		} else if (o instanceof GraphVertex) {
			return equals(((GraphVertex) o).getIndividual());
		} else {
			return System.identityHashCode(o) == System.identityHashCode(nodeAbleInstance);
		}

	}

	////////////////////////////////////////////////////////////////////////////
	//              V I S U A L I Z A T I O N   M E T H O D S                 //
	////////////////////////////////////////////////////////////////////////////

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
	 * @param b the selected to set.
	 */
	public void setSelected(boolean b) {
		this.selected = b;
	}

	/**
	 * Sets the selected.
	 * 
	 * @param b the selected to set.
	 */
	public void setSubTreeSelected(boolean b) {
		setSelected(b);
		for (TreeNode child: children) {
			child.setSubTreeSelected(b);
		}
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
	 * @param b the highlighted to set.
	 */
	public void setHighlighted(boolean b) {
		this.highlighted = b;
	}

	/**
	 * Sets the highlighted.
	 * 
	 * @param b the highlighted to set.
	 */
	public void setSubTreeHighlighted(boolean b) {
		setHighlighted(b);
		for (TreeNode child: children) {
			child.setSubTreeHighlighted(b);
		}
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
	public TreeNode clone() {
		try {
			TreeNode clone = (TreeNode) super.clone();
			clone.name = name;
			clone.children = new TreeNode[children.length];
			clone.parent = null;
			clone.nodeAbleInstance = null;

			for (int i = 0; i < children.length; i++) {
				clone.children[i] = children[i].clone();
			}

			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.getMessage());
		}
	}

	@Override
	public String toString() {
		String res = getClass().getSimpleName();
		res += "@" + String.valueOf(System.identityHashCode(this));
		res += "(" + getName() + ")";
		return res;
	}

}
