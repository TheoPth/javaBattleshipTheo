package fr.battleship;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import bataille.actifs.Game;
import bataille.actifs.IA;
import bataille.passifs.Coordinates;

public class TestIA {
	private static final int NB_TEST = 100;
	public static void main(String[] args) {
		PrintWriter pw;
		int sc1 = play(new Game (new IA(1, "1"), new IA(2, "2")));
		int sc2 = play(new Game (new IA(1, "1"), new IA(3, "2")));
		int sc3 = play(new Game (new IA(2, "1"), new IA(3, "2")));
		try {
			pw = new PrintWriter(new File("ai_proof.csv"));
			StringBuilder sb = new StringBuilder();
	        sb.append("AI Name");
	        sb.append(',');
	        sb.append("Score");
	        sb.append(',');
	        sb.append("AI Name2");
	        sb.append(',');
	        sb.append("Score2");
	        sb.append('\n');

	        sb.append("AI Level Beginner");
	        sb.append(',');
	        sb.append(sc1);
	        sb.append(',');
	        sb.append("Level Medium");
	        sb.append(',');
	        sb.append(NB_TEST - sc1);
	        sb.append('\n');
	        
	        sb.append("AI Level Beginner");
	        sb.append(',');
	        sb.append(sc2);
	        sb.append(',');
	        sb.append("Level Hard");
	        sb.append(',');
	        sb.append(NB_TEST - sc2);
	        sb.append('\n');
	        
	        sb.append("AI Level Medium");
	        sb.append(',');
	        sb.append(sc3);
	        sb.append(',');
	        sb.append("Level Hard");
	        sb.append(',');
	        sb.append(NB_TEST - sc3);
	        sb.append('\n');

	        pw.write(sb.toString());
	        pw.close();
	        System.out.println("done!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

	}

	private static int play(Game game) {
		int vic = 0;
		for (int i = 0; i < NB_TEST; i++) {
			while (!game.gameIsOver()){
				
				Coordinates coordShot = game.whosTurn().getShot();
				
				int resShot = game.getOpponent().shot(coordShot);
				game.whosTurn().setResShot(coordShot, resShot);	
				game.nextTurn();
			}
			
			if (game.getWinner().getName().equals("1")) {
				vic++;
			}
			
			game.whosTurn().reInit();
			game.getOpponent().reInit();
			game.changeOrder();
		}
		return vic;
	}
	
}
