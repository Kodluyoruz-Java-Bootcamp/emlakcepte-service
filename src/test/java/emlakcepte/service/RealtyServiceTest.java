package emlakcepte.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import emlakcepte.client.Banner;
import emlakcepte.client.BannerServiceClient;
import emlakcepte.exception.EmlakCepteException;
import emlakcepte.exception.UserNotFoundException;
import emlakcepte.model.Realty;
import emlakcepte.model.User;
import emlakcepte.model.enums.RealtyType;
import emlakcepte.model.enums.UserType;
import emlakcepte.repository.RealtyRepository;
import emlakcepte.request.RealtyRequest;

@ExtendWith(SpringExtension.class)
class RealtyServiceTest {

	@InjectMocks
	private RealtyService realtyService;

	@Mock
	private UserService userService;

	@Mock
	private RealtyRepository realtyRepository;

	@Mock
	private BannerServiceClient bannerServiceClient;

	@Test
	void it_should_throw_user_found_exception_when_user_is_null() {
		// given

		// when

		Throwable exception = catchThrowable(() -> realtyService.create(new RealtyRequest()));

		// then
		assertThat(exception).isInstanceOf(UserNotFoundException.class);

	}

	@Test
	void it_should_throw_emlakcepte_exception_when_indivual_user_has_max_realty_size() {
		// given

		Optional<User> user = getUser();
		user.get().setId(1);
		Mockito.when(userService.getById(1)).thenReturn(user);

		Mockito.when(realtyRepository.findAllByUserId(1)).thenReturn(getRealtyList());

		// when

		Throwable exception = catchThrowable(() -> realtyService.create(getRealtyRequest(1)));

		// then
		assertThat(exception).isInstanceOf(EmlakCepteException.class);

		Mockito.verify(realtyRepository, times(0)).save(Mockito.any(Realty.class));

		Mockito.verify(bannerServiceClient, times(0)).create(Mockito.any(Banner.class));

	}

	private List<Realty> getRealtyList() {
		return List.of(getRealty(1), getRealty(2), getRealty(3), getRealty(4), getRealty(5));
	}

	private Realty getRealty(int id) {
		return new Realty(id, "test ilan", LocalDateTime.now(), RealtyType.ACTIVE);
	}

	private RealtyRequest getRealtyRequest(int id) {
		// TODO Auto-generated method stub
		return new RealtyRequest(id, "test ilan", "şehir", 1);
	}

	private Optional<User> getUser() {
		return Optional.of(new User("test", "test@gmail.com", "hashPassword", UserType.INDIVIDUAL));
	}

}
