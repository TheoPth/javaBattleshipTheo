package bataille.actifs;

public class Game {
	public int turn;
	private Playerable j1;
	private Playerable j2;

	public Game(Playerable j1, Playerable j2) {
		this.turn = 0;
		this.j1 = j1;
		this.j2=j2;
	}

	
	public int playerTurn() {
		//0 pour Tour J1 et 1 pour Tour J2
		return turn % 2;
	}
	
	public Playerable whosTurn() {
		if(playerTurn()==0) {
			return j1;
		}
		else {
			return j2;
		}
	}

public Playerable getOpponent() {
	if(playerTurn()==0) {
		return j2;
	}
	else {
		return j1;
	}
}

public void nextTurn(){
	this.turn++;
}

public boolean gameIsOver(){
	return getOpponent().getFleet().shipsRemaining() == 0 
			|| whosTurn().getFleet().shipsRemaining() == 0;
}

public Playerable getWinner() {
	if (getOpponent().getFleet().shipsRemaining() == 0) {
		return whosTurn();
	} else if(whosTurn().getFleet().shipsRemaining() == 0) {
		return getOpponent();
	}
	return null;
	
}


public void changeOrder() {
	Playerable jtmp = this.j1;
	j1 = j2;
	j2 = jtmp;
	turn = 0;
}


}