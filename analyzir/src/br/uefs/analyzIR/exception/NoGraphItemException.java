package br.uefs.analyzIR.exception;

/**
 * 
 * @author lucas
 *
 */
public class NoGraphItemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoGraphItemException() {	}

	public NoGraphItemException(String message) {
		super(message);
	}

	public NoGraphItemException(Throwable cause) {
		super(cause);
	}

	public NoGraphItemException(String message, Throwable cause) {
		super(message, cause);	
	}

	public NoGraphItemException(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
