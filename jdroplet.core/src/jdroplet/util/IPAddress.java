package jdroplet.util;

import jdroplet.net.WebClient;
import org.json.JSONObject;

public class IPAddress {
	private long address;

	public static final IPAddress ANY = new IPAddress(0);
	public static final IPAddress BROADCAST = IPAddress.parse("255.255.255.255");
	public static final IPAddress LOOPBACK = IPAddress.parse("127.0.0.1");
	public static final IPAddress NONE = IPAddress.parse("255.255.255.255");
	
	public IPAddress(long addr) {
		address = addr;
	}

	public long getAddress() {
		return address;
	}

	public void setAddress(long address) {
		this.address = address;
	}
	
	public static IPAddress random() {
		long[][] ip_regions = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
				{ 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
				{ 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
				{ 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
				{ 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
				{ -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
				{ -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
				{ -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
				{ -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
				{ -569376768, -564133889 } }; // 222.16.0.0-222.95.255.255};
		long[] region = null;
		long ip = 0;
		int idx = 0;
		
		idx = (int)Math.round(Math.random() * (ip_regions.length - 1));
		region = ip_regions[idx];
		ip = Math.round(Math.random() * (region[1] - region[0]) + region[0]);
		return new IPAddress(ip);
	}

	public static IPAddress parse(String ip) {
		if (TextUtils.isEmpty(ip))
			throw new IllegalArgumentException("IP address cannot be null.");
		
		IPAddress ret;
		if ((ret = parseIPV4(ip)) == null)
			throw new IllegalArgumentException(
					"An invalid IP address was specified#" + ip);

		return ret;
	}

	private static IPAddress parseIPV4(String ipv4) {
		String[] temp = ipv4.split("\\.");
		if (temp.length > 4)
			throw new IllegalArgumentException (ipv4);
		
		int[] ips = new int[4];
		
        int position1 = ipv4.indexOf(".");
        int position2 = ipv4.indexOf(".", position1 + 1);
        int position3 = ipv4.indexOf(".", position2 + 1);
        
		ips[0] = Integer.parseInt((ipv4.substring(0, position1)));
        ips[1] = Integer.parseInt(ipv4.substring(position1 + 1, position2));
        ips[2] = Integer.parseInt(ipv4.substring(position2 + 1, position3));
        ips[3] = Integer.parseInt(ipv4.substring(position3 + 1));
        	
        long ip = (ips[0] << 24) + (ips[1] << 16) + (ips[2] << 8) + ips[3];
        return new IPAddress (ip);
	}
	
	public String toString() {
		return Long.toString((this.address >> 24) & 0xff) + "."
				+ Long.toString((this.address >> 16) & 0xff) + "."
				+ Long.toString((this.address >> 8) & 0xff) + "."
				+ Long.toString(this.address & 0xff);
	}
	
	public static String toString(long l) {
		return new IPAddress(l).toString();
	}
}
