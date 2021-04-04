package formatter.exceptions;

@SuppressWarnings("serial")
/**
 * Exception when {@link formatter.models.Entity}'s unique identifier uniqueness is violated
 */
public class IllegalIdentifierException extends Exception {

	/**
	 * Main constructor for initialization
	 */
	public IllegalIdentifierException() {
		super("Identifier already exists!");
	}
}
