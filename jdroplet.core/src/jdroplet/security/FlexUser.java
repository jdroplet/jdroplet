package jdroplet.security;


import java.security.Principal;

public class FlexUser extends GenericIdentity implements Principal {

	public FlexUser(String user_name, String authentication_type) {
		super(user_name, authentication_type);
	}
}
