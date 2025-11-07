package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.User;

public interface UserRepository extends CrudRepository<User, Integer>
{

    public User findByEmail(String email);
	
	public User findByUsername(String username);
	
	public User findByEmailAndPassword(String email, String password);
	
	public List<User> findProfileByEmail(String email);
	@Transactional
	@Modifying
	@Query(value = "update user set is_premuim = ?2 where email = ?1", nativeQuery = true)
	public void updateStatus(String email , boolean is_premium);
}