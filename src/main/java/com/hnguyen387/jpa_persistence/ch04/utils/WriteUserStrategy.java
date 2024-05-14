package com.hnguyen387.jpa_persistence.ch04.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.stereotype.Component;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportedUser;
import com.hnguyen387.jpa_persistence.globalexceptions.ImportException;

@Component
public class WriteUserStrategy implements WriteStrategy<ImportedUser>{
	private ExcelHelper excelHelper;
	
	private final String FILE_NAME = "Errors_Import_User_";
	private final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH-mm-ss";
	private final String EXTENTION = ".xlsx";
	
	public void initializeHelper(Workbook workbook) {
        this.excelHelper = ExcelHelper.createWithWorkbook(workbook);
    }
	
	public void write(Path path, List<ImportedUser> dtos) {
		try (Workbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX)){
			this.initializeHelper(workbook);
			Sheet sheet = workbook.createSheet("ErrUsers");
			Row headerRow = sheet.createRow(0);
			String headers = "No, Username, RegistrationDate, Email, Level, Active, Message";
			excelHelper.createHeader(headerRow, headers);
			int rowNum = 1;
			for (ImportedUser userDto : dtos) {
				Row row = sheet.createRow(rowNum++);
				createRow(row, userDto, workbook);
			}
			excelHelper.setAutoSize(sheet, new int[] {1,2,3});
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
	
	private void createRow(Row row, ImportedUser dto, Workbook workbook) {
		int i = 0;
		row.createCell(i++).setCellValue(dto.getId());
		row.createCell(i++).setCellValue(dto.getUsername());
		Date date = excelHelper.fromLocalDate(dto.getRegistrationDate());
		Cell dateCell = row.createCell(i++);
		dateCell.setCellValue(date);
		dateCell.setCellStyle(excelHelper.setFormatForCell(workbook, "yyyy-MM-dd"));
		row.createCell(i++).setCellValue(dto.getEmail());
		row.createCell(i++).setCellValue(dto.getLevel());
		row.createCell(i++).setCellValue(dto.isActive());
		String messages = dto.getErrMessage()
					.stream()
					.map(t -> t.message)
					.collect(Collectors.joining(", "));
		row.createCell(i++).setCellValue(messages);
	}
	
}
