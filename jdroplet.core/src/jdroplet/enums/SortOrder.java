package jdroplet.enums;

public enum SortOrder {
	//最新提交
	ASC(0),
	//
	DESC(1);
	
	private int value;
	SortOrder(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public static SortOrder get(int value) {
		return SortOrder.values()[value];
	}
}
