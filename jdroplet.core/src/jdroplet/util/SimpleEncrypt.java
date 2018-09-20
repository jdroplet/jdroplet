package jdroplet.util;

import jdroplet.cache.AppCache;
import jdroplet.cache.ICache;
import jdroplet.core.DateTime;

public class SimpleEncrypt {
	
	private static String getSeed() {
		String key = "app_simple_encrypt_seed";
		Object seed = AppCache.get(key);
		
		if (seed == null) {
			seed = Integer.toString(DateTime.now().getSecond());
			AppCache.add(key, seed, ICache.DAY_FACTOR);
		}
		
		return (String)seed;
	}
	
	public static String encrypt(String original) {
        int pos;
        int mask;
		String seed;
		StringBuilder sb;
        
		pos = 0;
		seed = getSeed();
        sb = new StringBuilder();
        
        for (int idx=0; idx<original.length(); idx++, pos++) {
        	if (pos >= seed.length()) {
        		pos = 0;
        	}
        	mask = (int)original.charAt(idx) ^ (int)seed.charAt(pos);
        	sb.append(String.valueOf((char)mask));
        } 
        return HexUtil.getHexString(sb.toString());
	}
	
	public static String decrypt(String encrypt) {
		int pos;
		int mask;
		byte [] bytes;
		String seed;
		StringBuilder sb;
        
		pos = 0;
		seed = getSeed();
        sb = new StringBuilder();
        try {
        	bytes = HexUtil.getBytes (encrypt, encrypt.length());
			encrypt = new String(bytes, "UTF-8");
		} catch (Exception e) {
			return "";
		}
        for (int idx=0; idx<encrypt.length(); idx++, pos++) {
        	if (pos >= seed.length()) {
        		pos = 0;
        	}
        	mask = (int)encrypt.charAt(idx) ^ (int)seed.charAt(pos);
        	sb.append(String.valueOf((char)mask));
        } 
        
        return sb.toString();
	}
}
