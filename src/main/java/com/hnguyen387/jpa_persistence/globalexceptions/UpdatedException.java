package com.hnguyen387.jpa_persistence.globalexceptions;

public class UpdatedException extends RuntimeException{

	private static final long serialVersionUID = 2055758166443451861L;

	public UpdatedException(String message) {
		super(message);
	}
	
}
