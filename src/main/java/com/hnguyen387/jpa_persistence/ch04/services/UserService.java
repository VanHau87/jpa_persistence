package com.hnguyen387.jpa_persistence.ch04.services;

import java.util.List;
import java.util.Set;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportedUser;
import com.hnguyen387.jpa_persistence.ch04.dtos.PagingAndSortingRecords.MultiSortedPageUsers;
import com.hnguyen387.jpa_persistence.ch04.dtos.SortedPageUser;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserDto;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserRecords.DtoV1;
import com.hnguyen387.jpa_persistence.ch04.dtos.UserRecords.DtoV2;

public interface UserService {
	int saveAll(List<ImportedUser> dtos);
	Set<String> getAllEmails();
	List<UserDto> findByActive(SortedPageUser sortedPage);
	/*
	List<DtoV1> findByActiveWithQueryV1(boolean isActive);
	List<DtoV2> findByActiveWithQueryV2(boolean isActive);
	*/
	List<UserDto> findByActiveMultiSort(MultiSortedPageUsers multiSortedPage);
	int lowerCaseEmailByLevel(int level);
}
