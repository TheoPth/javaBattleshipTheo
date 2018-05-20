package bataille.actifs;

import bataille.passifs.Coordinates;
import bataille.passifs.Fleet;

public interface Playerable {
	public String getName();
	public Fleet getFleet();
	public int shot(Coordinates coord);
	public void setResShot(Coordinates coordShot, int resShot);
	public Coordinates getShot();
	public void reInit();
}
