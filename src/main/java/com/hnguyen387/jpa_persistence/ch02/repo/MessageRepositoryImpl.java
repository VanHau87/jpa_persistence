package com.hnguyen387.jpa_persistence.ch02.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hnguyen387.jpa_persistence.ch02.Message;
import com.hnguyen387.jpa_persistence.ch02.SearchMessageDto;
import com.hnguyen387.jpa_persistence.globalexceptions.SavedException;
import com.hnguyen387.jpa_persistence.globalexceptions.UpdatedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class MessageRepositoryImpl implements MessageRepository{
	@Autowired
	private EntityManager entityManager;
	@Override
	public void createMessage(Message message) {
		try {
			entityManager.persist(message);
		} catch (Exception e) {
			Throwable rootCause = e.getCause();
			if (rootCause != null) {
				throw new SavedException("Cannot save new message: " + rootCause.getMessage());
			} else {
				throw new SavedException("Cannot save new message: " + e.getMessage());
			}
		}
	}
	@Override
	public List<Message> getMessages() {
		TypedQuery<Message> query = entityManager.createQuery("select m from Message m", Message.class);
		List<Message> messages = query.getResultList();
		return messages;
	}
	@Override
	public void updateMessage(Message message) {
		Message loadedMessage = findById(message.getId());
		if (loadedMessage != null) {
			loadedMessage.setText(message.getText());
		} else {
			throw new UpdatedException("Cannot find the Message to update");
		}
		
	}
	@Override
	public Message findById(Long id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> query = cb.createQuery(Message.class);
		Root<Message> root = query.from(Message.class);
		Predicate predicate = cb.equal(root.get("id"), id);
		query.where(predicate);
		TypedQuery<Message> typedQuery = entityManager.createQuery(query);
		return typedQuery.getSingleResult();
	}
	@Override
	public List<Message> findByText(SearchMessageDto dto) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> query = cb.createQuery(Message.class);
		Root<Message> root = query.from(Message.class);
		Predicate predicate = null;
		if (dto.isEqual) {
			predicate = cb.equal(root.get("text"), dto.text);
		} else {
			predicate = cb.like(root.get("text"), "%"+dto.text+"%");
		}
		query.where(predicate);
		return entityManager.createQuery(query).getResultList();
	}
}
