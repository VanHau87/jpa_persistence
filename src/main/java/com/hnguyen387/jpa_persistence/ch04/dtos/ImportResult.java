package com.hnguyen387.jpa_persistence.ch04.dtos;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;

public class ImportResult {
	public int successfulRows;
	public int columns;
	public List<Row> failedRowsData;
	public ImportResult() {
	}
	public ImportResult(int successfulRows, int columns, List<Row> failedRowsData) {
		this.successfulRows = successfulRows;
		this.columns = columns;
		this.failedRowsData = failedRowsData;
	}
	
}
