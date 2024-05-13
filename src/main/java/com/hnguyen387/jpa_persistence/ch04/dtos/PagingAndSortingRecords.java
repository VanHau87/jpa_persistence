package com.hnguyen387.jpa_persistence.ch04.dtos;

import java.util.List;

public class PagingAndSortingRecords {
	/*
	 * this class is used to declare records, used for page and sort
	 * */
	public record SortedField(String field,//sortBy
				String value //ascending or descending
			) {}
	public record MultiSortedPageInfo(int number, //page number-start from 0
				int size, //the amount of items in each page
				List<SortedField> sorts //sortBy multiple properties
			) {}
	public record MultiSortedPageUsers(UserDto dto, //item
			MultiSortedPageInfo info) {}
}
