package jdroplet.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {

	public static String MD5(String str) {
		return crypt(str, "UTF-8", "MD5");
	}

	public static String SHA1(String str) {
		return crypt(str, "UTF-8", "SHA-1");
	}

	private static String crypt(String str, String charsetName, String algorithm) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException(
					"String to encript cannot be null or zero length");
		}

		StringBuffer hexString = new StringBuffer();

		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(str.getBytes(charsetName));
			byte[] hash = md.digest();

			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("" + ex);
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException("" + ex);
		}

		return hexString.toString();
	}
}