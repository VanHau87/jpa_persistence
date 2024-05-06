package com.hnguyen387.jpa_persistence.globalexceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorDetails {
	private String status;
	private int code;
	private String errMessage;
	private String dateTime;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
		this.dateTime = formatter.format(dateTime);
	}
	
}
