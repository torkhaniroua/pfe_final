package com.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import com.application.configuration.JwtUtil;
import com.application.model.Professor;
import com.application.model.User;
import com.application.model.response.ErrorRes;
import com.application.model.response.LoginProfessorRes;
import com.application.model.response.LoginRes;
import com.application.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

	@Autowired
	private UserService userService;

	private final JwtUtil jwtUtil;

	public LoginController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@GetMapping("/")
	public String welcomeMessage() {
		return "Welcome to Elearning Management system !!!";
	}

	@PostMapping("/login/user")
	public ResponseEntity<?> loginUser(@RequestBody User loginRequest) throws Exception {
		String currEmail = loginRequest.getEmail();
		String currPassword = loginRequest.getPassword();

		try {
			Object userObj = userService.validateUser(currEmail, currPassword);

			if (userObj instanceof User) {
				// ✅ Connexion d’un utilisateur normal
				User u = (User) userObj;
				String token = jwtUtil.createToken(u);
				u.setPassword("");
				LoginRes loginRes = new LoginRes(u, token);
				return ResponseEntity.ok(loginRes);

			} else if (userObj instanceof Professor) {
				// ✅ Connexion d’un professeur
				Professor p = (Professor) userObj;
				String token = jwtUtil.createToken(p);
				p.setPassword("");
				LoginProfessorRes loginRes = new LoginProfessorRes(p, token);
				return ResponseEntity.ok(loginRes);

			} else {
				// ❌ Aucun compte correspondant
				ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
			}

		} catch (BadCredentialsException e) {
			ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

		} catch (Exception e) {
			ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}
}
