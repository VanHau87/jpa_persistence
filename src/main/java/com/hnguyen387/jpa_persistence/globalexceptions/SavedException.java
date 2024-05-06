package com.hnguyen387.jpa_persistence.globalexceptions;

public class SavedException extends RuntimeException{

	private static final long serialVersionUID = 2386090889202474715L;

	public SavedException(String message) {
		super(message);
	}
	
}
