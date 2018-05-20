package bataille.actifs;

import java.util.HashMap;
import java.util.Scanner;

import bataille.passifs.Coordinates;
import bataille.passifs.Fleet;
import bataille.passifs.FleetType;
import bataille.passifs.Ship;
import exception.OverlapException;


public class Player  implements Playerable {
	public static final int BOARD_SIZE = 10;
	static Scanner sc = new Scanner(System.in);
	
	/* Keep Shot in memory
	code : Miss : 0 ; hit : 1 ; sunk : 2 */   
	private HashMap<Coordinates, Integer> opponentBoard;
	
	private Fleet fleet;
	private String name;
	
	
	public Player() {
		super();
		String name = askName ("Pick player's name.");
		System.out.println("Init of " + name);
		Fleet fleet = initFleet();
		this.fleet = fleet;
		this.name = name;
		opponentBoard = new HashMap<Coordinates, Integer>();
	}
	
	public void reInit() {
		// Reninit a player, keep the same name
		Fleet fleet = initFleet();
		this.fleet = fleet;
		opponentBoard = new HashMap<Coordinates, Integer>();
	}
	
	
	public int shot(Coordinates pos){
		// Set the shot in personnal ship
		return this.fleet.shot(pos);
	};
	
	
	
	public  String OpponentBoardString () {
		// Print the matrice of the opponent to see where shot
		String res = " ";
		
		for(int j=0;j<=Player.BOARD_SIZE;j++){

			for(char i='A';i<'A'+Player.BOARD_SIZE;i++){
				
				// First line, letters
				if (j == 0) {
					res += "  "+ i;
				} else {
					Coordinates c = new Coordinates(i,j);
					
					res+= this.valueCell(c);	
				}
			}
			
			if (j != Player.BOARD_SIZE ) {
				if (j+1 < 10) {
					res +="\n" + Integer.valueOf(j+1) + " ";
				} else {
					res += "\n" + Integer.valueOf(j+1);
				}
			} else {
				res +="\n" ;
			}
		}
		return res;
	}
	
	private  String valueCell(Coordinates c) {
		// Get the value of the cell (water, touched ship, sunked chip)
		String res="";
		
			if(opponentBoard.containsKey(c)) {
				if(opponentBoard.get(c)==0) {
					res+=" / ";
				}
				if(opponentBoard.get(c)==1) {
					res+=" x ";
				}
				if(opponentBoard.get(c)==2) {
					res+=" * ";
				}
			}
			else{
				res+=" ~ ";
		}
			
		return res;	
	}

	// Update opponent board to memories shooted coordinates
	public void setResShot(Coordinates coordShot, int resShot) {
		this.opponentBoard.put(coordShot, resShot);
	}

	@Override
	public Coordinates getShot() {
		return new Coordinates(askCoordinates("Where do you want to shot ?"));
	};
	
	private static Fleet initFleet() {
		Fleet fleet = new Fleet();
		
		// All ship creation
		for(FleetType fleetType: FleetType.values() ) {
			for (int i = 0; i < fleetType.getAmount(); i++) {
				boolean insert = false;
				while (!insert) {
					try {
						fleet.addShip(createShip(fleetType));
						insert = true;
					} catch (OverlapException e) {
						System.out.println(e);
					}
				}	
			}
		}
		return fleet;
	}

	
	// Create a certain ship, depend on the argument type
	private static Ship createShip(FleetType fleetType) {
		
		String coordStart = askCoordinates("Coordonnée de départ du bateau : "  
				+ fleetType.toString() 
				+ " de taille " + fleetType.getSize()
				+ " ? ex : \"A1\" ");
		Coordinates startingPoint = new Coordinates(coordStart);
		String coordEnd = askCoordinates("Et ses coordonnées de fin ? Coordonnées possibles : "+startingPoint.computeCoordinates(fleetType.getSize()));
		
		Ship ship = null;
		while (ship == null) {
			// ask question until valid
			try {
				ship = new Ship(fleetType.toString(), coordStart, coordEnd);
			} catch(Exception e) {
				System.out.println(e.toString());
				coordStart = askCoordinates("Coordonnée de départ du bateau : "  
						+ fleetType.toString() 
						+ " de taille " + fleetType.getSize()
						+ " ? ex : \"A1\" ");
				coordEnd = askCoordinates("Et ses coordonnées de fin ex : "+startingPoint.computeCoordinates(fleetType.getSize()));
			}
		}
		
		return ship;
		
	}
	
	public String getBoardString() {
		// Print the matrice of the opponent to see where shot
				String res = " ";
				
				for(int j=0;j<=Player.BOARD_SIZE;j++){

					for(char i='A';i<'A'+Player.BOARD_SIZE;i++){
						
						// First line, letters
						if (j == 0) {
							res += "  "+ i;
						} else {
							Coordinates c = new Coordinates(i,j);
							
							res+= valueCellBoard(c);	
						}
					}
					
					if (j != Player.BOARD_SIZE ) {
						if (j+1 < 10) {
							res +="\n" + Integer.valueOf(j+1) + " ";
						} else {
							res += "\n" + Integer.valueOf(j+1);
						}
					} else {
						res +="\n" ;
					}
				}
				return res;
	}
	
	public String valueCellBoard(Coordinates c) {
		// Get the value of the cell (water, touched ship, sunked chip)
		
		switch (this.fleet.valueCell (c) ) {
		case -1 :
			return " ~ ";
		
		case 0 :
			return " O ";
			
		case 1 :
			return " X ";
			
		default:
			return " chaud ";
			
		}
		
	}
	
	// Ask coordinates with custom question
	public static String askCoordinates(String message) {
		System.out.println(message);
		String coord = sc.nextLine();
		
		while (!(Coordinates.isValid(coord))) {
			System.out.println("Coordinates aren't valid.");
			System.out.println(message);
			coord = sc.nextLine();
		}
		return coord;
	}
	
	
	public void printStatePlayer () {
		System.out.println("Enemy game board: \n" + 
				this.OpponentBoardString() + "\n");
		
		System.out.println("Your game board : \n" + 
				this.getBoardString());
	}
	
	private static String askName (String question) {
		System.out.println(question);
		return sc.nextLine();
	}
	
	public Fleet getFleet(){
		return this.fleet;
	}
	
	@Override
	public String toString() {
		return "Player [fleet=" + fleet + "]";
	}
	
	public String getName() {
		return name;
	}
	
}
