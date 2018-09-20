package jdroplet.security;

public class FormsIdentity implements IIdentity {
	FormsAuthenticationTicket ticket;
	
	public FormsIdentity (FormsAuthenticationTicket ticket) {
		this.ticket = ticket;
	}
	
	public String getAuthenticationType() {
		return "Forms";
	}

	public boolean IsAuthenticated() {
		return true;
	}

	public String getName() {
		return ticket.getName();
	}

	public FormsAuthenticationTicket Ticket() {
		return ticket;
	}
}
