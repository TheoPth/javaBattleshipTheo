package exception;

public class CoordException extends Exception {
	private static final long serialVersionUID = 1L;

	public String toString () {
		return "Your coordinates are not valid.";
	}
}
