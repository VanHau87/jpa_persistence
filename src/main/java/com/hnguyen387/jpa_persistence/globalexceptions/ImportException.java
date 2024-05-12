package com.hnguyen387.jpa_persistence.globalexceptions;

public class ImportException extends RuntimeException{

	private static final long serialVersionUID = -2847768518683640147L;

	public ImportException(String message) {
		super(message);
	}
	
}
