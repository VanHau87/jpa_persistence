package com.hnguyen387.jpa_persistence.ch05.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class DateTimeEntity {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", insertable = false)
	private LocalDateTime updatedDate;
	
	@PrePersist
	protected void onCreate() {
		createdDate = LocalDateTime.now();
	} 
	@PreUpdate
	protected void onUpdate() {
		updatedDate = LocalDateTime.now();
	}
	
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
}
