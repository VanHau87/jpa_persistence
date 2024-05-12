package com.hnguyen387.jpa_persistence.ch04.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.stereotype.Component;

import com.hnguyen387.jpa_persistence.globalexceptions.ImportException;

@Component
public class WriteErrorStrategy implements WriteStrategy<Row>{
	private final String FILE_NAME = "Errors_Import_User_";
	private final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH-mm-ss";
	private final String EXTENTION = ".xlsx";
	
	@Override
	public void write(Path path, List<Row> rows) {
		try (Workbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX)){
			Sheet sheet = workbook.createSheet("ErrUsers");
			Row headerRow = sheet.createRow(0);
			String headers = "No, Username, RegistrationDate, Email, Level, Active, Message";
			ExcelHelper.createHeader(headerRow, headers);
			int rowNum = 1;
			for (Row source : rows) {
				Row target = sheet.createRow(rowNum++);
				ExcelHelper.copyRow(source, target);
				
			}
			ExcelHelper.setAutoSize(sheet, new int[] {1,2,3});
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
			String fileName = FILE_NAME + LocalDateTime.now().format(formatter) + EXTENTION;
			Path fullPath = path.resolve(fileName);
			try (OutputStream outputStream = new FileOutputStream(fullPath.toString())) {
                workbook.write(outputStream);
            }
		} catch (Exception e) {
			throw new ImportException("Faild to write file: " + e.getMessage());
		}
	}

}
