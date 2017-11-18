package gj.quoridor.player.scaringella;

import gj.quoridor.player.Player;

public class ScaringellaPlayer implements Player {
	Field game;

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
		int n[] = new int[2];

		n[0] = 0;
		n[1] = game.chooseMovement();

		game.playerMovement(n[1]);
		game.updateAvailableMoves(game.getR(), game.getC());
		// game.printField();
		return n;
	}

	@Override
	public void start(boolean arg0) {
		// primo=rosso sopra
		game = new Field();
		game.setFirst(arg0);
		// game.printField();

	}

	@Override
	public void tellMove(int[] arg0) {
		// ci dice che mossa ha fatto player nemico con stessi modi
		if (arg0[0] == 1) {
			if (game.getFirst()) {
				game.placedWall(arg0[1]);
				game.updateAvailableMoves(game.getR(), game.getC());
				// game.printField();
				return;
			}
			game.placedWall(game.convertIndexWall(arg0[1]));
			game.updateAvailableMoves(game.getR(), game.getC());
			// game.printField();
		}
	}

}
