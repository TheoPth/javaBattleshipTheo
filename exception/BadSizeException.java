package exception;

public class BadSizeException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadSizeException(){
	}
	
	public String toString() {
		return "Size input doesn't match with Ship size.";
	}
}
