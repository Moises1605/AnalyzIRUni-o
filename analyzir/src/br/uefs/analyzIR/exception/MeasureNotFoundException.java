package br.uefs.analyzIR.exception;

@SuppressWarnings("serial")
public class MeasureNotFoundException extends Exception {

	public MeasureNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public MeasureNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MeasureNotFoundException(Throwable cause) {
		super(cause);
	}

	public MeasureNotFoundException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public MeasureNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
