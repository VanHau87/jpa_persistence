package com.hnguyen387.jpa_persistence.ch04.utils;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportResult;

public interface ReadStrategy {
	ImportResult read(Iterator<Row> iterator);
}
