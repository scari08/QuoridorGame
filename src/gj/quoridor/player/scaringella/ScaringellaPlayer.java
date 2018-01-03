package gj.quoridor.player.scaringella;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gj.quoridor.player.Player;

public class ScaringellaPlayer implements Player {

	private Node[][] board = new Node[9][9];

	private Node me;
	private Node enemy;
	private boolean red;

	private int[][] allDirections = new int[][] { { 1, -1, 1, -1 }, { -1, 1, -1, 1 } }; // il primo array è il rosso

	private int availableWall;
	private Set<Integer> walls;

	@Override
	public void start(boolean arg0) {
		initBoard();
		red = arg0;
		me = (red) ? board[0][4] : board[8][4];
		enemy = (red) ? board[8][4] : board[0][4];

		availableWall = 10;
		walls = new HashSet<>();
	}

	public void initBoard() {

		for (int i = 0; i < 9; i++) {
			for (int k = 0; k < 9; k++) {
				board[i][k] = new Node(i, k);
			}
		}

		for (int i = 0; i < 9; i++) {
			for (int k = 0; k < 9; k++) {

				if (i != 0) {
					board[i][k].addNeighbor(board[i - 1][k]);
				}

				if (i != 8) {
					board[i][k].addNeighbor(board[i + 1][k]);
				}

				if (k != 0) {
					board[i][k].addNeighbor(board[i][k - 1]);
				}

				if (k != 8) {
					board[i][k].addNeighbor(board[i][k + 1]);
				}
			}
		}

	}

	@Override
	public int[] move() {

		int[] move = new int[2];

		List<Node> myRemaining = Path.shortPath(me, (red) ? 8 : 0);
		List<Node> enemyRemaining = Path.shortPath(enemy, (red) ? 0 : 8);
		if (availableWall > 0 && myRemaining.size() > enemyRemaining.size()) {
			move[0] = 1;
			move[1] = minMaxLengthen(enemyRemaining.size() - myRemaining.size());
		} else {
			move[0] = 0;
			move[1] = shortMovement(myRemaining);
		}

		if (move[1] == -1) {
			move[0] = 0;
			move[1] = shortMovement(myRemaining);
		}

		// if (!checkLegalMove(move)) {
		// move[0] = 0;
		// move[1] = shortMovement(myRemaining);
		// } //////// ATTENZZZZZIONEE

		if (move[0] == 0) {
			me = movePlayer(me, move[1], true);
		} else if (move[0] == 1) {
			placeWall(move[1]);
			availableWall--;
		}
		return move;
	}

	private int minMaxLengthen(int delta) {
		int bestInd = -1;
		int best = delta;
		for (int i = 0; i < 128; i++) {
			if (!walls.contains(i)) {
				fracture(i);
				List<Node> myPath = Path.shortPath(me, (red) ? 8 : 0);
				List<Node> enemyPath = Path.shortPath(enemy, (red) ? 0 : 8);
				if (myPath != null && enemyPath != null) {
					int temp1 = myPath.size();
					int temp2 = enemyPath.size();
					if (temp2 - temp1 > best) {
						best = temp2 - temp1;
						bestInd = i;
					}
				}
			}
			patch(i);
		}
		return bestInd;
	}

	// private boolean checkLegalMove(int[] move) {
	// boolean result = false;
	// if (move[0] == 0)
	// result = checkLegalMovement(me, move[1]);
	// if (move[0] == 1) {
	// result = checkLegalWallPlacement(move[1]);
	// }
	// return result;
	// }
	//
	// private boolean checkLegalMovement(Node start, int direction) {
	// try {
	// return start.isNeighbor(movePlayer(start, direction, true));
	// } catch (ArrayIndexOutOfBoundsException e) {
	// return false;
	// }
	// }
	//
	// private boolean checkLegalWallPlacement(int i) {
	// if (availableWall == 0) {
	// return false;
	// }
	// if (walls.contains(i)) {
	// return false;
	// }
	// fracture(i);
	// if ((Path.shortPath(enemy, (red) ? 0 : 8) == null) || (Path.shortPath(me,
	// (red) ? 8 : 0) == null)) {
	// patch(i);
	// return false;
	// }
	// patch(i);
	// return true;
	// }

	public void placeWall(int ind) {
		walls.add(ind);
		walls.addAll(Wall.incompatible(ind));
		fracture(ind);
	}

	@Override
	public void tellMove(int[] arg0) {

		if (arg0[0] == 0) {
			enemy = movePlayer(enemy, arg0[1], false);
		} else {
			placeWall(arg0[1]);
		}
	}

	private Node movePlayer(Node start, int direction, boolean myMovement) {
		int[] directions = (red ^ myMovement) ? allDirections[1] : allDirections[0];
		int newR = start.getR(), newC = start.getC();
		if (direction < 2) {
			newR += directions[direction];
		} else {
			newC += directions[direction];
		}
		return board[newR][newC];
	}

	private void fracture(int wall) {
		Node[][] nodes = Wall.fracture(board, wall);
		for (int i = 0; i < 2; i++) {
			nodes[i][0].removeNeighbor(nodes[i][1]);
			nodes[i][1].removeNeighbor(nodes[i][0]);
		}
	}

	private void patch(int wall) {
		Node[][] nodes = Wall.fracture(board, wall);
		for (int i = 0; i < 2; i++) {
			nodes[i][0].addNeighbor(nodes[i][1]);
			nodes[i][1].addNeighbor(nodes[i][0]);
		}
	}

	private int shortMovement(List<Node> path) {
		return directMovement(path.get(path.size() - 1), path.get(path.size() - 2));
	}

	private int directMovement(Node start, Node goal) {
		int[] directions = (red) ? allDirections[0] : allDirections[1];
		for (int i = 0; i < 4; i++) {
			if (i < 2) {
				int temp = start.getR() + directions[i];
				if (temp == goal.getR())
					return i;
			} else {
				int temp = start.getC() + directions[i];
				if (temp == goal.getC())
					return i;
			}
		}
		return 0;
	}

}
