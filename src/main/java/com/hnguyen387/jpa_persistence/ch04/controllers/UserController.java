package com.hnguyen387.jpa_persistence.ch04.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hnguyen387.jpa_persistence.ch04.dtos.PagingAndSortingRecords.MultiSortedPageUsers;
import com.hnguyen387.jpa_persistence.ch04.dtos.SortedPageUser;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserRecords;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserRecords.DtoV1;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserRecords.DtoV2;
import com.hnguyen387.jpa_persistence.ch04.models.projections.BaseUserProjections;
import com.hnguyen387.jpa_persistence.ch04.models.projections.UserProjections;
import com.hnguyen387.jpa_persistence.ch04.repos.UserRepository;
import com.hnguyen387.jpa_persistence.ch04.services.UserService;

@RestController
@RequestMapping("ch04/users")
public class UserController {
	
	@Autowired
	private UserRepository repository;
	@Autowired
	private UserService userService;
	
	@PostMapping("page/sort/v1")
	public ResponseEntity<List<UserDto>> findByActiveWithPagable(@RequestBody SortedPageUser page){
		List<UserDto> users = userService.findByActive(page);
		return new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);
	}
	@PostMapping("page/sort/v2")
	public ResponseEntity<List<UserDto>> findByActiveWithMultiSortedPagable(@RequestBody MultiSortedPageUsers page){
		List<UserDto> users = userService.findByActiveMultiSort(page);
		return new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);
	}
	/*
	@PostMapping("queries/v1")
	public ResponseEntity<Long> testCountByActiveAndLevel(@RequestBody UserDto dto){
		long count = repository.countByActiveAndByLevel(dto.isActive(), dto.getLevel());
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}
	@PostMapping("queries/v2")
	public ResponseEntity<List<User>> testFindByActiveAndLevel(@RequestBody UserDto dto){
		List<User> users = repository.findByActiveAndByLevel(dto.isActive(), dto.getLevel());
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	@PostMapping("queries/v3")
	public ResponseEntity<Long> testCountByLevel(@RequestBody UserDto dto){
		long count = repository.countByLevel(dto.getLevel());
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}
	@PostMapping("queries/v4")
	public ResponseEntity<List<Object[]>> testFindByNameAndThenSort(@RequestBody UserDto dto){
		List<Object[]> results = repository.findByNameAndThenSort(dto.getUsername(), Sort.by("username"));
		return new ResponseEntity<List<Object[]>>(results, HttpStatus.OK);
	}
	@PostMapping("queries/v5")
	public ResponseEntity<List<Object[]>> testFindByNameAndThenSort_V1(@RequestBody UserDto dto){
		List<Object[]> results = repository.findByNameAndThenSort(dto.getUsername(), 
				Sort.by("email_length").descending());
		return new ResponseEntity<List<Object[]>>(results, HttpStatus.OK);
	}
	@PostMapping("queries/v6")
	public ResponseEntity<List<Object[]>> testFindByNameAndThenSort_V2(@RequestBody UserDto dto){
		List<Object[]> results = repository.findByNameAndThenSort(dto.getUsername(), 
				JpaSort.unsafe("LENGTH(u.email)"));
		return new ResponseEntity<List<Object[]>>(results, HttpStatus.OK);
	}
	@PostMapping("queries/v7")
	public ResponseEntity<List<UserProjection.EmailOnly>> testProjections(@RequestBody Integer level){
		List<UserProjection.EmailOnly> results = repository.findByLevel(level);
		return new ResponseEntity<List<UserProjection.EmailOnly>>(results, HttpStatus.OK);
	}
	*/
	@PostMapping("queries/v7")
	public ResponseEntity<List<BaseUserProjections>> testProjectionsV1(@RequestBody Boolean isActive){
		//var result = repository.findByActiveWithQuery(isActive, BaseUserProjections.class);
		var result = repository.findByActive(isActive, BaseUserProjections.class);
		return new ResponseEntity<List<BaseUserProjections>>(result, HttpStatus.OK);
	}
	@PostMapping("queries/v8")
	public ResponseEntity<List<UserProjections>> testProjectionsV2(@RequestBody Boolean isActive){
		//var results = repository.findByActiveWithQuery(isActive, UserProjections.class);
		var results = repository.findByActive(isActive, UserProjections.class);
		return new ResponseEntity<List<UserProjections>>(results, HttpStatus.OK);
	}
}
