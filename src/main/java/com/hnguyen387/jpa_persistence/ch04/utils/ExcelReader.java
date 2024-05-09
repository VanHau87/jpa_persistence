package com.hnguyen387.jpa_persistence.ch04.utils;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;
import com.hnguyen387.jpa_persistence.ch04.services.UserService;
import com.hnguyen387.jpa_persistence.globalexceptions.ExcelException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.*;

@Component
public class ExcelReader {
	@Autowired
	private UserService userService;
	
	public List<UserDto> readUsersFromFile(InputStream is) {
		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			List<UserDto> users = new ArrayList<>();
			List<UserDto> errUsers = new ArrayList<>();
			Set<String> emails = userService.getAllEmails();
			boolean header = true;
			int count = 0;
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	        Validator validator = factory.getValidator();
			for (Row row : sheet) {
				if (header) {
					header = false;
					continue;
				}
				UserDto dto = new UserDto();
				var id = parseLong(readCellData(row.getCell(0)));
				applyValue(id, dto, (value, object) -> object.setId((long)value));
				String username = String.valueOf(readCellData(row.getCell(1)));
				dto.setUsername(username.trim());
				var date = parseDate(readCellData(row.getCell(2)));
				applyValue(date, dto, (value, object) -> object.setRegistrationDate((LocalDate)date));
				String email = String.valueOf(readCellData(row.getCell(3)));
				if (emails.contains(email)) {
					dto.setEmailExist(true);
				} 
				dto.setEmail(email.trim());
				var level = parseInt(readCellData(row.getCell(4)));
				applyValue(level, dto, (value, object) -> object.setLevel((int)level));
				var isActive = parseBoolean(readCellData(row.getCell(5)));
				applyValue(isActive, dto, (value, object) -> object.setActive((boolean)isActive));
				validateUserDto(validator, dto);
				if (dto.getErrMessage().size() == 0) {
					users.add(dto);
					if (++count%10 == 0) {
						userService.saveAll(users);
						users.clear();
					}
				} else {
					errUsers.add(dto);
				}
			}
			if (!users.isEmpty()) {
	            userService.saveAll(users);
	        }
			return errUsers;
		} catch (Exception e) {
			throw new ExcelException("Faild to read file: " + e.getMessage());
		}
	}
	private void validateUserDto(Validator validator, UserDto dto) {
		Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<UserDto> violation : violations) {
                CellError error = new CellError(violation.getMessage());
                dto.getErrMessage().add(error);
            }
		}
	}
}
