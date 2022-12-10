package com.emlakcepte.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.emlakcepte.model.User;

@Component
public class UserRepository {
	
	private static List<User> userList = new ArrayList<>();

	public void createUser(User user) {	
		userList.add(user);
	}
	
	public List<User> findAllUsers() {	
		return userList;
	}

}
