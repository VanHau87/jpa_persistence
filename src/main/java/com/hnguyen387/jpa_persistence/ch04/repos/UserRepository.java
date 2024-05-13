package com.hnguyen387.jpa_persistence.ch04.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hnguyen387.jpa_persistence.ch04.models.User;


public interface UserRepository extends JpaRepository<User, Long>{
	/*Query methods*/
	/*
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
		List<User> findByRegistrationDateIn(Collection<LocalDate> dates);
		List<User> findByRegistrationDateNotIn(Collection<LocalDate> dates);
	*/
	/*Page, sort, limit*/
	/*
		User findFirstByUsernameOrderByUsernameDesc(String username);
		User findFirstByOrderByUsernameDesc();
		List<User> findFirst2ByUsernameOrderByUsernameDesc(String username);
		List<User> findTop10ByOrderByRegistrationDateAsc();
		List<User> findByActive(boolean active, Pageable pageable);
		List<User> findByLevel(int level, Sort sort);
	*/
	List<User> findByActive(boolean active, Pageable pageable);
	/*Query annotation*/
	@Query(value = "select u.email from Users u", nativeQuery = true)
	Set<String> getAllEmails();
	
	@Query("select count(u) from User u where u.active = ?1 and u.level = ?2")
	long countByActiveAndByLevel(boolean active, int level);
	
	@Query("select u from User u where u.active =:active and u.level =:level")
	List<User> findByActiveAndByLevel(@Param("active") boolean isActive, @Param("level") int level);
	
	@Query(value="SELECT COUNT(*) FROM USERS WHERE LEVEL = ?1", nativeQuery=true)
	long countByLevel(int level);
	
	@Query("select u.username, LENGTH(u.email) as email_length from #{#entityName} u where u.username like %?1%")
	List<Object[]> findByNameAndThenSort(String text, Sort sort);
	/*
		@Query("select u.username, LENGTH(u.email) as email_length from #{#entityName} u where u.username like %?1%")
		List<Object[]> findByNameAndThenSort(String text, Pageable pageable);
	*/
}
