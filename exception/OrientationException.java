package exception;

public class OrientationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public OrientationException(){
	}
	
	public String toString() {
		return "This Ship is not horizontal or vertical.";
	}
}
