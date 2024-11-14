package com.example.service;

import com.example.model.User;

public interface UserService {

	User loginUser(String username, String password);

	String registerUser(User user);
}