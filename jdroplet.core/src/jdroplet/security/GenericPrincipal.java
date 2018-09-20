package jdroplet.security;

public class GenericPrincipal {
	
	IIdentity identity;
	String [] roles;
	
	public GenericPrincipal (IIdentity identity, String [] roles) {
		if (identity == null)
			throw new NullPointerException ("identity");

		this.identity = identity;
		if (roles != null) {
			// make our own (unchangeable) copy of the roles
			this.roles = new String [roles.length];
			for (int i=0; i < roles.length; i++)
				this.roles [i] = roles [i];
		}
	}
	
	public IIdentity getIdentity() {
		return identity;
	}
	
	public String[] getRoles() {
		return roles.clone();
	}
	
//	public boolean IsInRole(String role) {
//		if (roles == null)
//			return false;
//
//		for (String r : roles)
//			if (role.equals(r))
//				return true;
//
//		return false;
//	}
}
