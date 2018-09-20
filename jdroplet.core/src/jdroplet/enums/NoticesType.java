package jdroplet.enums;

public enum NoticesType {

	ERROR(0),
	SUCCESS(1);
	
	private int value;
	NoticesType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
		
	public static NoticesType get(int value) {
		 return NoticesType.values()[value];
	}
}
