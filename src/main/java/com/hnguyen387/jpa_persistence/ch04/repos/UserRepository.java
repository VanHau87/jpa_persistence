package com.hnguyen387.jpa_persistence.ch04.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hnguyen387.jpa_persistence.ch04.models.User;

import java.util.Collection;
import java.util.List;
import java.time.LocalDate;


public interface UserRepository extends JpaRepository<User, Long>{
	/*Query*/
	Optional<User> findById(Long id);
	List<User> findByUsername(String username);
	List<User> findAllByOrderByUsernameAsc();
	List<User> findByRegistrationDateBetween(LocalDate startDate, LocalDate endDate);
	List<User> findByUsernameAndEmail(String username, String email);
	List<User> findByUsernameOrEmail(String username, String email);
	List<User> findByUsernameIgnoreCase(String username);
	List<User> findByLevelOrderByUsernameDesc(int level);
	List<User> findByLevelGreaterThanEqual(int level);
	List<User> findByUsernameContaining(String text);
	List<User> findByUsernameLike(String text);
	List<User> findByUsernameStartingWith(String start);
	List<User> findByUsernameEndingWith(String end);
	List<User> findByActive(boolean active);
	List<User> findByRegistrationDateInIn(Collection<LocalDate> dates);
	List<User> findByRegistrationDateNotIn(Collection<LocalDate> dates);
	/*Page, sort, limit*/
	User findFirstByOrderByUsernameDesc(String username);
	List<User> findFirst2ByOrderByUsernameDesc(String username);
	List<User> findTop10ByOrderByRegistrationDateAsc();
}
