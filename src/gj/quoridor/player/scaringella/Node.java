package gj.quoridor.player.scaringella;

import java.util.ArrayList;

public class Node {
	
	private int r,c;
	protected ArrayList<Node> neighbors;
	private Node parent;
	
	public Node(int r,int c) {
		this.r=r;
		this.c=c;
		neighbors=new ArrayList<>();
		
	}
	
	public void addNeighbor(boolean mosse[]) {
		if(mosse[0])this.linkNode(r+2, c);
		if(mosse[1])this.linkNode(r-2, c);
		if(mosse[2])this.linkNode(r, c+2);
		if(mosse[3])this.linkNode(r, c-2);
		
	}
	
	public void linkNode(int r, int c) {
		Node e= new Node(r,c);
		e.parent=this;
		this.neighbors.add(e);
	}

	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public Node getParent() {
		return parent;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

}
