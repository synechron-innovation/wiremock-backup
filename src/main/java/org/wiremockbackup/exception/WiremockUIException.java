package org.wiremockbackup.exception;

public class WiremockUIException extends Exception {
	private static final long serialVersionUID = 2807405255325037688L;

	public WiremockUIException(String errorMessage) {
        super(errorMessage);
    }
	public WiremockUIException(String errorMessage, Throwable err) {
	    super(errorMessage, err);
	}
	public WiremockUIException(Throwable err) {
	    super(err);
	}
}
