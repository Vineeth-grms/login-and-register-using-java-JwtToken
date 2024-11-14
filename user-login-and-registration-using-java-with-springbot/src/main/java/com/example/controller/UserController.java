package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.service.UserService;
import com.example.utiles.AuthResponse;
import com.example.utiles.JwtUtil;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService service;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			String response = service.registerUser(user);
			if (response.equals("User Registered Successfully...")) {
				String token = jwtUtil.generateToken(user.getUsername());
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(new AuthResponse(token, "User registered successfully and token generated."));
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during registration: " + e.getMessage());

		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		try {
			User authenticatedUser = service.loginUser(user.getUsername(), user.getPassword());
			if (authenticatedUser != null) {
				String token = jwtUtil.generateToken(authenticatedUser.getUsername());
				return ResponseEntity.ok(new AuthResponse(token, "User logged in successfully."));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

}
