package jdroplet.exceptions.fileupload;

public class SizeLimitExceededException extends FileUploadException {
	
	public SizeLimitExceededException() {
    }
	
	public SizeLimitExceededException(final String msg) {
        super(msg);
    }
}
