package jdroplet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import jdroplet.core.SystemConfig;
import jdroplet.exceptions.SystemException;


public class FileUtil {

 	private static void saftCloseStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException ex) {

			}
		}
	}

	private static void saftCloseStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException ex) {

			}
		}
	}

	public static boolean createPath(String path) {
		File f = new File(path);

		if (f.exists() == false)
			return f.mkdirs();

		return false;
	}

	public static File createFile(String name) {
		File file = null;

		file = new File(name);
		if (file.exists() == false) {
			try {
				String path = file.getParent();

				createPath(path);
				file.createNewFile();
			} catch (IOException ex) {
				throw new SystemException(ex.getMessage());
			}
		}

		return file;
	}

	public static boolean isLocalFile(String file) {
		if (file.indexOf("file://") == 0)
			return true;
		else if (file.charAt(0) == '/')
			return true;

		return false;
	}

	public static boolean exists(String filename) {
		return new File(filename).exists();
	}

	public static byte[] getFileData(String fullpath) {
		return getFileData(new File(fullpath));
	}

	public static byte[] getFileData(File file) {
		byte[] data = null;
		int size = 0;
		ByteArrayOutputStream baos = null;
		FileInputStream fis = null;

		baos = new ByteArrayOutputStream(1024);
		data = new byte[1024];
		try {
			fis = new FileInputStream(file);
			while ((size = fis.read(data)) != -1) 
				baos.write(data, 0, size);			
		} catch (FileNotFoundException ex) {
			throw new SystemException(ex.getMessage());
		} catch (IOException ex) {
			throw new SystemException( ex.getMessage());
		} finally {
			saftCloseStream(fis);
			saftCloseStream(baos);
		}
		data = baos.toByteArray();
		return data;
	}
	
	public static String getFileContent(String path) {
		return getFileContent(path, "utf-8");
	}
	
	public static String getFileContent(String path, String charsetName) {
		byte[] datas = null;
		String content = null;
		
		datas = getFileData(path);
		try {
			content = new String(datas, charsetName);
		} catch (UnsupportedEncodingException e) {
			throw new SystemException( e.getMessage());
		}
		return content;
	}
	
	public static Object data2Object (byte[] bytes) {  
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
        Object obj = null;     
        try {       
            bis = new ByteArrayInputStream (bytes);       
            ois = new ObjectInputStream (bis);       
            obj = ois.readObject();     
            ois.close();  
            bis.close();  
        } catch (IOException ex) {       
        	throw new SystemException( ex.getMessage());
        } catch (ClassNotFoundException ex) {       
        	throw new SystemException( ex.getMessage());
        } finally {
        	saftCloseStream(bis);
        	saftCloseStream(ois);
        }
        return obj;   
    }  
	
	public static void deleteFile(String filename) {
		File file = null;

		file = new File(filename);
		file.delete();
	}

	public static void saveFile(String folder, String name, Object obj) {
		byte[] bytes = null;     
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        
        bos = new ByteArrayOutputStream();     
        try {       
            oos = new ObjectOutputStream(bos);        
            oos.writeObject(obj);       
            oos.flush();        
			bytes = bos.toByteArray();
        } catch (IOException ex) {       
        	throw new SystemException( ex.getMessage());
        }  finally {
        	saftCloseStream(oos);
        	saftCloseStream(bos);
        }
        saveFile(folder, name, bytes);
	}
	
	public static void saveFile(String filename, byte[] data) {
		File file = null;

		file = createFile(filename);
		saveFile(file, data);
	}

	public static void saveFile(File file, byte[] data) {
		OutputStream os = null;

		try {
			os = new FileOutputStream(file);
			os.write(data);
			os.flush();
		} catch (FileNotFoundException ex) {
			throw new SystemException( ex.getMessage());
		} catch (IOException ex) {
			throw new SystemException( ex.getMessage());
		} finally {
			saftCloseStream(os);
		}
	}

	public static boolean mkfiledirs(String filename) {
		File siteDir = null;
		String path = null;
		int lastPos = -1;

		lastPos = filename.lastIndexOf("/");
		path = filename.substring(0, lastPos);
		siteDir = new File(path);
		if (!siteDir.exists()) {
			return siteDir.mkdirs();
		}
		return false;
	}
}