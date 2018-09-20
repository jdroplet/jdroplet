package jdroplet.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class InetAddressUtils {

	public static String getMacAddress() {
		String mac_addr = "";
		String str = "";
		try {
			NetworkInterface NIC = NetworkInterface.getByName("eth0");
			byte[] buf = NIC.getHardwareAddress();
			for (int i = 0; i < buf.length; i++) {
				str = str + byteHEX(buf[i]);
			}
			mac_addr = str.toUpperCase();
		} catch (SocketException e) {
		}
		return mac_addr;
	}
	
	public static InetAddress getAddress(String name) {
		InetAddress addr = null;
		Enumeration<NetworkInterface> itfs = null;
		Enumeration<InetAddress> ias = null;
		NetworkInterface ni = null;

		try {
			itfs = NetworkInterface.getNetworkInterfaces();
			while (itfs.hasMoreElements()) {
				ni = itfs.nextElement();
				if (!ni.getName().equals(name)) {
					continue;
				} else {
					ias = ni.getInetAddresses();
					while (ias.hasMoreElements()) {
						InetAddress ia = (InetAddress) ias.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						addr = ia;
					}
					break;
				}
			}
		} catch (SocketException e) {
		}
		return addr;
	}

	public static String getHostAddress() {
		return getAddress("eth0").getHostAddress();
	}
	
	public static boolean hostAddressIs(String ip) {
		return ip.equals(getHostAddress());
	}

	public static String byteHEX(byte ib) {
		char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = digit[(ib >>> 4) & 0X0F];
		ob[1] = digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}
}
