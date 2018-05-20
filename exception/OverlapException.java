package exception;

public class OverlapException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public OverlapException(){
	}
	
	public String toString() {
		return "This Ship overlaps with another one.";
	}
}
