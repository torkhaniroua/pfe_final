package com.application.repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.application.model.Professor;

public interface ProfessorRepository extends CrudRepository<Professor, String> {

	Professor findByEmail(String email);

	List<Professor> findProfessorListByEmail(String email);

	Professor findByProfessorname(String professorname);

	Professor findByEmailAndPassword(String email, String password);

	List<Professor> findProfileByEmail(String email);

	@Transactional
	@Modifying
	@Query(value = "update professor set status = 1 where email = ?1", nativeQuery = true)
	void updateStatus(String email);

	@Transactional
	@Modifying
	@Query(value = "update professor set status = 0 where email = ?1", nativeQuery = true)
	void rejectStatus(String email);
}
