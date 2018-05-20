package bataille.passifs;
import exception.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class Ship {
	private String shipType;
	//String is a case from the board and the Boolean indicates if the corresponding case has been hit or not
	//true if hit, false if not

	Map<Coordinates,Boolean> shipCase;
	
	
	//CONSTRUCTOR FOR A GENERIC SHIP
	// Both coordinates are valid due to previous verification on the Main
	public Ship(String shipType, String startCoordinates, String endCoordinates) throws BadSizeException, OrientationException {
		Coordinates startCoord = new Coordinates (startCoordinates);
		Coordinates endCoord = new Coordinates (endCoordinates);
		this.shipType = shipType;
		this.shipCase = new LinkedHashMap<Coordinates, Boolean>();
		
		//Filling the Map with the cases of the Ship according to the inputed cells and ship size
		this.shipCase.put(startCoord,false);
		if(startCoord.getHor()<endCoord.getHor() || startCoord.getVert()<endCoord.getVert()) {
		if (startCoord.isVertical(endCoord)) {
			for (int i=1;i<this.getSize();i++) {
				int val = startCoord.getHor()+i;
				this.shipCase.put(new Coordinates(startCoord.getVert(), val), false);
			}	
		}
		else if (startCoord.isHorizontal(endCoord)) {
			for (int i=1;i<this.getSize();i++) {
				char coord = startCoord.incrementChar(startCoord.getVert(), i);
				this.shipCase.put(new Coordinates(coord +""+ startCoord.getHor()),false);
			}	
		}	
		}
		else {
			if (startCoord.isVertical(endCoord)) {
				for (int i=1;i<this.getSize();i++) {
					int val = startCoord.getHor()-i;
					this.shipCase.put(new Coordinates(startCoord.getVert(), val), false);
				}	
			}
			else if (startCoord.isHorizontal(endCoord)) {
				for (int i=1;i<this.getSize();i++) {
					char coord = startCoord.decrementChar(startCoord.getVert(), i);
					this.shipCase.put(new Coordinates(coord +""+ startCoord.getHor()),false);
				}	
			}
		}
		//TESTS
		if(!orientationValid(startCoord, endCoord)){
			throw new OrientationException();
		}
		
		if(!sizeValid(startCoord, endCoord)){
			throw new BadSizeException();
		}		
	}
	//END OF CONSTRUCTOR
	
	public boolean orientationValid(Coordinates startCoord, Coordinates endCoord){
				return startCoord.isHorizontal(endCoord) || startCoord.isVertical(endCoord);
	}
	
	public boolean sizeValid(Coordinates startCoord, Coordinates endCoord) {
		
		return Math.abs(this.checkSize(startCoord, endCoord)) == getSize();
	}
	
	public int checkSize(Coordinates startCoord, Coordinates endCoord) {
		if(startCoord.getHor()<endCoord.getHor() || startCoord.getVert()<endCoord.getVert()) {
			if (startCoord.isVertical(endCoord)) {
				int s = endCoord.getHor();
				int e = startCoord.getHor();	
				return s - e +1;
			}
			else if (startCoord.isHorizontal(endCoord)){
				int s = startCoord.getVert();
				int e = endCoord.getVert();
				return e - s +1; 
			}
		}
		else {
			if (startCoord.isVertical(endCoord)) {
				int s = endCoord.getHor();
				int e = startCoord.getHor();	
				return s - e -1;
			}
			else if (startCoord.isHorizontal(endCoord)){
				int s = startCoord.getVert();
				int e = endCoord.getVert();
				return e - s-1; 
			}
		}
			return 0;
	}
	//IMPLEMENTATION OF METHODS THAT ARE COMMON TO ALL SHIPS	

	private int getHits(){
		int hits=0;

		for (Coordinates key : this.shipCase.keySet()){

			if(shipCase.get(key)){
				hits++;
			}
		}
		return hits;
	}

	public boolean isSunk() {
		return this.getSize() == this.getHits();
	}
	
	//INDICATES IF THE SHIP IS IN VERTICAL POSITION. IF NOT WE KNOW IT IS HORIZONTAL AS THE SHIP AS BEEN CONSTRUCTED AND THIS CONDITION IS TESTED ON THE CONSTRUCTOR
	public boolean isVertical(){
		char first = this.getFirstCoordinate().getVert();
		char last = this.getLastCoordinate().getVert();
		return first == last;

	}
	
	private int getSize(){
		return FleetType.valueOf(shipType).getSize();
	}
	
	public Coordinates getFirstCoordinate(){
		return this.shipCase.entrySet().iterator().next().getKey();
	}
	
	public Coordinates getLastCoordinate(){
		Coordinates out = new Coordinates();
		for (Coordinates key : this.shipCase.keySet()) {
			out = key;
		  }
		  return out;
	}
	
	
	// SHOOT METHODS

	public Map<Coordinates, Boolean> getShipCase() {
		return shipCase;
	}

	public boolean isHit(Coordinates missileCoord) {
		return this.shipCase.containsKey(missileCoord);
	}
	
	/* Met à jour le bateau si besoin et renvoie le code correspond à :
	 * Touché -> 1
	 * Touché-Coulé -> 2
	 * Pas touché -> 0
	 * Si déjà touché à cet endroit, renvoie 1 si touché et 2 si déjà touché-coulé 
	 */
	public int  hitShip(Coordinates missileCoord){
		if (this.isHit(missileCoord)){
			if (this.isAlreadyHit(missileCoord)) {
				return 2;
			}
			
			this.shipCase.put(missileCoord, true);
			
			if (this.isSunk()) {
				return 2;
			}
			
			return 1;
		} else {
			return 0;
		}
	}
	
	private boolean isAlreadyHit(Coordinates missileCoord) {
		return this.shipCase.get(missileCoord);
	}
	
	@Override
	public String toString() {
		String s="[";

		for (Coordinates key : this.shipCase.keySet()){
			s+= key + " : "+shipCase.get(key)+", ";
	}
		s=s.substring(0, s.length()-2);
		s+="]";
		return s;
	}
	
	public int valueCoordinates(Coordinates c) {
		// Return the value of a cell
		// touched -> 1
		// not touched -> 0
		// -1 not on the boat
		
		if (this.shipCase.containsKey(c)) {
			if (this.shipCase.get(c)) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}

}

