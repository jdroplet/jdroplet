package jdroplet.exceptions;

public class PageNotFoundException extends ApplicationException {
 
	private static final long serialVersionUID = 1L;

	public PageNotFoundException() {
		super("PageNotFound");
	}
	
	public PageNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
