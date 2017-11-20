package gj.quoridor.player.scaringella;

public class Move {//	muovi avanti/indietro/sinistra/destra/piazzamuroindice1/2/3/4..
	//dovrebbe avere due sottoclassi una per movimento giocatore l altra per piazzare muro
	//per ora faccio solo movimento giocatore
	
	private int direction;//può essere 1/-1 o 2/-2
	
	
	public Move(int dir) {
		this.direction=dir;
		
	}
	
	public Node updatePlayerMovement(Node[][] board, Node player){
		//quando sceglierò quale move fare gli faccio fare updatePLayerMOvement passandogli la posizione del player e stato della board
		
		if(direction%2==1)player.setR(player.getR()+direction);	//mi sa che sto sbagliando perchè sto aggiornando la r di board[][] visto che me=board[][]
		if(direction%2==0)player.setC(player.getC()+direction/2); //se fossero stati puntatori sarebbe stato molto più intuitivo
		
		
		return player;//ritorno il nuovo stato del player
		
	}

}
