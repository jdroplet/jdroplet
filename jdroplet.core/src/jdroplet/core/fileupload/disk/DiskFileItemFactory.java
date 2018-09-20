package jdroplet.core.fileupload.disk;

import java.io.File;

import jdroplet.core.fileupload.FileItem;
import jdroplet.core.fileupload.FileItemFactory;


public class DiskFileItemFactory implements FileItemFactory {

	public static final int DEFAULT_SIZE_THRESHOLD = 10240;
	private File repository;
	private int sizeThreshold = DEFAULT_SIZE_THRESHOLD;
	
	public DiskFileItemFactory() {
    }
	
	public DiskFileItemFactory(int sizeThreshold, File repository) {
        this.sizeThreshold = sizeThreshold;
        this.repository = repository;
    }
	
	public File getRepository() {
        return repository;
    }
	
	public void setRepository(File repository) {
        this.repository = repository;
    }
	
	public int getSizeThreshold() {
        return sizeThreshold;
    }
	
	public void setSizeThreshold(int sizeThreshold) {
        this.sizeThreshold = sizeThreshold;
    }
	
	public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
        return new DiskFileItem(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
    }
}
