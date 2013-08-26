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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A <code>TreeLevel</code>.
 */
public class TreeLevel implements Iterable<TreeNode> {

	/**
	 * The parent <code>Tree</code>.
	 */
	Tree tree;

	/**
	 * The level number.
	 */
	int level;

	/**
	 * The list of node.
	 */
	LinkedList<TreeNode> nodes;

	/**
	 * 
	 * Constructs a <code>TreeLevel</code>.
	 * 
	 * @param tree the parent <code>Tree</code>.
	 * @param l the level number.
	 */
	public TreeLevel(Tree tree, int level) {
		this.tree = tree;
		this.level = level;
		this.nodes = new LinkedList<TreeNode>();
	}

	/**
	 * Returns the level.
	 * 
	 * @return the level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 * 
	 * @param level the level to set.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Adds nodes to this level.
	 * 
	 * @param nodes the nodes to add.
	 */
	public void addNodes(TreeNode ... nodes) {
		synchronized (this.nodes) {
			for (TreeNode node: nodes) {
				this.nodes.add(node);
			}
		}
	}

	/**
	 * Returns an array of this level's nodes.
	 * 
	 * @return an array of this level's nodes; Can be empty.
	 */
	public TreeNode[] getNodes() {
		TreeNode[] res = null;

		synchronized (nodes) {
			res = new TreeNode[nodes.size()];
			nodes.toArray(res);
		}
		return res;
	}

	/**
	 * The <code>Iterable</code> implemented method ; Returns an
	 * <code>Iterator</code> of this level's nodes.
	 * 
	 * @return the iterator of this level's nodes.
	 */
	public Iterator<TreeNode> iterator() {
		return nodes.iterator();
	}

	/**
	 * Computes the left and right limits of each nodes of this level.
	 * 
	 * @throws NoYetAngleException if a node has no angle yet.
	 */
	public void computeLimits() throws NoYetAngleException {

		TreeNode currentNode = null;

		TreeNode lastParentNode = null;

		TreeNode firstParentNode = null;

		synchronized (nodes) {
			ListIterator<TreeNode> iterator = nodes.listIterator();

			int d = tree.getD();
			double arcAngle = Math.acos((1.0 * d * level) / (1.0 * d * (level + 1)));

			while (iterator.hasNext()) {

				currentNode = iterator.next();

				if (!currentNode.isLeaf()) {

					if (lastParentNode != null) {

						double currentAngle = currentNode.getAngle();
						double lastAngle = lastParentNode.getAngle();

						if (currentAngle == Tree.NO_ANGLE) {
							throw new NoYetAngleException(currentNode);
						}

						// Compute the bisector limit.
						double bisectorLimit = (lastAngle + currentAngle) / 2.0;

						// Compute the tangent limit of the last node.
						double lastTangentLimit = lastAngle + arcAngle;

						// Compute the tangent limit of the current node.
						double currentTangentLimit = currentAngle - arcAngle;

						lastParentNode.setRightLimit(Math.min(lastTangentLimit, bisectorLimit));
						currentNode.setLeftLimit(Math.max(currentTangentLimit, bisectorLimit));

					} else {
						// if the current node is the first parent node, store
						// it.
						firstParentNode = currentNode;
					}
					lastParentNode = currentNode;
				}
			}

			// Compute the last limit.
			if (firstParentNode != null) {
				double firstAngle = firstParentNode.getAngle();
				double lastAngle = lastParentNode.getAngle();

				// Compute the bisector limit.
				double lastBisectorLimit = (lastAngle + firstAngle) / 2.0 + Math.PI;
				double firstBisectorLimit = (lastAngle + firstAngle) / 2.0 - Math.PI;

				// Compute the tangent limit of the last node.
				double lastTangentLimit = lastAngle + arcAngle;

				// Compute the tangent limit of the current node.
				double firstTangentLimit = firstAngle - arcAngle;

				lastParentNode.setRightLimit(Math.min(lastTangentLimit, lastBisectorLimit));
				firstParentNode.setLeftLimit(Math.max(firstTangentLimit, firstBisectorLimit));

			}
		}
	}

	/**
	 * Computes the position of each node, according to its angle.
	 * 
	 * @throws NoYetAngleException if a node has no angle yet.
	 */
	public void resetPositions() throws NoYetAngleException {

		double d = tree.getD() * level * tree.getZoom();
		int origin = tree.getOrigin();

		synchronized (nodes) {
			for (TreeNode node: nodes) {

				double angle = node.getAngle();

				if (angle == Tree.NO_ANGLE) {
					throw new NoYetAngleException(node);
				}

				node.setX((int) (d * (Math.cos(angle)) + origin));
				node.setY((int) (d * (Math.sin(angle)) + origin));
			}
		}
	}

	/**
	 * Computes attributs of children of this level's node.
	 * 
	 * @throws NoYetLimitsException if a node's limits haven't been defined yet.
	 */
	public void computeChildrenAttributes() throws NoYetLimitsException {
		synchronized (nodes) {
			for (TreeNode node: nodes) {
				computeChildrenAttributes(node);
			}
		}
	}

	/**
	 * Computes attributs of children of the specified node.
	 * 
	 * @param node the node whose children's attributes have to be computed.
	 * @throws NoYetLimitsException if the specified node's limits haven't been
	 *         defined yet.
	 */
	public void computeChildrenAttributes(TreeNode node) throws NoYetLimitsException {

		int d = tree.getD() * (level + 1);
		int origin = tree.getOrigin();

		TreeNode[] children = node.getChildren();

		if (children != null && children.length != 0) {

			double rightLimit = node.getRightLimit();
			double leftLimit = node.getLeftLimit();

			if (rightLimit == Tree.NO_ANGLE || leftLimit == Tree.NO_ANGLE) {
				throw new NoYetLimitsException(node);
			}

			double interval = rightLimit - leftLimit;
			double n = children.length + 1;
			int maxDiameter = Math.min(tree.getDiameter(), (int) (level * tree.getD() * interval / n));

			for (int i = 0; i < children.length; i++) {
				double angle = leftLimit + (i + 1) / n * interval;
				children[i].setAngle(angle);
				children[i].setDiameter(maxDiameter, false);
				children[i].setX((int) (d * (Math.cos(angle)) + origin));
				children[i].setY((int) (d * (Math.sin(angle)) + origin));
			}
		}
	}

	@Override
	public String toString() {
		String res = getClass().getSimpleName();

		res += "@" + String.valueOf(System.identityHashCode(this));
		res += "[#" + level + ",";
		synchronized (nodes) {
			res += nodes.toString();
		}
		res += "]";
		return res;
	}

}
