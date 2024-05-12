package com.hnguyen387.jpa_persistence.ch04.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportResult;
import com.hnguyen387.jpa_persistence.ch04.utils.ReadUserStrategy;
import com.hnguyen387.jpa_persistence.ch04.utils.ReaderContext;
import com.hnguyen387.jpa_persistence.ch04.utils.WriteErrorStrategy;
import com.hnguyen387.jpa_persistence.ch04.utils.WriterContext;

@RestController
@RequestMapping("import")
public class ImportDataController {
	@Autowired
	private ReadUserStrategy readUserStrategy;
	@Autowired
	private WriteErrorStrategy writeErrorStrategy;
	
	@PostMapping("users")
	public ResponseEntity<String> handleImport(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }
		ReaderContext rContext = new ReaderContext(readUserStrategy);
		ImportResult importResult = rContext.readFile(file);
		List<Row> failedData = importResult.failedRowsData;
		if (failedData.size() > 0) {
			Path path = Paths.get("D:/backend/springboot-v3/projects/excelFiles");
			WriterContext<Row> wContext = new WriterContext<>(writeErrorStrategy);
			wContext.writeFile(path, failedData);
		}
		return ResponseEntity.ok(String.format("There are %d row(s) imported success, %d row(s) failed", 
				importResult.successfulRows, failedData.size()));
	}
}
