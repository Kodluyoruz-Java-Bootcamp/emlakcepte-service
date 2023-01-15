package emlakcepte.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import emlakcepte.configuration.RabbitMQConfiguration;
import emlakcepte.controller.UserController;
import emlakcepte.converter.UserConverter;
import emlakcepte.exception.UserNotFoundException;
import emlakcepte.model.User;
import emlakcepte.repository.UserRepository;
import emlakcepte.request.LoginRequest;
import emlakcepte.request.UserRequest;
import emlakcepte.request.UserUpdateRequest;
import emlakcepte.response.UserResponse;
import emlakcepte.util.PasswordUtil;

//@Service // kapalı çünkü @Bean olarak tanımladık fakat bu da doğru bir yöntem
public class UserService {

	private static final String EMAIL_VEYA_ŞIFRE_YANLIŞ = "Email veya şifre yanlış";

	private static final String LOGIN_BAŞARILI = "Login Başarılı";

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitMQConfiguration rabbitMQConfiguration;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserConverter converter;

	public UserResponse createUser(UserRequest userRequest) {
		Logger logger = Logger.getLogger(UserController.class.getName());

		String hash = PasswordUtil.preparePasswordHash(userRequest.getPassword(), userRequest.getEmail());

		logger.log(Level.INFO, "[createUser] - password hash created: {0}", hash);

		User savedUser = userRepository.save(converter.convert(userRequest, hash));

		logger.log(Level.INFO, "[createUser] - user created: {0}", savedUser.getId());
		
		rabbitTemplate.convertAndSend(rabbitMQConfiguration.getQueueName(), userRequest);

		logger.log(Level.WARNING, "[createUser] - userRequest: {0}, sent to : {1}",
				new Object[] { userRequest.getEmail(), rabbitMQConfiguration.getQueueName() });
		return converter.convert(savedUser);
	}

	public List<UserResponse> getAll() {
		return converter.convert(userRepository.findAll());
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

	public UserResponse getByEmail(String email) {
		return converter.convert(userRepository.findByEmail(email).get());
	}

	public Optional<User> getById(Integer userId) {
		return userRepository.findById(userId);
	}

	public UserResponse update(UserUpdateRequest userUpdateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	public String login(LoginRequest loginRequest) {

		User foundUser = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new UserNotFoundException("kullanıcı bulunamadı"));

		// diğer yöntem
		// Optional<User> findByEmail =
		// userRepository.findByEmail(loginRequest.getEmail());

		String passwordHash = PasswordUtil.preparePasswordHash(loginRequest.getPassword(), loginRequest.getEmail());

		boolean isValid = PasswordUtil.validatePassword(passwordHash, foundUser.getPassword());

		return isValid ? LOGIN_BAŞARILI : EMAIL_VEYA_ŞIFRE_YANLIŞ;

	}

}
