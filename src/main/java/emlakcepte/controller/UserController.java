package emlakcepte.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emlakcepte.request.UserRequest;
import emlakcepte.request.UserUpdateRequest;
import emlakcepte.response.UserResponse;
import emlakcepte.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	// GET /users
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAll() {
		return ResponseEntity.ok(userService.getAll());
	}

	@PostMapping
	// @ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {

		Logger logger = Logger.getLogger(UserController.class.getName());
		UserResponse userResponse = userService.createUser(userRequest);

		logger.log(Level.INFO, "user created: {0}", userResponse);
		return ResponseEntity.ok(userResponse);
	}

	// GET /users/{email} -> /users/cemdrman@gmail.com
	@GetMapping(value = "/{email}")
	public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
		return ResponseEntity.ok(userService.getByEmail(email));
	}

	@PutMapping
	// @ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<UserResponse> update(@RequestBody UserUpdateRequest userUpdateRequest) {
		UserResponse user = userService.update(userUpdateRequest);
		return ResponseEntity.ok(user);
	}

	// GET /users/{id} -> /users/1

}
