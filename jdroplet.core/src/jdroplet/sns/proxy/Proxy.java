package jdroplet.sns.proxy;

import jdroplet.data.model.User;



public abstract class Proxy {

	protected Integer shopId;
	protected String appId;
	protected String appSecret;
	protected String code;
	protected String token;
	protected String userId;

	public abstract void setShopId(Integer shopId);

	public abstract void requestAccessToken();

	public abstract void requestAuthorizeCode(String redirect, String scope);

	public abstract User getUser();

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
}
