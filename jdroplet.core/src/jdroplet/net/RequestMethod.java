package jdroplet.net;

public enum RequestMethod {

	POST,
	GET,
	OPTIONS;


	public boolean equals(String val) {
		return this.toString().equals(val);
	}
}
