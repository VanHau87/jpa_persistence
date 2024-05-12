package com.hnguyen387.jpa_persistence.ch04.services;

import java.util.List;
import java.util.Set;

import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;

public interface UserService {
	int saveAll(List<UserDto> dtos);
	Set<String> getAllEmails();
}
