package com.hnguyen387.jpa_persistence.ch04.models;

public class UserProjection {
	
	public static class EmailOnly {
		private String email;

		public EmailOnly(String email) {
			this.email = email;
		}

		public String getEmail() {
			return email;
		} 
		
	}
}
