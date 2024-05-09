package com.hnguyen387.jpa_persistence.ch04.controllers;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;
import com.hnguyen387.jpa_persistence.ch04.utils.ExcelReader;
import com.hnguyen387.jpa_persistence.ch04.utils.ExcelWriter;

@RestController
@RequestMapping("ch04/users")
public class UserController {
	@Autowired
	private ExcelReader reader;
	@Autowired
	private ExcelWriter writer;
	
	@PostMapping("import")
	public ResponseEntity<String> handleImportExcelUsers(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }
		List<UserDto> userDtos = null;
		try (InputStream is = file.getInputStream()){
			  userDtos = reader.readUsersFromFile(is);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		int count = userDtos != null ? userDtos.size() : 0;
		if (count > 0) {
			Path path = Paths.get("D:/backend/springboot-v3/projects/excelFiles");
			writer.writeUsersToFile(path, userDtos);
			return ResponseEntity.ok(String.format("%d row(s) failed to write.", count));
		}
		return ResponseEntity.ok("File handled success!");
	}
}
