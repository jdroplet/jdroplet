package jdroplet.enums;

public class Permission {

	public static final long View 			= 0x0000001;
	public static final long Post 			= 0x0000002;
	public static final long Edit 			= 0x0000004;
	public static final long Delete 		= 0x0000008;
	public static final long Vote 			= 0x0000010;
	public static final long CreatePoll 	= 0x0000020;
	public static final long Moderate 	= 0x0000040;
	public static final long Administer 	= 0x0000080;
	
	/*View		(0x0000001),
	Post		(0x0000002),
	Edit		(0x0000004),
	Delete		(0x0000008),
	Vote		(0x0000010),
	CreatePoll	(0x0000020),
	Moderate	(0x0000040),
	Administer	(0x0000040);	
	
	private long value;
	Permission(long value) {
		this.setValue(value);
	}
	
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	*/
}
