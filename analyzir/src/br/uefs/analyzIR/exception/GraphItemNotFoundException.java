package br.uefs.analyzIR.exception;

public class GraphItemNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphItemNotFoundException() {
	}

	public GraphItemNotFoundException(String message) {
		super(message);
	}

	public GraphItemNotFoundException(Throwable cause) {
		super(cause);
	}

	public GraphItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GraphItemNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
