package com.emlakcepte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emlakcepte.model.User;
import com.emlakcepte.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired // injection işlemi, spring tarafından oluşan objenin bağlanması. Default tanımı singleton.
	private UserService service;

	// GET /users
	@GetMapping
	public List<User> getAll() {
		
		System.out.println("getAll - userService :: " + service);
		return service.getAllUser();
	}

	// POST /users
	// @RequestMapping(method = RequestMethod.POST, value = "/saveUser") --best
	// practise ters
	@PostMapping
	// @ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<User> create(@RequestBody User user) {
		//UserService service = new UserService();
		service.createUser(user);
		// return user;
		System.out.println("create - userService :: " + service);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	// GET /users/{email} -> /users/cemdrman@gmail.com
	@GetMapping(value = "/{email}")
	public User getByEmail(@PathVariable String email) {
		System.out.println("gelen email request'i: " + email);
		//UserService service = new UserService();
		System.out.println("getByEmail - userService :: " + service);
		return service.getByEmail(email);
	}
	
	//@PathParam
	

}
