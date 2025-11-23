package com.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.application.model.Professor;
import com.application.model.User;
import com.application.services.ProfessorService;
import com.application.services.EmailService;
import com.application.services.UserService;
import java.util.Map;

@RestController
public class RegistrationController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfessorService professorService;

	@Autowired
	private EmailService emailService;
	
	@PostMapping("/registeruser")
	@CrossOrigin(origins = "http://localhost:4200")
	public User registerUser(@RequestBody User user) throws Exception
	{
		String currEmail = normalizeEmail(user.getEmail());
		user.setEmail(currEmail);
		user.setEmailVerified(false);
		String newID = getNewID();
		user.setUserid(newID);
		
		if(currEmail != null && !"".equals(currEmail))
		{
			User userObj = userService.fetchUserByEmailIgnoreCase(currEmail);
			if(userObj != null)
			{
				throw new ResponseStatusException(HttpStatus.CONFLICT, "User with "+currEmail+" already exists !!!");
			}
		}
		User userObj = null;
		userObj = userService.saveUser(user);
		if (userObj != null) {
			emailService.sendAccountCreationEmail(userObj.getEmail(), userObj.getUsername());
		}
		return userObj;
	}
	
	@PostMapping("/registerprofessor")
	@CrossOrigin(origins = "http://localhost:4200")
	public Professor registerDoctor(@RequestBody Professor professor) throws Exception
	{
		String currEmail = normalizeEmail(professor.getEmail());
		professor.setEmail(currEmail);
		String newID = getNewID();
		professor.setProfessorid(newID);
		// Newly created professors require admin approval before login.
		if (professor.getStatus() == null || professor.getStatus().isBlank()) {
			professor.setStatus("PENDING");
		}
		
		if(currEmail != null && !"".equals(currEmail))
		{
			Professor professorObj = professorService.fetchProfessorByEmailIgnoreCase(currEmail);
			if(professorObj != null)
			{
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Professor with "+currEmail+" already exists !!!");
			}
		}
		Professor professorObj = null;
		professorObj = professorService.saveProfessor(professor);
		if (professorObj != null) {
			emailService.sendAccountCreationEmail(professorObj.getEmail(), professorObj.getProfessorname());
		}
		return professorObj;
	}
	
	@GetMapping("/email-exists")
	@CrossOrigin(origins = "http://localhost:4200")
	public java.util.Map<String, Boolean> emailExists(@RequestParam String email) {
		String normalized = normalizeEmail(email);
		boolean exists = false;
		if (normalized != null && !normalized.isEmpty()) {
			exists = userService.fetchUserByEmail(normalized) != null
					|| professorService.fetchProfessorByEmail(normalized) != null;
		}
		return java.util.Map.of("exists", exists);
	}
	
	@GetMapping("/verify-email")
	@CrossOrigin(origins = "http://localhost:4200")
	public Map<String, Object> verifyEmail(@RequestParam String token) {
		User user = userService.verifyByToken(token);
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token");
		}
		return Map.of("email", user.getEmail(), "verified", true);
	}
	
	public String getNewID()
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) 
        {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
	}
	
	private String normalizeEmail(String email) {
		return email == null ? null : email.trim().toLowerCase();
	}
}
