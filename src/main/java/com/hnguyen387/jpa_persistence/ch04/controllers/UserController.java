package com.hnguyen387.jpa_persistence.ch04.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hnguyen387.jpa_persistence.ch04.dtos.PageUser;
import com.hnguyen387.jpa_persistence.ch04.models.User;
import com.hnguyen387.jpa_persistence.ch04.repos.UserRepository;

@RestController
@RequestMapping("ch04/users")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	
	@PostMapping("test")
	public ResponseEntity<List<User>> testJpaMethods(@RequestBody PageUser page){
		List<User> users = repository.findByActive(page.isActive, PageRequest.of(page.pageNumber, page.pageSize));
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
}
