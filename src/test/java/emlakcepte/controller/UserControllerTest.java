package emlakcepte.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import emlakcepte.model.enums.UserType;
import emlakcepte.request.UserRequest;
import emlakcepte.response.UserResponse;
import emlakcepte.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void it_should_get_all_users() throws Exception {
		// given
		Mockito.when(userService.getAll()).thenReturn(getAllUserResponses());

		// when
		ResultActions resultActions = mockMvc.perform(get("/users"));

		// then

		//// @formatter:off
 
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[0].name").value("test"))
		.andExpect(jsonPath("$[0].email").value("test@gmail.com"))
		.andExpect(jsonPath("$[0].type").value(UserType.INDIVIDUAL.toString()));	
		
		// @formatter:on

	}

	@Test
	void it_should_create() throws Exception {

		Mockito.when(userService.createUser(Mockito.any(UserRequest.class))).thenReturn(getUserResponse(1));

		String request = mapper.writeValueAsString(getUserRequest());

		ResultActions resultActions = mockMvc
				.perform(post("/users").content(request).contentType(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("test"))
				.andExpect(jsonPath("$.email").value("test@gmail.com"))
				.andExpect(jsonPath("$.type").value(UserType.INDIVIDUAL.toString()));

	}

	private UserRequest getUserRequest() {
		return new UserRequest("test", "test@gmail.com", "test123", UserType.INDIVIDUAL);
	}

	private List<UserResponse> getAllUserResponses() {
		return List.of(getUserResponse(1));
	}

	private UserResponse getUserResponse(int id) {
		return new UserResponse(id, "test", "test@gmail.com", UserType.INDIVIDUAL);
	}

}
