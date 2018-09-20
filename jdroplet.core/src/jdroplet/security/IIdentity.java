package jdroplet.security;

public interface IIdentity {
	String getAuthenticationType();

	boolean IsAuthenticated();

	String getName();
}
