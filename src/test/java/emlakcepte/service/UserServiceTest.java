package emlakcepte.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import emlakcepte.configuration.RabbitMQConfiguration;
import emlakcepte.converter.UserConverter;
import emlakcepte.model.User;
import emlakcepte.model.enums.UserType;
import emlakcepte.repository.UserRepository;
import emlakcepte.request.UserRequest;
import emlakcepte.response.UserResponse;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserConverter converter;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RabbitMQConfiguration rabbitMQConfiguration;

	@Mock
	private RabbitTemplate rabbitTemplate;

	@Test
	void it_should_create() {

		// given

		Mockito.when(converter.convert(Mockito.any(UserRequest.class), Mockito.anyString())).thenReturn(new User());

		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(getUser());

		Mockito.when(converter.convert(Mockito.any(User.class))).thenReturn(getUserResponse());

		Mockito.when(rabbitMQConfiguration.getQueueName()).thenReturn("queue.name");

		// when

		UserResponse userResponse = userService.createUser(getUserRequest());

		// then

		assertThat(userResponse).isNotNull();
		assertThat(userResponse.getName()).isEqualTo(getUser().getName());
		assertThat(userResponse.getEmail()).isEqualTo(getUser().getEmail());
		assertThat(userResponse.getType()).isEqualTo(getUser().getType());

		verify(rabbitTemplate, times(1)).convertAndSend(Mockito.anyString(), Mockito.any(UserRequest.class));
		verify(userRepository).save(Mockito.any(User.class));
	}

	private User getUser() {
		return new User("test", "test@gmail.com", "hashPassword", UserType.INDIVIDUAL);
	}

	private UserRequest getUserRequest() {
		return new UserRequest("test", "test@gmail.com", "test123", UserType.INDIVIDUAL);
	}

	private UserResponse getUserResponse() {
		return new UserResponse(1, "test", "test@gmail.com", UserType.INDIVIDUAL);
	}

}
