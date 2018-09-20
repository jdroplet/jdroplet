package jdroplet.enums;

public enum SortUsersBy {
	//最新提交
	LastUser(0);
	
	private int value;
	SortUsersBy(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
