package gj.quoridor.player.scaringella;

import java.util.ArrayList;

public class Node {
	
	private int r,c;
	private ArrayList<Node> neighbors;
	
	public Node(int r,int c) {
		this.r=r;
		this.c=c;
		neighbors=new ArrayList<>();		
	}
	
	public void addNeighbor(Node n) {
		neighbors.add(n);
	}
	
	public void removeNeighbor(Node n){
		neighbors.remove(n);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (c != other.c)
			return false;
		if (r != other.r)
			return false;
		return true;
	}
	
	public void linkNode(int r, int c) {
		Node e= new Node(r,c);
		this.neighbors.add(e);
	}

	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public void setR(int r) {
		this.r = r;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

}
