package com.hnguyen387.jpa_persistence.ch04.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hnguyen387.jpa_persistence.ch04.utils.CellError;
import com.hnguyen387.jpa_persistence.ch04.validation.ValidUser;

@ValidUser
public class UserDto {
	private Long id;
	private String username;
	private LocalDate registrationDate;
	private String email;
	private boolean emailExist;
	private int level;
	private Boolean active;
	private List<CellError> errMessage;
	public Long getId() {
		return id;
	}
	
	public UserDto() {
		errMessage = new ArrayList<>();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailExist() {
		return emailExist;
	}

	public void setEmailExist(boolean emailExist) {
		this.emailExist = emailExist;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<CellError> getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(List<CellError> errMessage) {
		this.errMessage = errMessage;
	}
	
}
