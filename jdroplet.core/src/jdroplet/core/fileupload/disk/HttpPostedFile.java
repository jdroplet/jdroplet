package jdroplet.core.fileupload.disk;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jdroplet.core.fileupload.FileItem;
import jdroplet.exceptions.fileupload.FileUploadException;


public final class HttpPostedFile  {
	private FileItem item;
	private String extension = "";
	
	HttpPostedFile(FileItem item) {
		this.item = item;
	}
	
	public String getExtension() {
		if (this.extension == null || this.extension.equals("")) {
			this.extension = this.item.getName().substring(this.item.getName().lastIndexOf('.') + 1);
		}
		
		return this.extension;
	}
	
	public String getContentType() {
		return item.getContentType();
	}
	
	public String getName() {
		return item.getName();
	}
	
	public long getSize() {
		return item.getSize();
	}
	
	public void saveAs(String filename) {
		BufferedInputStream inputStream = null;
		FileOutputStream outputStream = null;

		try {
			inputStream = new BufferedInputStream(this.item.getInputStream());
			outputStream = new FileOutputStream(filename);
			
			int c;
			byte[] b = new byte[4096];
			while ((c = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, c);
			}
		}
		catch (IOException e) {
			throw new FileUploadException(e.getMessage());
		}
		finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				}
				catch (IOException e) { }
			}

			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) { }
			}
		}
	}
}
