package jdroplet.exceptions;

public abstract class CoreException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CoreException(String message) {
		super(message);
	}
}
