package com.application.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.application.model.User;
import com.application.model.Professor;
import com.application.repository.UserRepository;
import com.application.repository.ProfessorRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProfessorRepository professorRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	// ✅ Vérifie user + professor
	public Object validateUser(String email, String password) {
		// Vérifier d'abord dans la table User
		User user = userRepo.findByEmail(email);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			user.setRole("User");
			return user;

		}

		// Vérifier dans la table Professor
		Professor professor = professorRepo.findByEmail(email);
		if (professor != null && passwordEncoder.matches(password, professor.getPassword())) {
			professor.setRole("Professor");
			return professor;
		}

		// Aucun utilisateur trouvé
		return null;
	}

	public User saveUser(User user) {
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		return userRepo.save(user);
	}

	public User updateUserProfile(User user) {
		return userRepo.save(user);
	}

	public List<User> getAllUsers() {
		return (List<User>) userRepo.findAll();
	}

	public User fetchUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public void deleteUserByEmail(String email) {
		userRepo.delete(userRepo.findByEmail(email));
	}

	public User fetchUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public User fetchUserByEmailAndPassword(String email, String password) {
		return userRepo.findByEmailAndPassword(email, password);
	}

	public List<User> fetchProfileByEmail(String email) {
		return (List<User>) userRepo.findProfileByEmail(email);
	}

	public void updateStatus(String email, boolean is_premium) {
		userRepo.updateStatus(email, is_premium);
	}
}
