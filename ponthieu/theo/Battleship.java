package ponthieu.theo;


import java.util.HashMap;
import java.util.Scanner;

import bataille.actifs.Game;
import bataille.actifs.IA;
import bataille.actifs.Player;
import bataille.passifs.Coordinates;


public class Battleship  {
	
	static Scanner sc = new Scanner(System.in);
	static HashMap<Coordinates, Integer> opponentBoard;

	public static void main(String[] args) {
		
		System.out.println("Welcome to my java battle ship.");
		Game game = createGame();
		boolean continuer = true;
		
		
		while (continuer){ 
			System.out.println("It's the turn of " + game.whosTurn().getName());
			
			// Print boat state if it's a real player
			if (game.whosTurn() instanceof Player) {
				((Player)game.whosTurn()).printStatePlayer();
			} 
			
			Coordinates coordShot = game.whosTurn().getShot();
			
			
			System.out.println(game.whosTurn().getName() + " shot at : " + coordShot);
			
						
			
			int resShot = game.getOpponent().shot(coordShot);
			game.whosTurn().setResShot(coordShot, resShot);
			promptShot(resShot);	
			game.nextTurn();
			
			// If game end ask replay 
			if (game.gameIsOver()) {
				System.out.println(game.turn);
				System.out.println("The player " + game.getWinner().getName() + " won !" );
				
				continuer = askBool("Replay ? O/N");
				
				if (continuer) {
					// If replay, reiinit playeur and change order
					game.whosTurn().reInit();
					game.getOpponent().reInit();
					game.changeOrder();
				}
				
			}
		}
		
		System.out.println("Good bye.");
	}
	
	
	
	private static Game createGame() {
		switch (askTypeGame()) {
			case 1 :
				return new Game(new Player(), new Player());
			case 2 :
				int lvl = askLvl ("Wich level for the IA ? 1, 2 or 3");
				IA ia = new IA(lvl, "IA");
				return new Game(new Player(), ia);
			default :
				return null;
		}
	}
	

	private static int askLvl(String question) {
		int lvl = 0;
		do {
			System.out.println(question);
			try {
				lvl = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.out.println("Level unavailable. Enter 1, 2 or 3 please.");
				lvl = 0;
			} 
			
		} while (lvl < 1 || lvl > 3);
		return lvl;
	}
	
	
	// Permet d'afficher le résultat d'un tir
	private static void promptShot (int codePrompt) {
		switch (codePrompt) {
		case 0 :
			System.out.println("Miss !");
			break;
		case 1 :
			System.out.println("Touched !");
			break;
		case 2 :
			System.out.println("Sunked!");
			break;
		case 3 :
			System.out.println("Already touhed !");
			break;
		}	
	}
	
	private static int askTypeGame () {
		int age = 0;
		do {
			System.out.println("Which type of player do you want ? 1 -> 2 players, 2 -> 1 player and 1 IA.");
			
			try {
				age = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.out.println("Invalid, please enter : 1 or 2.");
			}
		} while (age < 1 || age > 2);
		
		return age;
	}

	
	private static boolean askBool(String question) {
		String res = "";
		
		do {
			System.out.println(question);
			res = sc.nextLine();
			
			
		} while(!res.toUpperCase().equals("O") && !res.toUpperCase().equals("N"));
		
		return res.toUpperCase().equals("O");
	}
	
}
