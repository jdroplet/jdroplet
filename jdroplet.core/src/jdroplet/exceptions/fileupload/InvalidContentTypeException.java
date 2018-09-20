package jdroplet.exceptions.fileupload;

public class InvalidContentTypeException extends FileUploadException {
	public InvalidContentTypeException() {
        super();
    }
	
	public InvalidContentTypeException(String message) {
        super(message);
    }
}
