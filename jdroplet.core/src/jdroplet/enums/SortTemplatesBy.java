package jdroplet.enums;

public enum SortTemplatesBy {
	//最新提交
	LastTemplate(0);
	
	private int value;
	SortTemplatesBy(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
