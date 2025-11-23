package com.application.services;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.application.model.User;
import com.application.model.Professor;
import com.application.model.response.PasswordResetResponse;
import com.application.repository.CommentRepository;
import com.application.repository.MessageRepository;
import com.application.repository.WishlistRepository;
import com.application.repository.EnrollmentRepository;
import com.application.repository.UserRepository;
import com.application.repository.ProfessorRepository;
import org.springframework.util.StringUtils;

@Service
public class UserService {

	private static final Set<String> ADMIN_EMAILS =
			new HashSet<>(Arrays.asList("admin@gmail.com"));

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProfessorRepository professorRepo;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private EnrollmentRepository enrollmentRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;
	
	private static final String VERIFY_LINK_BASE = "http://localhost:4200/email-verify?token=";

	private static final SecureRandom RANDOM = new SecureRandom();
	private static final String PASSWORD_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789@$#!?";

	// ✅ Vérifie user + professor
	public Object validateUser(String email, String password) {
		// Vérifier d'abord dans la table User
		User user = userRepo.findByEmail(email);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			// Autoriser la connexion m�me si l'email n'est pas encore v�rifi� pour d�bloquer les anciens comptes
			user.setRole(resolveUserRole(user));
			return user;

		}

		// Vérifier dans la table Professor
		Professor professor = professorRepo.findByEmail(email);
		if (professor != null && passwordEncoder.matches(password, professor.getPassword())) {
			professor.setRole("PROFESSOR");
			return professor;
		}

		// Aucun utilisateur trouvé
		return null;
	}

	private String resolveUserRole(User user) {
		if (user == null) {
			return "USER";
		}
		String email = user.getEmail() == null ? "" : user.getEmail().trim().toLowerCase();
		if (!email.isEmpty() && ADMIN_EMAILS.contains(email)) {
			return "ADMIN";
		}
		String role = user.getRole();
		if (role == null || role.isBlank()) {
			return "USER";
		}
		return role.trim().toUpperCase();
	}

	public User saveUser(User user) {
		prepareVerification(user);
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		return userRepo.save(user);
	}

	public User updateUserProfile(User user) {
		User existing = userRepo.findByEmail(user.getEmail());
		if (existing != null) {
			if (user.getPassword() == null || user.getPassword().isBlank()) {
				user.setPassword(existing.getPassword());
			} else if (!user.getPassword().equals(existing.getPassword())) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			// keep immutable / absent fields from being overwritten
			if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
				user.setAvatarUrl(existing.getAvatarUrl());
			}
			if (user.getUserid() == null || user.getUserid().isBlank()) {
				user.setUserid(existing.getUserid());
			}
		}
		return userRepo.save(user);
	}
	
	public User verifyByToken(String token) {
		if (!StringUtils.hasText(token)) {
			return null;
		}
		User user = userRepo.findByVerificationToken(token);
		if (user == null) {
			return null;
		}
		user.setEmailVerified(true);
		user.setVerificationToken(null);
		return userRepo.save(user);
	}
	
	private void prepareVerification(User user) {
		if (user == null) {
			return;
		}
		if (user.isEmailVerified()) {
			return; // nothing to do for already verified users
		}
		// new or unverified users: generate token if missing and send email
		if (!StringUtils.hasText(user.getVerificationToken())) {
			String token = UUID.randomUUID().toString();
			user.setVerificationToken(token);
		}
		String link = VERIFY_LINK_BASE + user.getVerificationToken();
		String body = "Hello " + (user.getUsername() == null ? "user" : user.getUsername()) + ",\n\n"
				+ "Please confirm your email by clicking the link below:\n" + link + "\n\n"
				+ "If you did not create an account, you can ignore this message.\n\n"
				+ "eLearning Support";
		emailService.sendPasswordResetEmail(user.getEmail(), "Verify your eLearning account", body);
	}

	public List<User> getAllUsers() {
		return (List<User>) userRepo.findAll();
	}

	public User fetchUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User fetchUserByEmailIgnoreCase(String email) {
		return userRepo.findByEmailIgnoreCase(email);
	}

	@Transactional
	public void deleteUserByEmail(String email) {
		if (email != null && ADMIN_EMAILS.contains(email.trim().toLowerCase())) {
			throw new IllegalArgumentException("Admin accounts cannot be deleted");
		}
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return;
		}
		// Nettoyage des dépendances pour éviter les violations de contraintes
		messageRepository.deleteAllUserMessages(email);
		commentRepository.deleteByUser(user);
		wishlistRepository.deleteByLikeduser(email);
		// Enrollments liés à cet utilisateur (par nom ou id)
		enrollmentRepository.deleteByEnrolledusername(user.getUsername());
		if (user.getUserid() != null) {
			enrollmentRepository.deleteByEnrolleduserid(user.getUserid());
		}
		userRepo.delete(user);
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

	public User updateUserAvatar(String email, String avatarUrl) {
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return null;
		}
		user.setAvatarUrl(avatarUrl);
		return userRepo.save(user);
	}

	public User upsertOAuthUser(String email, String displayName, String avatarUrl, String provider) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email is required for social login");
		}

		User user = userRepo.findByEmail(email);
		boolean isNewUser = user == null;
		if (isNewUser) {
			user = new User();
			user.setEmail(email);
			user.setUserid(buildOAuthUserId(provider));
			user.setPassword(passwordEncoder.encode(generateTemporaryPassword()));
			user.setIsPremuim(false);
		}

		if (displayName != null && !displayName.isBlank()) {
			user.setUsername(displayName);
		} else if (isNewUser && (user.getUsername() == null || user.getUsername().isBlank())) {
			user.setUsername(email);
		}

		if (avatarUrl != null && !avatarUrl.isBlank()) {
			user.setAvatarUrl(avatarUrl);
		}

		user.setRole(resolveUserRole(user));
		return userRepo.save(user);
	}

	public PasswordResetResponse resetPassword(String email, String roleHint) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email is required");
		}
		String normalizedRole = roleHint == null ? "" : roleHint.trim().toLowerCase();
		String normalizedEmail = email.trim();

		if ("professor".equals(normalizedRole)) {
			Professor professor = professorRepo.findByEmail(normalizedEmail);
			if (professor != null) {
				return resetProfessorPassword(professor);
			}
		}

		if ("user".equals(normalizedRole) || normalizedRole.isEmpty()) {
			User user = userRepo.findByEmail(normalizedEmail);
			if (user != null) {
				return resetUserPassword(user);
			}
		}

		// if hint was professor but not found, try user as fallback
		if ("professor".equals(normalizedRole)) {
			User fallbackUser = userRepo.findByEmail(normalizedEmail);
			if (fallbackUser != null) {
				return resetUserPassword(fallbackUser);
			}
		}

		// fallback for unspecified role: try professor
		if (normalizedRole.isEmpty()) {
			Professor professor = professorRepo.findByEmail(normalizedEmail);
			if (professor != null) {
				return resetProfessorPassword(professor);
			}
		}

		throw new IllegalArgumentException("No account found for " + normalizedEmail);
	}

	private PasswordResetResponse resetUserPassword(User user) {
		String rawPassword = generateTemporaryPassword();
		user.setPassword(passwordEncoder.encode(rawPassword));
		userRepo.save(user);
		String body = buildEmailBody(user.getUsername(), rawPassword, "user dashboard");
		emailService.sendPasswordResetEmail(user.getEmail(), "eLearning password reset", body);
		return new PasswordResetResponse(user.getEmail(), "USER", "Temporary password sent to email");
	}

	private PasswordResetResponse resetProfessorPassword(Professor professor) {
		String rawPassword = generateTemporaryPassword();
		professor.setPassword(passwordEncoder.encode(rawPassword));
		professorRepo.save(professor);
		String body = buildEmailBody(professor.getProfessorname(), rawPassword, "professor dashboard");
		emailService.sendPasswordResetEmail(professor.getEmail(), "eLearning password reset", body);
		return new PasswordResetResponse(professor.getEmail(), "PROFESSOR", "Temporary password sent to email");
	}

	private String generateTemporaryPassword() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			builder.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
		}
		return builder.toString();
	}

	private String buildOAuthUserId(String provider) {
		String prefix = provider == null || provider.isBlank()
				? "SOC"
				: provider.substring(0, Math.min(provider.length(), 3)).toUpperCase(Locale.ROOT);
		return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
	}

	private String buildEmailBody(String name, String tempPassword, String dashboard) {
		return "Hello " + name + ",\n\n"
				+ "We received a request to reset your eLearning password. "
				+ "Use the temporary password below to log in, then update it from your profile settings:\n\n"
				+ "Temporary password: " + tempPassword + "\n\n"
				+ "Once logged in, please change this password immediately for security reasons.\n\n"
				+ "eLearning Support";
	}
}
