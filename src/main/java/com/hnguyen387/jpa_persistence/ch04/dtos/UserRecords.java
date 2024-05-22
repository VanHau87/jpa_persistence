package com.hnguyen387.jpa_persistence.ch04.dtos;

public class UserRecords {
	//for UI
	public record SimpleField(String field, String value) {}
	//for projections
	public record DtoV1(String username, String email) {}
	public record DtoV2(String username, String email, int level) {}
	
}
