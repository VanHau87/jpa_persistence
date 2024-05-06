package com.hnguyen387.jpa_persistence.ch02.repo;

import java.util.List;

import com.hnguyen387.jpa_persistence.ch02.Message;
import com.hnguyen387.jpa_persistence.ch02.SearchMessageDto;

public interface MessageRepository {
	void createMessage(Message message);
	List<Message> getMessages();
	void updateMessage(Message message);
	Message findById(Long id);
	List<Message> findByText(SearchMessageDto dto);
}
