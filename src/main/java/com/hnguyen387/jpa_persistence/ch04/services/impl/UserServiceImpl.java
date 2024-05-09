package com.hnguyen387.jpa_persistence.ch04.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;
import com.hnguyen387.jpa_persistence.ch04.models.User;
import com.hnguyen387.jpa_persistence.ch04.repos.UserRepository;
import com.hnguyen387.jpa_persistence.ch04.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository repository;
	
	@Transactional
	@Override
	public void saveAll(List<UserDto> dtos) {
		List<User> users = new ArrayList<>();
		for (UserDto dto : dtos) {
			User user = new User();
			BeanUtils.copyProperties(dto, user, "id");
			users.add(user);
		}
		repository.saveAllAndFlush(users);
	}

	@Override
	public Set<String> getAllEmails() {
		return repository.getAllEmails();
	}
	
	
}
