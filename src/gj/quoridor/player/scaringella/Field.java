package gj.quoridor.player.scaringella;

import java.util.Iterator;
import java.util.LinkedList;

public class Field {
	private boolean mosse[] = new boolean[4];
	private char campo[][] = new char[17][17];
	private int r, c;
	private boolean first;

	public Field() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				campo[i * 2][j * 2] = 'O';
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 8; j++)
				campo[i * 2][j * 2 + 1] = '-';
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 9; j++)
				campo[i * 2 + 1][j * 2] = '|';
		r = 0;
		c = 8;
		campo[r][c] = 'R';
		campo[16][8] = 'B';
		mosse[0] = true;
		mosse[1] = false;
		mosse[2] = true;
		mosse[3] = true;
	}

	public void placedWall(int n) {
		if (n % 16 < 8) {// verticale
			campo[(n / 16) * 2][(n % 16) * 2 + 1] = ' ';
			campo[(n / 16) * 2 + 2][(n % 16) * 2 + 1] = ' ';
			return;
		} // orrizzontale
		campo[(n / 16) * 2 + 1][((n + 8) % 16) * 2] = ' ';
		campo[(n / 16) * 2 + 1][((n + 8) % 16) * 2 + 2] = ' ';
	}

	public void playerMovement(int n) {
		if (n == 0) {
			campo[r][c] = 'O';
			r += 2;
			campo[r][c] = 'R';
		}
		if (n == 1) {
			campo[r][c] = 'O';
			r -= 2;
			campo[r][c] = 'R';
		}
		if (n == 3) {
			campo[r][c] = 'O';
			c -= 2;
			campo[r][c] = 'R';
		}
		if (n == 2) {
			campo[r][c] = 'O';
			c += 2;
			campo[r][c] = 'R';
		}
	}

	public void updateAvailableMoves(int r, int c) {
		if (r != 16 && campo[r + 1][c] == '|')
			mosse[0] = true;
		else
			mosse[0] = false;
		if (r != 0 && campo[r - 1][c] == '|')
			mosse[1] = true;
		else
			mosse[1] = false;
		if (c != 0 && campo[r][c - 1] == '-')
			mosse[3] = true;
		else
			mosse[3] = false;
		if (c != 16 && campo[r][c + 1] == '-')
			mosse[2] = true;
		else
			mosse[2] = false;
	}

	public int chooseMovement() {
		if (mosse[0] == true)
			return 0;
		if (mosse[2] == true)
			return 2;
		if (mosse[3] == true)
			return 3;
		return 1;
	}

	public int chooseMovementBFS(int r, int c) {
		Node startNode = new Node(r, c);
		LinkedList<Node> visitedNode = new LinkedList<>();

		LinkedList<Node> queue = new LinkedList<>();
		queue.add(startNode);
		startNode.setParent(null);
		while (!queue.isEmpty()) {
			Node node = (Node) queue.removeFirst();
			updateAvailableMoves(node.getR(), node.getR());
			node.addNeighbor(mosse);
			if (node.getR() == 16)
				return constructPath(node);// ritorno il penultimo elemento del constructpath
			else {
				visitedNode.add(node);
				Iterator<Node> i = node.neighbors.iterator();
				while (i.hasNext()) {
					Node neighborNode = (Node) i.next();
					for (Node t : visitedNode) {
						if (t.getC() != neighborNode.getC() || t.getR() != neighborNode.getR()) {
							neighborNode.setParent(node);
							queue.addLast(neighborNode);
						}
					}
				}
			}
		}
		return 0;

	}

	public int constructPath(Node node) {
		LinkedList<Node> path = new LinkedList<>();
		while (node.getParent() != null) {
			path.addFirst(node);
			node = node.getParent();
		}
		if (path.get(path.size() - 2).getR() < path.get(path.size() - 1).getR())
			return 0;
		if (path.get(path.size() - 2).getR() > path.get(path.size() - 1).getR())
			return 1;
		if (path.get(path.size() - 2).getC() < path.get(path.size() - 1).getC())
			return 2;
		if (path.get(path.size() - 2).getC() > path.get(path.size() - 1).getC())
			return 3;
		return 0;
	}

	public int convertIndexWall(int n) {
		if (n % 16 < 8) {// verticale
			n = (7 - (n / 16)) * 16 + (7 - (n % 16));
			return n;
		} // orrizzontale
		n = (7 - ((n - 8) / 16)) * 16 + (7 - ((n - 8) % 16)) + 8;
		return n;
	}

	public void printField() {
		for (int i = 0; i < 17; i++) {
			for (int j = 0; j < 17; j++)
				System.out.print(campo[i][j]);
			System.out.println();
		}
		for (int i = 0; i < 4; i++)
			System.out.println(mosse[i] + " ");
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

	public boolean getFirst() {
		return this.first;
	}

}
