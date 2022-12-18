package emlakcepte.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import emlakcepte.configuration.RabbitMQConfiguration;
import emlakcepte.model.User;
import emlakcepte.repository.UserRepository;
import emlakcepte.request.UserRequest;

//@Service // kapalı çünkü @Bean olarak tanımladık fakat bu da doğru bir yöntem
public class UserService {

	// @Autowired
	// private AmqpTemplate rabbitTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitMQConfiguration rabbitMQConfiguration;

	@Autowired
	private UserRepository userRepository;

	public User createUser(UserRequest userRequest) {
		User savedUser = userRepository.save(convert(userRequest));
		System.out.println("[createUser] user oluşturuldu :: " + savedUser);
		rabbitTemplate.convertAndSend(rabbitMQConfiguration.getQueueName(), userRequest);
		return savedUser;
	}

	private User convert(UserRequest userRequest) {
		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setName(userRequest.getName());
		user.setPassword(userRequest.getPassword());
		user.setCreateDate(LocalDateTime.now());
		user.setType(userRequest.getType());
		return user;
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public void updatePassword(User user, String newPassword) {
		// önce hangi user bul ve passwordü update et.
	}

	public User getByEmailUntiPattern(String email) {

		//// @formatter:off
		return userRepository.findAll()
				.stream()
				.filter(user -> user.getEmail().equals(email))
				.findFirst()
				.get();
		// @formatter:on

	}

	public User getByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> getById(Integer userId) {
		return userRepository.findById(userId);
	}

}
