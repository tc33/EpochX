package org.epochx.gx.util;

import org.epochx.epox.*;
import org.epochx.gx.node.*;

public final class Utils {
	
	private Utils() {}
	
	public static void removeChild(ASTNode node, int index) {
		Node[] children = node.getChildren();
		int noChildren = children.length-1;
		
		Node[] newChildren = new Node[noChildren];
		
		for (int i=0; i<noChildren; i++) {
			if (i < index) {
				newChildren[i] = children[i];
			} else {
				newChildren[i] = children[i+1];
			}
		}
		
		node.setChildren(newChildren);
	}
	
	public static void insertChild(ASTNode parent, ASTNode child, int index) {
		Node[] children = parent.getChildren();
		int noChildren = children.length+1;
		
		Node[] newChildren = new Node[noChildren];
		
		// Replace all the old ones.
		for (int i=0; i<noChildren; i++) {
			if (i == index) {
				newChildren[index] = child;
			} else if (i < index) {
				newChildren[i] = children[i];
			} else {
				newChildren[i] = children[i-1];
			}
		}
		
		parent.setChildren(newChildren);
	}
}
