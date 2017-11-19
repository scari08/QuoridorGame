package gj.quoridor.player.scaringella;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gj.quoridor.player.Player;

public class ScaringellaPlayer implements Player {
	// Field game;

	private Node[][] board = new Node[9][9];

	private Node me;
	private Node enemy;
	private boolean red;

	private List<Move> moves;
	private List<Move> availableMoves;
	
	public void updateAvailableMoves() {
		//da implementare il controllo che non faccia spostamenti sbagliati
		
		availableMoves=new ArrayList<>();
		availableMoves.add(new Move(1));
		availableMoves.add(new Move(-1));
		availableMoves.add(new Move(2));
		availableMoves.add(new Move(-2));
	}
	
	public void scegliRandom(/*in teoria gli dovrei passare le availablemoves ma per ora siamo nella stessa classe*/) {
		Random random=new Random();
		availableMoves.get(random.nextInt(4)).updatePlayerMovement(board, me);
	}
	
	private int aviableWall;
	private Set<Integer> walls;

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
		return null;
	}

	@Override
	public void start(boolean arg0) {

		initBoard();

		red = arg0;

		me = (red) ? board[0][4] : board[8][4];
		enemy = (red) ? board[8][4] : board[0][4];

		aviableWall = 10;
		walls = new HashSet<>();
		// primo=rosso sopra
		// game = new Field();
		// game.setFirst(arg0);
		// game.printField();

	}

	@Override
	public void tellMove(int[] arg0) {

		if (arg0[0] == 0) {

		} else {
			walls.add(arg0[1]);
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

}
