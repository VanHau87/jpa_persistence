package com.hnguyen387.jpa_persistence.ch05.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hnguyen387.jpa_persistence.ch05.entities.Author;
import com.hnguyen387.jpa_persistence.ch05.repos.AuthorRepository;
import com.hnguyen387.jpa_persistence.ch05.services.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService{
	
	@Autowired
	private AuthorRepository repository;
	
	@Override
	public Author createAuthor(Author author) {
		return repository.save(author);
	}

	@Override
	public Author updateField(Author author) {
		String field = author.getField();
		Long id = author.getId();
		Author updatedAuthor = null;
		if (id != null) {
			var auth = repository.findById(id).orElseThrow();
			//only update 1 property: field
			auth.setField(field);
			updatedAuthor = repository.save(auth);
		}
		return updatedAuthor;
	}

}
