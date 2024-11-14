package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository repository;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String registerUser(User user) {
		if (repository.existsByUsername(user.getUsername())) {
			return "Username already exists..";
		} else if (repository.existsByEmail(user.getEmail())) {
			return "Email already exists..";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		repository.save(user);
		return "User Registered Successfully...";
	}

	@Override
	public User loginUser(String username, String password) {
		User loginUser = repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Users not found"));
		// password.equals(loginUser.get().getPassword()) for normal password
		if (!passwordEncoder.matches(password, loginUser.getPassword())) {
			throw new BadCredentialsException("Invalid Credentials");
		}
		return loginUser;

	}

}
