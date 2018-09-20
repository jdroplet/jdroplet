package jdroplet.enums;

import java.io.Serializable;

public class PostClientType  implements Serializable  {

	private static final long serialVersionUID = -5389675790532842049L;
	public static PostClientType NORMAL 	= new PostClientType(0);
	public static PostClientType W2G 			= new PostClientType(1);
	public static PostClientType ANDROID 	= new PostClientType(2);
	public static PostClientType IPHONE 		= new PostClientType(3);
	public static PostClientType W3G 			= new PostClientType(4);
	public static PostClientType UCWEB 		= new PostClientType(9);
	public static PostClientType QQWEB 		= new PostClientType(10);
	public static PostClientType XCLIENT		= new PostClientType(99);// 特殊活动使用的回复方式
	
	private int mValue;
	public PostClientType(int value) {
		mValue = value;
	}
	public int getValue() {
		return mValue;
	}
	
	public String toString() {
		return Integer.toString(mValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		return equals((PostClientType)obj);
	}
	
	public boolean equals(PostClientType obj) {
		return this.mValue == obj.mValue;
	}
	
	public static PostClientType get(int value) {
		 return new PostClientType(value);
	}
}


