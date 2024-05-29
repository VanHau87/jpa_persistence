package com.hnguyen387.jpa_persistence.ch05.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hnguyen387.jpa_persistence.ch05.entities.Author;
import com.hnguyen387.jpa_persistence.ch05.services.AuthorService;

@RestController
@RequestMapping("ch05/authors")
public class AuthorController {
	
	@Autowired
	private AuthorService service;
	
	@PostMapping
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		var savedAuthor = service.createAuthor(author);
		return new ResponseEntity<Author>(savedAuthor, HttpStatus.OK);
	}
	@PostMapping("update")
	public ResponseEntity<Author> updateAuthor(@RequestBody Author author) {
		var updatedAuthor = service.updateField(author);
		return new ResponseEntity<Author>(updatedAuthor, HttpStatus.OK);
	}
}
