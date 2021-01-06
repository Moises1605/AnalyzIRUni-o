package br.uefs.analyzIR.exception;

public class NoGraphPointException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoGraphPointException() {
	}	

	public NoGraphPointException(String message) {
		super(message);
	}

	public NoGraphPointException(Throwable cause) {
		super(cause);
	}

	public NoGraphPointException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoGraphPointException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
