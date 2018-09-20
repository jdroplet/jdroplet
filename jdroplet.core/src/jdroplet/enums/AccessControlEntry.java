package jdroplet.enums;

public enum AccessControlEntry {
	
	NotSet(0),
	Allow(1),
	Deny(2);
	
	private int value;
	AccessControlEntry(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
