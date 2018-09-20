package jdroplet.enums;

public enum LoginUserStatus {
	// 验证登录成功
	Success(0),
	// 无效凭据
	InvalidCredentials(1),
	// 帐号待批准
	AccountPending(2),
	// 帐号帐号禁止
	AccountBanned(3),
	// 帐号未批准
	AccountDisapproved(4);

	private int value;

	LoginUserStatus(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static LoginUserStatus get(Integer value) {
		return LoginUserStatus.values()[value];
	}
}
