package jdroplet.util;

import jdroplet.core.DateTime;

import java.util.Date;

public class SearchQuery {
	
	public String getField() {
		return field;
	}

	public void setField(String filed) {
		this.field = filed;
	}

	public Object getValue() {
		if (LIKE.equals(this.operate))
			return "%" + value + "%";
		else
			return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String LIKE = "like";
	public static String EQ = "=";
	public static String NEQ = "<>";

	public static String GT = ">";
	public static String GET = ">=";

	public static String LT = "<";
	public static String LET = "<=";

	public static String AND = "&";
	public static String OR = "|";

	private String field;
	private Object value;
	private String operate;
	
	
	public SearchQuery(String field, Object value) {
		this(field, value, SearchQuery.EQ);
	}
	
	public SearchQuery(String field, Object value, String operate) {
		this.field = field;
		this.value = value;
		this.operate = operate;
	}
	
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	@Override
	public String toString() {
		String val = null;

		if (this.value instanceof Integer)
			val = this.value.toString();
		else if (this.value instanceof Date)
			val = "'" + DateTime.toString((Date) this.value) + "'";
		else
			val = "'" + this.value + "'";

		if (LIKE.equals(this.operate))
			return this.field + " like '%"  + this.value + "%' ";
		else if (EQ.equals(this.operate))
			return this.field + "=" + val + "";
		else if (NEQ.equals(this.operate))
			return this.field + "<>" + val + "";
		else if (GT.equals(this.operate))
			return this.field + ">" + val + "";
		else if (GET.equals(this.operate))
			return this.field + ">=" + val + "";
		else if (LT.equals(this.operate))
			return this.field + "<" + val + "";
		else if (LET.equals(this.operate))
			return this.field + "<=" + val + "";
		else if (AND.equals(this.operate))
			return this.field + " & " + val + "=" + val ;
		else if (OR.equals(this.operate))
			return this.field + " | " + val + "=" + val ;
		else
			return this.field + "=" + this.value + "";
	}

	public String toQueryString() {
		if (LIKE.equals(this.operate))
			return this.field + " like ? ";
		else if (EQ.equals(this.operate))
			return this.field + "=?";
		else if (NEQ.equals(this.operate))
			return this.field + "<>?";
		else if (GT.equals(this.operate))
			return this.field + ">?";
		else if (GET.equals(this.operate))
			return this.field + ">=?";
		else if (LT.equals(this.operate))
			return this.field + "<?";
		else if (LET.equals(this.operate))
			return this.field + "<=?";
		else if (AND.equals(this.operate))
			return this.field + " & ? = " + this.field;
		else
			return this.field + "=?";
	}
}
