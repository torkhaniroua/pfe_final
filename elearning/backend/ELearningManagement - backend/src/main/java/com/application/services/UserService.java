package com.application.services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.application.model.User;
import com.application.model.Professor;
import com.application.repository.UserRepository;
import com.application.repository.ProfessorRepository;
import com.application.repository.EnrollmentRepository;
import com.application.repository.WishlistRepository;
import com.application.repository.MessageRepository;
import com.application.repository.CommentRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProfessorRepository professorRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private EnrollmentRepository enrollmentRepository;
	@Autowired
	private WishlistRepository wishlistRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private CommentRepository commentRepository;

	// ✅ Vérifie user + professor
	public Object validateUser(String email, String password) {
		// Vérifier d'abord dans la table User
		User user = userRepo.findByEmail(email);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;

		}

		// Vérifier dans la table Professor
		Professor professor = professorRepo.findByEmail(email);
		if (professor != null && passwordEncoder.matches(password, professor.getPassword())) {
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
		String lookupEmail = email == null ? "" : email.trim();
		User user = userRepo.findByEmailIgnoreCase(lookupEmail);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		// clean related data
		enrollmentRepository.deleteByEnrolledusername(user.getUsername());
		wishlistRepository.deleteByLikeduser(email);
		messageRepository.deleteByUserOrProfessor(email);
		commentRepository.findByUser(user).forEach(commentRepository::delete);
		userRepo.deleteByEmailIgnoreCase(email);
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

	public User upsertOAuthUser(String email, String fullName, String avatarUrl, String provider) {
		User existing = userRepo.findByEmail(email);
		if (existing != null) {
			// keep existing password/role but refresh profile data
			if (fullName != null && !fullName.isBlank()) {
				existing.setUsername(fullName);
			}
			existing.setAvatarUrl(avatarUrl);
			existing.setAuthProvider(provider);
			return userRepo.save(existing);
		}

		User user = new User();
		user.setEmail(email);
		user.setUsername(fullName != null && !fullName.isBlank() ? fullName : email);
		user.setRole("USER");
		user.setAuthProvider(provider);
		user.setAvatarUrl(avatarUrl);
		user.setIsPremuim(false);
		// generate a random password so the field is not null (OAuth users don't use it)
		String randomPassword = UUID.randomUUID().toString();
		user.setPassword(passwordEncoder.encode(randomPassword));
		return userRepo.save(user);
	}

	/**
	 * Reset password with a temporary one (used for forgot-password)
	 */
	public void resetPassword(String email, String rawTempPassword) {
		User user = userRepo.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		user.setPassword(passwordEncoder.encode(rawTempPassword));
		userRepo.save(user);
	}
}
