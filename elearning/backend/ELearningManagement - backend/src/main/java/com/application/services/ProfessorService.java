package com.application.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.application.model.Professor;
import com.application.repository.CommentRepository;
import com.application.repository.MessageRepository;
import com.application.repository.ProfessorRepository;

@Service
public class ProfessorService {

	@Autowired
	private ProfessorRepository professorRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private MessageRepository messageRepository;

	/**
	 * Authenticate professor; only allow if admin has approved.
	 */
	public Professor validateUser(String email, String password) {
		Professor professor = professorRepo.findByEmail(email);
		if (professor != null && passwordEncoder.matches(password, professor.getPassword())) {
			String status = professor.getStatus() == null ? "" : professor.getStatus().trim().toUpperCase();
			if (!status.equals("1") && !status.equals("ACCEPTED") && !status.equals("APPROVED")) {
				throw new IllegalStateException("Compte professeur non approuvé (status: " + status + "). Contactez l'administrateur.");
			}
			return professor;
		}
		return null;
	}

	/**
	 * Save professor with encoded password and default pending status.
	 */
	public Professor saveProfessor(Professor professor) {
		// Avoid double-encoding a password that is already hashed
		if (!professor.getPassword().startsWith("$2a$")) {
			String hashedPassword = passwordEncoder.encode(professor.getPassword());
			professor.setPassword(hashedPassword);
		}
		if (professor.getStatus() == null || professor.getStatus().isBlank()) {
			professor.setStatus("PENDING"); // requires admin approval
		}
		return professorRepo.save(professor);
	}

	/**
	 * Legacy helper kept for compatibility.
	 */
	public Professor addNewProfessor(Professor professor) {
		return saveProfessor(professor);
	}

	/**
	 * Update professor profile; preserve password/avatar if not provided.
	 */
	public Professor updateProfessorProfile(Professor professor) {
		Professor existing = professorRepo.findByEmail(professor.getEmail());
		if (existing != null) {
			if (professor.getPassword() == null || professor.getPassword().isBlank()) {
				professor.setPassword(existing.getPassword());
			} else if (!professor.getPassword().equals(existing.getPassword())) {
				professor.setPassword(passwordEncoder.encode(professor.getPassword()));
			}
			if (professor.getAvatarUrl() == null || professor.getAvatarUrl().isBlank()) {
				professor.setAvatarUrl(existing.getAvatarUrl());
			}
		}
		return professorRepo.save(professor);
	}

	public List<Professor> getAllProfessors() {
		return (List<Professor>) professorRepo.findAll();
	}

	public List<Professor> getProfessorListByEmail(String email) {
		return (List<Professor>) professorRepo.findProfessorListByEmail(email);
	}

	public Professor fetchProfessorByEmail(String email) {
		return professorRepo.findByEmail(email);
	}

	public Professor fetchProfessorByEmailIgnoreCase(String email) {
		return professorRepo.findByEmailIgnoreCase(email);
	}

	public Professor fetchProfessorByProfessorname(String professorname) {
		return professorRepo.findByProfessorname(professorname);
	}

	public Professor fetchProfessorByEmailAndPassword(String email, String password) {
		return professorRepo.findByEmailAndPassword(email, password);
	}

	public List<Professor> fetchProfileByEmail(String email) {
		return (List<Professor>) professorRepo.findProfileByEmail(email);
	}

	/**
	 * Change professor status to approved.
	 */
	public void updateStatus(String email) {
		professorRepo.updateStatus(email);
	}

	public void rejectStatus(String email) {
		professorRepo.rejectStatus(email);
	}

	public List<Professor> getProfessorsByEmail(String email) {
		return professorRepo.findProfessorListByEmail(email);
	}

	/**
	 * Delete professor by email.
	 */
	@Transactional
	public boolean deleteUserByEmail(String email) {
		if (email == null) {
			return false;
		}
		Professor p = professorRepo.findByEmailIgnoreCase(email);
		if (p == null) {
			return false;
		}
		// clean dependent data to avoid FK issues
		commentRepository.deleteByProfessor(p);
		messageRepository.deleteAllProfessorMessages(email);
		professorRepo.delete(p);
		return true;
	}

	public Professor updateProfessorAvatar(String email, String avatarUrl) {
		Professor professor = professorRepo.findByEmail(email);
		if (professor == null) {
			return null;
		}
		professor.setAvatarUrl(avatarUrl);
		return professorRepo.save(professor);
	}
}
