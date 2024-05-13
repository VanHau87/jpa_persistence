package com.hnguyen387.jpa_persistence.ch04.dtos;

public class PageInfo {
	public int number;//number of page (starts from 0)
	public int size;//number of item in 1 page
	public String direction; //DESC or ASC
	public String sortBy;
}
