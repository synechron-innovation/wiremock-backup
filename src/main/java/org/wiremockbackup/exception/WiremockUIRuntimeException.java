package org.wiremockbackup.exception;

public class WiremockUIRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 3045285999234690774L;

	public WiremockUIRuntimeException(String errorMessage) {
        super(errorMessage);
    }
	public WiremockUIRuntimeException(String errorMessage, Throwable err) {
	    super(errorMessage, err);
	}
	public WiremockUIRuntimeException(Throwable err) {
	    super(err);
	}
}
