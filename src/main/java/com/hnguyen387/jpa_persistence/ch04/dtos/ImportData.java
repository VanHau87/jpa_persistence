package com.hnguyen387.jpa_persistence.ch04.dtos;

import java.util.List;

import com.hnguyen387.jpa_persistence.ch04.utils.CellError;

public class ImportData {
	private List<CellError> errMessage;

	public ImportData() {
	}

	public ImportData(List<CellError> errMessage) {
		this.errMessage = errMessage;
	}

	public List<CellError> getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(List<CellError> errMessage) {
		this.errMessage = errMessage;
	}
	
}
