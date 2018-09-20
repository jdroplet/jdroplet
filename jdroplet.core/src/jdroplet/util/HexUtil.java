package jdroplet.util;

import java.io.UnsupportedEncodingException;

public class HexUtil {
	
	public static String getHexString (String str)	{
		try {
			return getHexString (str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}
	
	public static String getHexString (byte [] bytes) {
		StringBuilder result = new StringBuilder (bytes.length * 2);

		for (byte b : bytes)
			result.append(String.format("%02x", b));

		return result.toString ();
	}
	
	public static byte toHexValue (char c, boolean high) {
		byte v;
		if (c >= '0' && c <= '9')
			v = (byte) (c - '0');
		else if (c >= 'a' && c <= 'f')
			v = (byte) (c - 'a' + 10);
		else if (c >= 'A' && c <= 'F')
			v = (byte) (c - 'A' + 10);
		else
			throw new RuntimeException ("Invalid hex character");

		if (high)
			v <<= 4;

		return v;
	}
	
	public static byte [] getBytes (String key, int len) {
		byte [] result = new byte [len / 2];
		for (int i = 0; i < len; i += 2)
			result [i / 2] = (byte) (toHexValue (key.charAt(i), true) + toHexValue (key.charAt(i + 1), false));

		return result;
	}
}
