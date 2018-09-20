package jdroplet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteUtil {

	/**
	 * 将可序列化的对象转成二进制流
	 * @param obj 需要序列号的对象
	 * @return 返回对象的二进制流
	 * @throws IOException
	 */
	public static byte[] o2b(Object obj) throws IOException {
		ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        byte[] bytes = null;
        
        bo = new ByteArrayOutputStream();
        oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        bytes = bo.toByteArray();
        bo.close();
        oo.close();
        
        return bytes;
	}
	
	/**
	 * 将二进制流转换成对象
	 * @param bytes 需要转换成对象的二进制流
	 * @return 返回一个对象
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object b2o(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream is = null;
        ObjectInputStream ois = null;
        
        is = new ByteArrayInputStream(bytes);
        ois = new ObjectInputStream(is);
        
        return ois.readObject();
	}
}
