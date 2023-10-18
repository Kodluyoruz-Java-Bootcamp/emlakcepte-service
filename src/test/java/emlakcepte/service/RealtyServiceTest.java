package emlakcepte.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
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
import emlakcepte.response.BannerResponse;

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

		assertThat(exception.getMessage()).isEqualTo("kullanıcı bulunamadı");

	}

	@Test
	void it_should_throw_emlakcepte_exception_when_indivual_user_has_max_realty_size() {

		// given

		Optional<User> user = getUser(1, UserType.INDIVIDUAL);

		Mockito.when(userService.getById(1)).thenReturn(user);

		Mockito.when(realtyRepository.findAllByUserId(1)).thenReturn(getRealtyList(6));

		// when

		Throwable exception = catchThrowable(() -> realtyService.create(getRealtyRequest(1)));

		// then
		assertThat(exception).isInstanceOf(EmlakCepteException.class);

		assertThat(exception.getMessage()).isEqualTo("Bireysel kullanıcı en fazla 5 ilan girebilir");

		verify(userService, times(1)).getById(Mockito.eq(1)); // direkt olarak 1 de diyebilirdik. örnek amaçlı ekledim

		verifyNoMoreInteractions(userService);

		verify(realtyRepository, times(1)).findAllByUserId(Mockito.anyInt());

		Mockito.verify(realtyRepository, times(0)).save(Mockito.any(Realty.class));

		verifyNoInteractions(bannerServiceClient);

	}

	@Test
	void it_should_throw_runtime_exception_when_banner_response_status_is_not_created_for_indivual_user() {

		// given

		Optional<User> user = getUser(1, UserType.INDIVIDUAL);

		Mockito.when(userService.getById(1)).thenReturn(user);

		Mockito.when(realtyRepository.findAllByUserId(1)).thenReturn(getRealtyList(1));

		Mockito.when(bannerServiceClient.create(Mockito.any(Banner.class)))
				.thenReturn(getBannerResponse(HttpStatus.BAD_REQUEST));

		// when

		Throwable exception = catchThrowable(() -> realtyService.create(getRealtyRequest(1)));

		// then
		assertThat(exception).isInstanceOf(RuntimeException.class);

		assertThat(exception.getMessage()).isEqualTo("Banner kaydedilemedi!");

		verify(userService, times(1)).getById(1);

		verifyNoMoreInteractions(userService);

		verify(realtyRepository, times(1)).findAllByUserId(1);

		Mockito.verify(realtyRepository, times(0)).save(Mockito.any(Realty.class));

		verify(bannerServiceClient, times(1)).create(Mockito.any(Banner.class));

	}

	@Test
	void it_should_save_realty() {

		// given

		Optional<User> user = getUser(1, UserType.INDIVIDUAL);

		Mockito.when(userService.getById(1)).thenReturn(user);

		Mockito.when(realtyRepository.findAllByUserId(1)).thenReturn(getRealtyList(4));

		Mockito.when(bannerServiceClient.create(Mockito.any(Banner.class)))
				.thenReturn(getBannerResponse(HttpStatus.CREATED));

		Realty realty = getRealty(1);

		Mockito.when(realtyRepository.save(Mockito.any())).thenReturn(realty);

		// when

		Realty responseRealty = realtyService.create(getRealtyRequest(1));

		// then

		verify(userService, times(1)).getById(1);

		verifyNoMoreInteractions(userService);

		verify(realtyRepository, times(1)).findAllByUserId(1);

		Mockito.verify(realtyRepository, times(1)).save(Mockito.any(Realty.class));

		verifyNoMoreInteractions(realtyRepository);

		verify(bannerServiceClient, times(1)).create(Mockito.any(Banner.class));

		verifyNoMoreInteractions(bannerServiceClient);

		assertThat(responseRealty).isNotNull();

		assertThat(responseRealty.getId()).isEqualTo(realty.getId());

		assertThat(responseRealty.getTitle()).isEqualTo(realty.getTitle());

		assertThat(responseRealty.getProvince()).isEqualTo(realty.getProvince());

		assertThat(responseRealty.getStatus()).isEqualTo(realty.getStatus());

	}

	private BannerResponse getBannerResponse(HttpStatus status) {
		BannerResponse bannerResponse = new BannerResponse();
		bannerResponse.setHttpStatus(status);
		return bannerResponse;
	}

	private List<Realty> getRealtyList(int realtySize) {
		List<Realty> realties = new ArrayList<Realty>(realtySize);
		for (int i = 0; i < realtySize; i++) {
			realties.add(getRealty(i));
		}
		return realties;
	}

	private Realty getRealty(int id) {
		return new Realty(id, "test ilan", LocalDateTime.now(), RealtyType.ACTIVE);
	}

	private RealtyRequest getRealtyRequest(int id) {
		return new RealtyRequest(id, "test ilan", "şehir", 1);
	}

	private Optional<User> getUser(int id, UserType type) {
		return Optional.of(new User(id, "test", "test@gmail.com", "hashPassword", type));
	}

}
