package jdroplet.core;

public class RawUrl {
	private String name;
	private String value;
	private String rawPath;
	private int size;
	private String[] vars;
	
	public RawUrl(String name, String value) {
		this.name = name;
		this.value = value;
	
		this.parseUrl();
	}
	
	private void parseUrl() {
		String[] p = this.value.split(",");
		
		this.vars = new String[p.length];
		this.size = ((((p[0]).trim()).equals("")) ? 0 : p.length);
		
		for(int i=0; i<this.size; i++) {
			this.vars[i] = p[i].trim();
		}
		
		//======
		String[] m = this.name.split("\\.");
		int params = this.value.length() == 0 ? 0 : p.length;//Integer.parseInt(m[2]);
		StringBuilder buf = new StringBuilder(32);
		
		//buf.append("/");
		buf.append(m[0]);
		buf.append("/");
		buf.append(m[1]);
		for(int idx=0; idx<params; idx++) {
			//buf.append("/%");
			//buf.append(idx+1);
			//buf.append("$d");
			
			buf.append("/{").append(idx).append("}");
		}
		buf.append(SystemConfig.getPageExtension());
		this.rawPath = buf.toString();
	}

	public String getName() {
		return this.name;
	}

	public int getSize() {
		return this.size;
	}

	public String[] getVars() {
		return this.vars;
	}
	
	public String rawPath() {
		return this.rawPath;
	}
}
