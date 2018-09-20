package jdroplet.core.fileupload;

public class PostFile extends FileUploadBase {
	
	private FileItemFactory fileItemFactory;
	
	public PostFile() {
        super();
    }
	
	public PostFile(FileItemFactory fileItemFactory) {
        super();
        this.fileItemFactory = fileItemFactory;
    }
	
	@Override
	public FileItemFactory getFileItemFactory() {
		return fileItemFactory;
	}
	
	public void setFileItemFactory(FileItemFactory factory) {
        this.fileItemFactory = factory;
    }

}
