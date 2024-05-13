package com.hnguyen387.jpa_persistence.ch04.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportedUser;
import com.hnguyen387.jpa_persistence.ch04.dtos.PageInfo;
import com.hnguyen387.jpa_persistence.ch04.dtos.PagingAndSortingRecords.MultiSortedPageInfo;
import com.hnguyen387.jpa_persistence.ch04.dtos.PagingAndSortingRecords.MultiSortedPageUsers;
import com.hnguyen387.jpa_persistence.ch04.dtos.PagingAndSortingRecords.SortedField;
import com.hnguyen387.jpa_persistence.ch04.dtos.SortedPageUser;
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
	public int saveAll(List<ImportedUser> dtos) {
		List<User> users = new ArrayList<>();
		for (ImportedUser dto : dtos) {
			User user = new User();
			BeanUtils.copyProperties(dto, user, "id");
			users.add(user);
		}
		List<User> savedUsers = repository.saveAllAndFlush(users);
		return savedUsers.size();
	}

	@Override
	public Set<String> getAllEmails() {
		return repository.getAllEmails();
	}

	@Override
	public List<UserDto> findByActive(SortedPageUser sortedPage) {
		UserDto dto = sortedPage.dto;
		boolean isActive = Optional.ofNullable(dto) //check null
				.map(UserDto::isActive) //convert UserDto to boolean 
				.orElse(false);//if UserDto null, return false 
		PageInfo info = sortedPage.info;
		Sort.Direction direction = setSortedDirection(info.direction);
		Pageable page = PageRequest.of(info.number, info.size, direction, info.sortBy);
		return repository.findByActive(isActive, page)
				.stream()
				.map(this::convertToDto)
				.toList();
	}
	private Sort.Direction setSortedDirection(String direction) {
		return convertToDirection(direction).equals("ASC") ?
				Sort.Direction.ASC :
					Sort.Direction.DESC;
	}

	private String convertToDirection(String direction) {
		try {
			int convert = Integer.valueOf(direction);
			return convert >= 0 ? "ASC" : "DESC";
		} catch (NumberFormatException e) {
			var textDirection = direction.trim().toUpperCase();
			return textDirection.startsWith("ASC") ? "ASC" : "DESC";
		}
	}

	@Override
	public List<UserDto> findByActiveMultiSort(MultiSortedPageUsers multiSortedPage) {
		var dto = multiSortedPage.dto();
		boolean isActive = Optional.ofNullable(dto)
				.map(user -> user.isActive())
				.orElse(false);
		var info = multiSortedPage.info();
		MultiSortedPageInfo pageInfo =	Optional.ofNullable(info)
				.map(page -> page)
				.orElse(new MultiSortedPageInfo(0, 10, null));
		Order[] orders = setMultiSortDirection(pageInfo.sorts());
		Pageable page = PageRequest.of(info.number(), info.size(), Sort.by(orders));
		return repository.findByActive(isActive, page) //List<User>
				.stream() //Stream<User>
				.map(user -> convertToDto(user)) //Stream<UserDto>
				.toList(); //List<UserDto>
	}

	private Order[] setMultiSortDirection(List<SortedField> sorts) {
		if (sorts == null || sorts.isEmpty()) {
			return new Order[0];// return an empty array
		}
		return sorts.stream().map(sortedField -> {
			boolean isASC = convertToDirection(sortedField.value()).equals("ASC");
			return new Order(isASC ? Sort.Direction.ASC : Sort.Direction.DESC, sortedField.field());
		}).toArray(Order[]::new);
	}
	private UserDto convertToDto(User user) {
	    UserDto dto = new UserDto();
	    BeanUtils.copyProperties(user, dto);
	    return dto;
	}
}
