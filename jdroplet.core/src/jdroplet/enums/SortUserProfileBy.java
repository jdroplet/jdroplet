package jdroplet.enums;

public enum SortUserProfileBy {
	COUNT(1),
	POINT(2);
	
	private int value;
	SortUserProfileBy(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
