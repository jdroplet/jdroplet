package jdroplet.enums;

public enum CreateUserStatus {
    Created(0),
    UserExist(1),
    DuplicateUsername(2),
    DisallowedUsername(3),
    Updated(4),
    Deleted(5);
    
    private int value;
    CreateUserStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}

