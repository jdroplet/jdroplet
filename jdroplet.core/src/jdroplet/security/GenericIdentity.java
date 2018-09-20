package jdroplet.security;


public class GenericIdentity implements IIdentity {
	String user_name;
	String authentication_type;
	
	public GenericIdentity (String user_name, String authentication_type) {
		if (user_name == null)
			throw new NullPointerException ("user_name");

		if (authentication_type == null)
			throw new NullPointerException ("authentication_type");

		this.user_name = user_name;
		this.authentication_type = authentication_type;
	}
	
	public boolean IsAuthenticated() {
		return !user_name.equals("");
	}

	public String getAuthenticationType() {
		return authentication_type;
	}

	public String getName() {
		return user_name;
	}

}
