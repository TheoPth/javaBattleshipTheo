package bataille.actifs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import bataille.passifs.Coordinates;
import bataille.passifs.Fleet;
import bataille.passifs.FleetType;
import bataille.passifs.Ship;
import exception.OverlapException;

/**
 * @author root
 *
 */
public class IA  implements Playerable {
	
	private int level;
	public static final int BOARD_SIZE = 10;
	// keep shot in memory
	private HashMap<Coordinates, Integer> opponentBoard;
	private String name = "";
	
	
	private Fleet fleet;
	
	public IA(int level, String name) {
		super();
		this.name = name;
		this.level = level;
		opponentBoard = new HashMap<Coordinates, Integer>();
		
		// Init IA
		fleet = initFleet();
		
	}
	
	public void reInit() {
		// Reninit a ia, keep the same name
		Fleet fleet = initFleet();
		this.fleet = fleet;
		opponentBoard = new HashMap<Coordinates, Integer>();
	}
	
	private  Fleet initFleet() {
		Fleet fleet = new Fleet();
		
		// Ship creation
		for(FleetType fleetType: FleetType.values() ) {
			for (int i = 0; i < fleetType.getAmount(); i++) {
				boolean insert = false;
				while (!insert) {
					try {
						fleet.addShip(createShipRandom(fleetType));
						insert = true;
					} catch (OverlapException e) {
					}
				}	
			}
		}
		return fleet;
	}
	
	private Ship createShipRandom (FleetType fleetType) {
		Coordinates start = Coordinates.getRandomCoordinates();
		ArrayList<Coordinates> vc = start.validCoordinates(fleetType.getSize());
		
		Coordinates end = vc.get((int) Math.round(Math.random()*(vc.size() - 1)));
		
		Ship ship = null;
		
		while (ship == null) {
			try {
				ship = new Ship (fleetType.name(), start.toString(), end.toString());
			} catch (Exception e) {
				createShipRandom(fleetType);
			}
		}
		
		return ship;
	}
	
	public Coordinates getShot () {
		// Depend on the level of the AI
		
		switch (this.level) {
			case 1 :
				return  Coordinates.getRandomCoordinates();
			case 2 :
				return this.getShot2();
			case 3 : 
				return this.getShot3();
			default :
				return null;
		}
	}
	
	private Coordinates getShot3() {
		// Shot lvl 3
		Coordinates touched;
		
		// Shotted ship but not sunk
		touched = getTouchedAfloatShip();
		
		// If none touched boat, continue random shot 
		if (touched == null) {
			return this.getShotOdd();
		}
		
		// get all touched and no-sunked coordinates in contact
		ArrayList<Coordinates> touchedShip = getAllTouched(touched);
		// List of coordinates shootable
		ArrayList<Coordinates> listHitable = getCoordinatesAvailable(touchedShip);
		
		int al = (int)Math.floor(Math.random()*listHitable.size());
		
		return listHitable.get(al);
	}

	private ArrayList<Coordinates> getCoordinatesAvailable(ArrayList<Coordinates> touched) {
		// get first and last coodinates of a touched shi
		// if boat is touched one time, twice coordinates of same
		// if none ship is touched, return null
		// Sometimes boat are touched and we can and the first/end coordinates are false
		// in this case, shot random around
		
		Coordinates coordEnd[] = getEndCoord(touched);
		
		
		if (coordEnd != null) {
			return touchableCoord(coordEnd);
		}
		else {
			ArrayList<Coordinates> res = new ArrayList<Coordinates>();
			res.add(this.getShot2());
			return res;
		}
	}

	

	private ArrayList<Coordinates> touchableCoord(Coordinates[] coordEnd) {
		// Search goods coordiantes
		
		ArrayList<Coordinates> res = new ArrayList<Coordinates>();
		// first case : only 1 case, we can touch arround
		if (coordEnd[0].equals(coordEnd[1])) {
			addIfValid(res, coordEnd[0].up());
			addIfValid(res, coordEnd[1].down());
			addIfValid(res, coordEnd[1].right());
			addIfValid(res, coordEnd[0].left());
			
		}
		
		// Second case : we can touch in the direction of the boat and after the second or
		// before the first coordinates
		else if (!coordEnd[0].isHorizontal(coordEnd[1])) {
			addIfValid(res, coordEnd[0].up());
			addIfValid(res, coordEnd[1].down());
			
		} else {
			addIfValid(res, coordEnd[0].left());
			addIfValid(res, coordEnd[1].right());
		}
		
		
		// remove bad coordinates
		res = checkShot(res);
		if (res.isEmpty()) {
			// if boat are touched and checkshot don't have right coordinates
			if (coordEnd[0].isHorizontal(coordEnd[1])) {
				addIfValid(res, coordEnd[0].up());
				addIfValid(res, coordEnd[1].down());
				
			} else {
				addIfValid(res, coordEnd[0].left());
				addIfValid(res, coordEnd[1].right());
			}	
		}
		
		// if after all check we can't get a good coordiantesn, return randomood coordinates
		res = checkShot(res);
		if (res.isEmpty()) {
			
			ArrayList<Coordinates> res2 = new ArrayList<Coordinates> ();
			res2.add(this.getShotOdd());
			return res2;
		}
		
		return checkShot(res);
		
	}

	private ArrayList<Coordinates> checkShot(ArrayList<Coordinates> res) {
		
		// Filter wrong coordinates
		ArrayList<Coordinates> realRes = new ArrayList<Coordinates>();
		for (Coordinates coord : res) {
			if (!this.opponentBoard.containsKey(coord)) {
				realRes.add(coord);
			}
		}
		
		return realRes;
	}

	private void addIfValid(ArrayList<Coordinates> res, Coordinates coordinates) {
		if (coordinates != null && coordinates.isValid()) {
			res.add(coordinates);
		}
	}

	private Coordinates[] getEndCoord(ArrayList<Coordinates> touched) {
		// get start/end of touched boat
		Coordinates min = touched.get(0);
		Coordinates max = touched.get(0);
		for (Coordinates coord : touched) {
			if (coord.isBefore(min)) {
				min = coord;
			}
			
			if (coord.isAfter(max)) {
				max = coord;
			}
		}
		
		Coordinates[] res = {min, max};
		return res;
	}

	private Coordinates getTouchedAfloatShip() {
		// Seach a touched non sunked boat
		
		for (Entry<Coordinates, Integer> coord : this.opponentBoard.entrySet()) {
			if (coord.getValue() == 1) {
				return coord.getKey();
			}
		}
		
		return null;
	}
	
	private Coordinates getShotOdd() {
		// shot only on odd Coordinates
		
		for (int i = (int)'A'; i < Player.BOARD_SIZE + (int)'A'; i++) {
			for (int j = 1; j <= Player.BOARD_SIZE; j++ ) {
				Coordinates shot = new Coordinates (Character.toString((char)i) + Integer.valueOf(j));
				if (shot.isOdd() && !this.opponentBoard.containsKey(shot)) {
					return shot;
				}
			}
		}
		
		
		return getShot2();
	}

	private Coordinates getShot2() {
		Coordinates shot = Coordinates.getRandomCoordinates();
		while (this.opponentBoard.containsKey(shot)) {
			shot = Coordinates.getRandomCoordinates();
		}
		
		return shot;
	}

	
	public Fleet getFleet(){
		return this.fleet;
	}
	
	public int shot(Coordinates pos){
		return this.fleet.shot(pos);
	};
	
	
	@Override
	public String toString() {
		return "IA [lvl=" + this.level + ", fleet=" + fleet + "]";
	}


	// Update result shoot
	public void setResShot(Coordinates coordShot, int resShot) {
		this.opponentBoard.put(coordShot, resShot);
		if (resShot == 2) {
			this.opponentBoard.put(coordShot, 1);
			sunkBoat(getAllTouched(coordShot));
		}
	}

	private void sunkBoat(ArrayList<Coordinates> coordShot) {
		for (Coordinates coord : coordShot) {
			this.opponentBoard.put(coord, 2);
		}
		
	}

	public ArrayList<Coordinates> getAllTouched(Coordinates coord) {
		// get all touched coordinates next to a touched coordinates 
		ArrayList<Coordinates> touchedCoord = new ArrayList<Coordinates>();
		
		return suiv(coord, touchedCoord);
	}
	
	private ArrayList<Coordinates> suiv (Coordinates coordShot, ArrayList<Coordinates> toucheC) {
		// get next touched coordinates
		
		if (this.opponentBoard.get(coordShot) == 1 ) {
		toucheC.add(coordShot);
		
			if (coordShot.up() !=  null && this.opponentBoard.containsKey(coordShot.up())) {
				if (!toucheC.contains(coordShot.up())) {
					suiv(coordShot.up(), toucheC);
				}
				
			}
			
			if (coordShot.down() !=  null && this.opponentBoard.containsKey(coordShot.down())) {
				if (!toucheC.contains(coordShot.down())) {
					suiv(coordShot.down(), toucheC);
				}
				
			}
			
			if (coordShot.left() !=  null && this.opponentBoard.containsKey(coordShot.left())) {
				if (!toucheC.contains(coordShot.left())) {
					suiv(coordShot.left(), toucheC);
				}
				
			}
			
			if (coordShot.right() !=  null && this.opponentBoard.containsKey(coordShot.right())) {
				if (!toucheC.contains(coordShot.right())) {
					suiv(coordShot.right(), toucheC);
				}
				
			}
		}
		return toucheC;
	}
	
	public HashMap<Coordinates, Integer> getOpponentBoard() {
		return this.opponentBoard;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	


}
