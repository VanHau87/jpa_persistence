package com.hnguyen387.jpa_persistence.ch05.services;

import com.hnguyen387.jpa_persistence.ch05.entities.Author;

public interface AuthorService {
	Author createAuthor(Author author);
	Author updateField(Author author);
}
