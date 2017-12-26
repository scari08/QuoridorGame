package gj.quoridor.player.scaringella;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import gj.quoridor.player.Player;

public class ScaringellaPlayer implements Player {
	// Field game;

	private Node[][] board = new Node[9][9];

	private Node me;
	private Node enemy;
	private boolean red;

	private int[][] allDirection = new int[][] { { 1, -1, 1, -1 }, { -1, 1, -1, 1 } }; // il primo array � il rosso

	private int availableWall;
	private Set<Integer> walls;

	// private List<Move> moves;
	// private List<Move> availableMoves;

	// public void updateAvailableMoves() {
	//
	//
	// //da implementare il controllo che non faccia spostamenti sbagliati
	// //a dire il vero ora basta che me segua i neighbor
	//
	// if(me.getNeighbors().size()==4) {
	// availableMoves=new ArrayList<>();
	// availableMoves.add(new Move(1));
	// availableMoves.add(new Move(-1));
	// availableMoves.add(new Move(2));
	// availableMoves.add(new Move(-2));
	// return;
	// }
	// for (Node i : me.getNeighbors()) {
	// if(i.getR()==me.getR()) {//significa che pu� spostarsi destra/sinistra
	// //se sono rosso e il mio vicino ha +1 c allora posso andare a sinistra
	// availableMoves.add(new Move((i.getC()-me.getC())*2));//simple calcus credo
	// che sta cosa funzioni indipendentemente da rosso blu boh magari funziona
	//
	// }
	// if(i.getC()==me.getC()) {
	// availableMoves.add(new Move((i.getR()-me.getR())));
	// }
	// }
	//
	// }

	public void enemyWall(int ind) {
		// se nemico piazza muro devo togliere alcuni neighbor e togliere alcune
		// posizioni di muri
		walls.add(ind);
		walls.addAll(Wall.incompatible(ind));
		fracture(ind);
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

	public ScaringellaPlayer() {
		// TODO Auto-generated constructor stub
	}

	// all inizio costruisci il campo di gioco con il metodo start

	@Override
	public int[] move() {

		// array di 2 valori
		// il primo valore se 0 muove in avanti 0
		// indietro 1 sinistra 2 destra 3 (relativo)
		// se primo valore=1 metti un muro nell indice= secondo valore max 127
		// int n[] = new int[2];
		//
		// n[0] = 0;
		// n[1] = game.chooseMovement();
		//
		// game.playerMovement(n[1]);
		// game.updateAvailableMoves(game.getR(), game.getC());
		// game.printField();
		int[] mossaTemp = null;
		do {
			mossaTemp = randomMove();
		} while (!checkLegalMove(mossaTemp));
		
		if(mossaTemp[0]==0){
			me = movePlayer(me, mossaTemp[1], true);
		}
		else if(mossaTemp[0]==1){
			enemyWall(mossaTemp[1]);
			availableWall--;
		}

		/////////attenzione!!!!
		//me = movePlayer(me, mossaTemp[1], true);
		

		return mossaTemp;
	}

	private boolean checkLegalMove(int[] move) {

		if (move[0] == 0)
			return checkLegalMovement(me, move[1]);
		if (move[0] == 1)
			return checkLegalWallPlacement(move[1]);
		return false;
	}

	private boolean checkLegalMovement(Node start, int direction) {

		try {
			return start.isNeighbor(movePlayer(start, direction, true));
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace(); // TEMPORARY
			return false;
		}

	}

	private boolean checkLegalWallPlacement(int i) {
		if (availableWall == 0) {
			return false;
		} else if (walls.contains(i)) {
			return false;//due errori uno � l'availablewalls e l'altro � che prima devo piazzare il mur e poi cercare il path
		} else if ((Path.shortPath(enemy, (red) ? 0 : 8) == null) || (Path.shortPath(me, (red) ? 8 : 0) == null)) {
//			ArrayList<Node> cose = (ArrayList<Node>) Path.shortPath(enemy, (red) ? 0 : 8);
//
//			System.out.println("R=" + cose.get(0).getR() + " C=" + cose.get(0).getC());
//			System.out.println("R=" + cose.get(1).getR() + " C=" + cose.get(1).getC());
//			
			return false;
		}
		return true;
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

	@Override
	public void start(boolean arg0) {

		initBoard();

		red = arg0;

		me = (red) ? board[0][4] : board[8][4];
		enemy = (red) ? board[8][4] : board[0][4];

		availableWall = 10;
		walls = new HashSet<>();
		// primo = rosso sopra
		// game = new Field();
		// game.setFirst(arg0);
		// game.printField();

	}

	@Override
	public void tellMove(int[] arg0) {

		if (arg0[0] == 0) {
			enemy = movePlayer(enemy, arg0[1], false);
		} else {
			enemyWall(arg0[1]);
		}
		// // ci dice che mossa ha fatto player nemico con stessi modi
		// if (arg0[0] == 1) {
		// if (game.getFirst()) {
		// game.placedWall(arg0[1]);
		// game.updateAvailableMoves(game.getR(), game.getC());
		// // game.printField();
		// return;
		// }
		// game.placedWall(game.convertIndexWall(arg0[1]));
		// game.updateAvailableMoves(game.getR(), game.getC());
		// // game.printField();
		// }
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

}
