package com.application.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.application.model.Professor;
import com.application.repository.ProfessorRepository;

@Service
public class ProfessorService {

	@Autowired
	private ProfessorRepository professorRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * ✅ Authentification d’un professeur
	 */
	public Professor validateUser(String email, String password) {
		Professor professor = professorRepo.findByEmail(email);
		if (professor != null && passwordEncoder.matches(password, professor.getPassword())) {
			return professor;
		}
		return null;
	}

	/**
	 * ✅ Sauvegarde d’un professeur avec mot de passe encodé
	 */
	public Professor saveProfessor(Professor professor) {
		// Empêche un mot de passe déjà encodé d’être ré-encodé
		if (!professor.getPassword().startsWith("$2a$")) {
			String hashedPassword = passwordEncoder.encode(professor.getPassword());
			professor.setPassword(hashedPassword);
		}
		return professorRepo.save(professor);
	}

	/**
	 * ❌ Ancienne méthode (non sécurisée) — garde-la si besoin pour compatibilité
	 */
	public Professor addNewProfessor(Professor professor) {
		return saveProfessor(professor); // redirige vers la version sécurisée
	}

	/**
	 * ✅ Mise à jour d’un professeur
	 */
	public Professor updateProfessorProfile(Professor professor) {
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
	 * ✅ Changement du statut du professeur (ex: activé)
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
	 * ✅ Suppression d’un professeur par email
	 */
	public void deleteUserByEmail(String email) {
		Professor p = professorRepo.findByEmail(email);
		if (p != null) {
			professorRepo.delete(p);
		}
	}

	/**
	 * Reset password with a temporary one (used for forgot-password)
	 */
	public void resetPassword(String email, String rawTempPassword) {
		Professor p = professorRepo.findByEmail(email);
		if (p == null) {
			throw new RuntimeException("Professor not found");
		}
		p.setPassword(passwordEncoder.encode(rawTempPassword));
		professorRepo.save(p);
	}
}
