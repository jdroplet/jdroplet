package jdroplet.enums;

public enum SortSectionBy {
	
	ID(0),
	DateCreated(1),
	SortOrder(2);
	
	
	private int value;
	
	SortSectionBy(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public static SortSectionBy get(int value) {
		 return SortSectionBy.values()[value];
	}
	
}
