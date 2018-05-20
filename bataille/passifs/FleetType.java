package bataille.passifs;

public enum FleetType {
    CARRIER(5,1),
    BATTLESHIP(4,1),
    CRUISER(3,1),
    SUBMARINE(3,1),
    DESTROYER(2,1);
    

    private int size;
    private int amount;
    
    
    private FleetType(int size, int amount) {
		this.size = size;
		this.amount = amount;
	}

    public int getSize() {
        return size;
    }
    
    public int getAmount(){
    	return amount;
    }
    
    public int getTotalShip() {
    	int total = 0;
    	for(FleetType fleetType: FleetType.values()) {
    		total += fleetType.getAmount();
    	}
    	
    	return total;
    }

}
