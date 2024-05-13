package com.hnguyen387.jpa_persistence.ch04.dtos;

import java.util.List;

public class PagingAndSortingRecords {
	public record SortedField(String field, String value) {}
	public record MultiSortedPageInfo(int number, int size, List<SortedField> sorts) {}
	public record MultiSortedPageUsers(UserDto dto, MultiSortedPageInfo info) {}
}
