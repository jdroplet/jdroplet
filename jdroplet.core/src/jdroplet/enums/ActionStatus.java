package jdroplet.enums;

public enum ActionStatus {

	SUCCESS(0), // 
	ERROR(1), //
	UNLOGIN(2), // user unlogin
	
	OUTOFRANGE(3), // out of range
	CLOSED(4), // close 
	UNAPPROVED(5), //  
	
	QUESTIONERROR(6), // 答题回答错误 
	ACCOUNTERROR(7), // 账号异常  
	OUTOFDATE(8), //时间未到  
	
	CAPTCHAERROR(9),//验证码错误  
	UNANSWER(10),// 未回答问题 
	ANTIATTACK(11), //频繁请求 
	
	DISPOSE(12), // 处理中
	REQCAPTCHA(13),// request captcha
	REQSMSCAPTCHA(14),// request sms captcha
	
	RESET(15),
	ACCOUNTACTIVE(16);	//用户没有激活
	
	private int value;

	ActionStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return Integer.toString(value);
	}
}
