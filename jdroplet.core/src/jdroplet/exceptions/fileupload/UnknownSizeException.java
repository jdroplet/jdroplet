package jdroplet.exceptions.fileupload;


public class UnknownSizeException extends FileUploadException {
	
	public UnknownSizeException() {
		
	}
	
	public UnknownSizeException(String message) {
        super(message);
    }
}
