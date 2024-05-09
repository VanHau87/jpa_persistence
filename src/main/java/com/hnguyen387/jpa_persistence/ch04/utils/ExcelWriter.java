package com.hnguyen387.jpa_persistence.ch04.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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

import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;
import com.hnguyen387.jpa_persistence.globalexceptions.ExcelException;

@Component
public class ExcelWriter {
	private final String FILE_NAME = "Errors_Import_User_";
	
	public void writeUsersToFile(Path path, List<UserDto> dtos) {
		checkOrCreate(path);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss");
		String fileName = FILE_NAME + LocalDateTime.now().format(formatter) + ".xlsx";
		Path fullPath = path.resolve(fileName);
		try (Workbook workbook = new XSSFWorkbook(XSSFWorkbookType.XLSX)){
			Sheet sheet = workbook.createSheet("ErrUsers");
			Row headerRow = sheet.createRow(0);
			String headers = "No, Username, RegistrationDate, Email, Level, Active, Message";
			createHeader(headerRow, headers);
			int rowNum = 1;
			for (UserDto userDto : dtos) {
				Row row = sheet.createRow(rowNum++);
				createRow(row, userDto, workbook);
			}
			ExcelHelper.setAutoSize(sheet, new int[] {1,2,3});
			try (OutputStream outputStream = new FileOutputStream(fullPath.toString())) {
                workbook.write(outputStream);
            }
		} catch (Exception e) {
			throw new ExcelException("Faild to write file: " + e.getMessage());
		}
	}
	private void createHeader(Row headerRow, String headers) {
		String[] titles = headers.split(",");
		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];
			headerRow.createCell(i).setCellValue(title);
		}
	}
	private void createRow(Row row, UserDto dto, Workbook workbook) {
		row.createCell(0).setCellValue(dto.getId());
		row.createCell(1).setCellValue(dto.getUsername());
		Cell dateCell = row.createCell(2);
		Date date = ExcelHelper.fromLocalDate(dto.getRegistrationDate());
		dateCell.setCellValue(date);
		dateCell.setCellStyle(ExcelHelper.setFormatForCell(workbook, "yyyy-MM-dd"));
		row.createCell(3).setCellValue(dto.getEmail());
		row.createCell(4).setCellValue(dto.getLevel());
		row.createCell(5).setCellValue(dto.getActive());
		String messages = dto.getErrMessage()
					.stream()
					.map(t -> t.message)
					.collect(Collectors.joining(", "));
		row.createCell(6).setCellValue(messages);
	}
	private void checkOrCreate(Path path) {
        try {
            if (!Files.exists(path)) {
            	Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }
	
}
