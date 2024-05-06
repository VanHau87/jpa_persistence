package com.hnguyen387.jpa_persistence.ch02;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hnguyen387.jpa_persistence.ch02.repo.MessageRepository;

@RestController
@RequestMapping("ch02/messages")
public class MessageController {
	@Autowired
	private MessageRepository messageRepository;
	@PostMapping
	public ResponseEntity<String> createMessage(@RequestBody Message message) {
		messageRepository.createMessage(message);
		return ResponseEntity.ok("Create new message successfully!!!");
	}
	@PostMapping("search")
	public ResponseEntity<List<Message>> findMessages(@RequestBody SearchMessageDto dto) {
		List<Message> messages = messageRepository.findByText(dto);
		return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
	}
	@GetMapping
	public ResponseEntity<List<Message>> getMessages() {
		List<Message> messages = messageRepository.getMessages();
		return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
	}
	@PutMapping
	public ResponseEntity<String> updateMessage(@RequestBody Message message) {
		messageRepository.updateMessage(message);
		return ResponseEntity.ok("Update message successfully!!!");
	}
}
