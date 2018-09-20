package jdroplet.util;

import jdroplet.security.DigestUtil;

public class ShortUrlHelp {

	private static String[] chars = new String[] { "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
			"s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };

	public static String[] getShort(String val) {
		String[] results = null;

		val = DigestUtil.MD5(val);
		results = new String[4];
		for (int i = 0; i < 4; i++) {
			String sTempSubString = val.substring(i * 8, i * 8 + 8);
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			for (int j = 0; j < 6; j++) {
				long index = 0x0000003D & lHexLong;
				outChars += chars[(int) index];
				lHexLong = lHexLong >> 5;
			}
			results[i] = outChars;
		}
		return results;
	}
}
