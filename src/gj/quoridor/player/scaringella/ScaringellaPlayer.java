package gj.quoridor.player.scaringella;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import gj.quoridor.player.Player;

public class ScaringellaPlayer implements Player {

	private Node[][] board = new Node[9][9];

	private Node me;
	private Node enemy;
	private boolean red;

	private int[][] allDirection = new int[][] { { 1, -1, 1, -1 }, { -1, 1, -1, 1 } }; // il primo array è il rosso

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

		int[] mossaTemp = null;
		do {
			mossaTemp = randomMove();
		} while (!checkLegalMove(mossaTemp));

		if (mossaTemp[0] == 0) {
			me = movePlayer(me, mossaTemp[1], true);
		} else if (mossaTemp[0] == 1) {
			placeWall(mossaTemp[1]);
			availableWall--;
		}
		return mossaTemp;
	}

	private int[] randomMove() {

		int[] move = new int[2];
		move[0] = ThreadLocalRandom.current().nextInt(2);

		if (move[0] == 0) {
			move[1] = ThreadLocalRandom.current().nextInt(4);
		} else {
			move[1] = ThreadLocalRandom.current().nextInt(128);
		}

		return move;
	}

	private boolean checkLegalMove(int[] move) {

		if (move[0] == 0)
			return checkLegalMovement(me, move[1]);
		if (move[0] == 1) {
			return checkLegalWallPlacement(move[1]);
		}
		return false;
	}

	private boolean checkLegalMovement(Node start, int direction) {
		try {
			return start.isNeighbor(movePlayer(start, direction, true));
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean checkLegalWallPlacement(int i) {
		if (availableWall == 0) {
			return false;
		}
		if (walls.contains(i)) {
			return false;
		}
		fracture(i);
		if ((Path.shortPath(enemy, (red) ? 0 : 8) == null) || (Path.shortPath(me, (red) ? 8 : 0) == null)) {
			patch(i);
			return false;
		}
		patch(i);
		return true;
	
	}

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
		int[] directions = (red ^ myMovement) ? allDirection[1] : allDirection[0];
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

}
