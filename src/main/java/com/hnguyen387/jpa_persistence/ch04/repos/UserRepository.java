package com.hnguyen387.jpa_persistence.ch04.repos;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hnguyen387.jpa_persistence.ch04.models.User;


public interface UserRepository extends JpaRepository<User, Long>{
	/*Query methods*/
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
	//List<User> findByActive(boolean active);
	List<User> findByRegistrationDateIn(Collection<LocalDate> dates);
	List<User> findByRegistrationDateNotIn(Collection<LocalDate> dates);
	/*Page, sort, limit*/
	User findFirstByUsernameOrderByUsernameDesc(String username);
	User findFirstByOrderByUsernameDesc();
	List<User> findFirst2ByUsernameOrderByUsernameDesc(String username);
	List<User> findTop10ByOrderByRegistrationDateAsc();
	List<User> findByActive(boolean active, Pageable pageable);
	List<User> findByLevel(int level, Sort sort);
	/*Query annotation*/
	@Query(value = "select u.email from Users u", nativeQuery = true)
	Set<String> getAllEmails();
}
