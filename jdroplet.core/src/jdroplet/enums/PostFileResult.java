package jdroplet.enums;

public enum PostFileResult {
	
	Success(0),
	Fail(1),
	UnAllowFileType(2),
	OutOfSize(3);
	
	
	private int value;
	PostFileResult(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
