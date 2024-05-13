package com.hnguyen387.jpa_persistence.ch04.utils;

import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.applyValue;
import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.buildValidator;
import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.parseBoolean;
import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.parseDate;
import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.parseInt;
import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.parseLong;
import static com.hnguyen387.jpa_persistence.ch04.utils.ExcelHelper.readCellData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportResult;
import com.hnguyen387.jpa_persistence.ch04.dtos.ImportedUser;
import com.hnguyen387.jpa_persistence.ch04.services.UserService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class ReadUserStrategy implements ReadStrategy{
	@Autowired
	private UserService userService;
	
	public ImportResult read(Iterator<Row> iterator) {
		//read all other rows
        int count = 0;
        int columns = 0;
        int successfulRows = 0;
        List<ImportedUser> users = new ArrayList<>();  
		List<Row> rowErrors = new ArrayList<>();
		Set<String> emails = userService.getAllEmails();
		Validator validator = buildValidator();
        while (iterator.hasNext()) {
			Row row = iterator.next();
			columns = 0;
			ImportedUser dto = new ImportedUser();
			var id = parseLong(readCellData(row, columns++));
			applyValue(id, dto, (value, user) -> user.setId((long)value));
			var username = String.valueOf(readCellData(row, columns++)).trim();
			applyValue(username, dto, (value, user) -> user.setUsername(username));
			var date = parseDate(readCellData(row, columns++));
			applyValue(date, dto, (value, user) -> user.setRegistrationDate((LocalDate)date));
			var email = String.valueOf(readCellData(row, columns++)).trim();
			applyValue(email, dto, (value, user) -> user.setEmail(email));
			var level = parseInt(readCellData(row, columns++));
			applyValue(level, dto, (value, object) -> object.setLevel((int)level));
			var isActive = parseBoolean(readCellData(row, columns++));
			applyValue(isActive, dto, (value, object) -> object.setActive((boolean)isActive));
			if (emails.contains(email)) {
				dto.setEmailExist(true);
			} 
			validateDto(validator, dto);
			if (dto.getErrMessage().size() == 0) {
				users.add(dto);
				if (++count%20 == 0) {
					successfulRows += userService.saveAll(users);
					users.clear();
				}
			} else {
				String messages = dto.getErrMessage()
						.stream()
						.map(t -> t.message)
						.collect(Collectors.joining(", "));
				row.createCell(columns).setCellValue(messages);
				rowErrors.add(row);
			}
		}
		if (!users.isEmpty()) {
			successfulRows += userService.saveAll(users);
        }
		return new ImportResult(successfulRows, columns, rowErrors);
	}
	private void validateDto(Validator validator, ImportedUser dto) {
		Set<ConstraintViolation<ImportedUser>> violations = validator.validate(dto);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<ImportedUser> violation : violations) {
                CellError error = new CellError(violation.getMessage());
                dto.getErrMessage().add(error);
            }
		}
	}
	
}
