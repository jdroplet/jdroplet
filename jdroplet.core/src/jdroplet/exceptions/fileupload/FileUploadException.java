package jdroplet.exceptions.fileupload;

public class FileUploadException extends RuntimeException {
	
	public FileUploadException() {
    }
	
	public FileUploadException(final String msg) {
        super(msg);
    }
}
