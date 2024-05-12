package com.hnguyen387.jpa_persistence.ch04.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportResult;
import com.hnguyen387.jpa_persistence.globalexceptions.ImportException;

public class ReaderContext {
	private ReadStrategy readStrategy;

	public ReaderContext(ReadStrategy readStrategy) {
		this.readStrategy = readStrategy;
	}

	public ReadStrategy getReadStrategy() {
		return readStrategy;
	}

	public void setReadStrategy(ReadStrategy readStrategy) {
		this.readStrategy = readStrategy;
	}
	
	public ImportResult readFile(MultipartFile file) {
		return readFile(file, 0);
	}
	public ImportResult readFile(MultipartFile file, int at) {
		return readFile(file, at, true);
	}
	public ImportResult readFile(MultipartFile file, int at, boolean skipHeader) {
		try (InputStream input = file.getInputStream();
			Workbook workbook = new XSSFWorkbook(input)) {
			Sheet sheet = workbook.getSheetAt(at);
			Iterator<Row> iterator = sheet.iterator();
			if (skipHeader) {
				if (iterator.hasNext())
		        	iterator.next();
			}
			return readStrategy.read(iterator);
		} catch (IOException e) {
			throw new ImportException(String.format("Failed to import data: %s", e.getMessage()));
		}
	}
}
