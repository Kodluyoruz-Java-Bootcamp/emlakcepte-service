package emlakcepte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emlakcepte.model.User;
import emlakcepte.request.UserRequest;
import emlakcepte.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	// GET /users
	@GetMapping
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userService.getAll());
	}

	@PostMapping
	// @ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<User> create(@RequestBody UserRequest userRequest) {
		User user = userService.createUser(userRequest);
		return ResponseEntity.ok(user);
	}

	// GET /users/{email} -> /users/cemdrman@gmail.com
	@GetMapping(value = "/{email}")
	public ResponseEntity<User> getByEmail(@PathVariable String email) {
		return ResponseEntity.ok(userService.getByEmail(email));
	}

}
