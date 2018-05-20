package bataille.passifs;
import exception.*;
import java.util.ArrayList;

public class Fleet {
	private ArrayList<Ship> shipList;
	
	public Fleet (ArrayList<Ship> tabShip) {
		this.shipList = tabShip;
	}
	public Fleet () {
		this.shipList=new ArrayList<Ship>();
	}
	
	public void addShip(Ship s) throws OverlapException {
		if(this.overlapFleet(s)){
			throw new OverlapException();
		}	
		this.shipList.add(s);
	}
	
	//Used to count the amount of ships initialized for the player
	public int countShips(){
		return 0;
	}
	//Used to count the amount of ships that are still alive
	public int shipsRemaining(){
		int batRemain = 0;
		for (Ship ship : this.shipList) {
			if (!ship.isSunk()) {
				batRemain++;
			}
		}
		return batRemain;
	}
	
	public ArrayList<Ship> getShipList() {
		return shipList;
	}
	
	//verifier chevauchement
	
	public boolean overlapFleet(Ship ship){
		for(Ship shipFleet : this.shipList){
			if(overlapShip(shipFleet, ship)){
				return true;
			}
		}
		return false;
	}
	
	private boolean overlapShip(Ship ship, Ship ship2) {
		for (Coordinates coord : ship2.getShipCase().keySet()){
			for(Coordinates c : ship.getShipCase().keySet()){
				if (coord.equals(c)){
					return true;
				}
			}
		}
		return false;
	}
	
	/* Return le résultat du tir et met à jour si un bateau est touché
		A l'eau -> 0
		Touché -> 1
		Touché Coulé -> 2
		Déjà touché -> 3
		Coulé -> 4
		*/
	
	public int shot (Coordinates coord) {
		for(Ship ship : this.shipList) {
			if (ship.isHit(coord)) {
				// si le bateau est concerné par le tir on renvoie le resultat du tir.
				return ship.hitShip(coord);
			}
		}
		
		return 0;
	}
	@Override
	public String toString() {
		return "Fleet [shipList=" + shipList + "]";
	}
	public int valueCell(Coordinates c) {
		// Return the value of a cell
		// touched -> 1
		// not touched -> 0
		// -1 none boat
		int res = -1;
		for (Ship s : this.shipList) {
			if (s.valueCoordinates(c) != -1) {
				res = s.valueCoordinates(c);
			}
		}
		return res;
	}
	
	
	
}