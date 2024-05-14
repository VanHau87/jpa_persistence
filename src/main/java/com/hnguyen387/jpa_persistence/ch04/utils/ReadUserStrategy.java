package com.hnguyen387.jpa_persistence.ch04.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportResult;
import com.hnguyen387.jpa_persistence.ch04.dtos.ImportedUser;
import com.hnguyen387.jpa_persistence.ch04.services.UserService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class ReadUserStrategy implements ReadStrategy{
	private UserService userService;
	public record ParseResult(ImportedUser user, int index) {}
	
	public ReadUserStrategy(UserService userService) {
		this.userService = userService;
	}
	
	public ImportResult read(Iterator<Row> iterator, ExcelHelper helper) {
        int count = 0;
        int colIndex = 0;
        int successfulRows = 0;
        List<ImportedUser> users = new ArrayList<>();  
		List<Row> rowErrors = new ArrayList<>();
		Set<String> emails = userService.getAllEmails();
		Validator validator = helper.buildValidator();
		
        while (iterator.hasNext()) {
			Row row = iterator.next();
			ParseResult result = parseAndValidateRow(row, emails, validator, colIndex, helper);
			var user = result.user;
			if (user.getErrMessage().isEmpty()) {
				users.add(user);
				if (++count%20 == 0) {
					successfulRows += userService.saveAll(users);
					users.clear();
				}
			} else {
				addRowError(row, result.index, user, rowErrors);
			}
		}
		if (!users.isEmpty()) {
			successfulRows += userService.saveAll(users);
        }
		return new ImportResult(successfulRows, colIndex, rowErrors);
	}
	private ParseResult parseAndValidateRow(Row row, Set<String> emails, Validator validator, 
			int colIndex, ExcelHelper helper) {
		ImportedUser dto = new ImportedUser();
		colIndex = 0;
		var id = helper.parseLong(helper.readCell(row, colIndex++));
		applyValue(id, dto, (v, user) -> user.setId((Long) v));
		var username = String.valueOf(helper.readCell(row, colIndex++)).trim();
		applyValue(username, dto, (v, user) -> user.setUsername(v));
		var date = helper.parseDate(helper.readCell(row, colIndex++));
		applyValue(date, dto, (v, user) -> user.setRegistrationDate((LocalDate)v));
		var email = String.valueOf(helper.readCell(row, colIndex++)).trim();
		applyValue(email, dto, (v, user) -> user.setEmail(v));
		var level = helper.parseInt(helper.readCell(row, colIndex++));
		applyValue(level, dto, (v, user) -> user.setLevel((int)v));
		var isActive = helper.parseBoolean(helper.readCell(row, colIndex++));
		applyValue(isActive, dto, (v, user) -> user.setActive((boolean)v));
		if (emails.contains(email)) {
			dto.setEmailExist(true);
		} 
		validateDto(validator, dto);
		return new ParseResult (dto, colIndex);
	}
	private void validateDto(Validator validator, ImportedUser dto) {
		Set<ConstraintViolation<ImportedUser>> violations = validator.validate(dto);
		if (!violations.isEmpty()) {
			for (ConstraintViolation<ImportedUser> violation : violations) {
                dto.getErrMessage().add(new CellError(violation.getMessage()));
            }
		}
	}
	private <T> void applyValue(T value, ImportedUser user, BiConsumer<T, ImportedUser> setter) {
		if (value instanceof CellError error) {
			List<CellError> errors = user.getErrMessage();
			if (errors == null) {
                errors = new ArrayList<>();
                user.setErrMessage(errors);
            }
		} else {
			setter.accept(value, user);
		}
    }
	private void addRowError(Row row, int columns, ImportedUser dto, List<Row> rowErrors) {
        String messages = dto.getErrMessage()
                .stream()
                .map(e -> e.message)
                .collect(Collectors.joining(", "));
        row.createCell(columns).setCellValue(messages);
        rowErrors.add(row);
    }

}
