package emlakcepte.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import emlakcepte.client.Banner;
import emlakcepte.client.BannerServiceClient;
import emlakcepte.controller.UserController;
import emlakcepte.exception.EmlakCepteException;
import emlakcepte.exception.UserNotFoundException;
import emlakcepte.model.Realty;
import emlakcepte.model.User;
import emlakcepte.model.enums.RealtyType;
import emlakcepte.model.enums.UserType;
import emlakcepte.repository.RealtyRepository;
import emlakcepte.request.RealtyRequest;

@Service
public class RealtyService {

	private static final int MAX_INDIVICUAL_REALTY_SIZE = 5;

	@Autowired
	private UserService userService;

	@Autowired
	private RealtyRepository realtyRepository;

	@Autowired
	private BannerServiceClient bannerServiceClient;

	Logger logger = Logger.getLogger(UserController.class.getName());

	public Realty create(RealtyRequest realtyRequest) {

		User foundUser = userService.getById(realtyRequest.getUserId())
				.orElseThrow(() -> new UserNotFoundException("user bulunamadı"));

		validateIndividualRealtySize(foundUser);

		/*
		 * NPE fırlatır
		 * 
		 * if (foundUser.get().getType().equals(UserType.INDIVIDUAL)) { // en fazla 5
		 * ilan girebilir.
		 * System.out.println("Bireysel kullanıclar en fazla 5 ilan girebilir."); }
		 */

		// User user = userService.getByEmail("test@gmail.com");
		// realty.setUser(user);

		Realty realty = convert(realtyRequest);
		realty.setUser(foundUser);
		Realty savedRealty = realtyRepository.save(realty);

		System.out.println("createRealty :: " + realty);

		// TODO :: banner-service çağır ve banner siparişi ver

		Banner bannerRequest = new Banner(String.valueOf(realty.getNo()), 1, "123123", "");

		Banner bannerResponse = bannerServiceClient.create(bannerRequest);

		if (bannerResponse.getAdet() > 1) {
			System.out.println("hata verilsin");
		}
		System.out.println("bannerResponse :" + bannerResponse.getAdet());

		return savedRealty;

	}

	private void validateIndividualRealtySize(User foundUser) {
		if (UserType.INDIVIDUAL.equals(foundUser.getType())) { // en fazla 5 ilan girebilir.

			List<Realty> realtyList = realtyRepository.findAllByUserId(foundUser.getId());

			if (MAX_INDIVICUAL_REALTY_SIZE == realtyList.size()) {

				logger.log(Level.WARNING, "Bireysel kullanıcı en fazla 5 ilan girebilir. userID : {0}",
						foundUser.getId());

				throw new EmlakCepteException("indivual.user.realty.max.size");
			}
		}
	}

	private Realty convert(RealtyRequest realtyRequest) {
		Realty realty = new Realty();
		realty.setNo(realtyRequest.getNo());
		realty.setCreateDate(LocalDateTime.now());
		realty.setStatus(RealtyType.PASSIVE);
		realty.setTitle(realtyRequest.getTitle());
		realty.setProvince(realtyRequest.getProvince());
		return realty;
	}

	public List<Realty> getAll() {
		return realtyRepository.findAll();
	}

	public void getAllByProvince(String province) {

		getAll().stream().filter(realty -> realty.getProvince().equals(province)) //
				// .count();
				.forEach(realty -> System.out.println(realty));

	}
//
//	public List<Realty> getAllByUserName(User user) {
//		return getAll().stream().filter(realty -> realty.getUser().getMail().equals(user.getMail())).toList();
//	}

	public List<Realty> getActiveRealtyByUserName(User user) {
		return getAll().stream().filter(realty -> realty.getUser().getName().equals(user.getName()))
				.filter(realty -> RealtyType.ACTIVE.equals(realty.getStatus())).toList();
	}

	public List<Realty> getAllById(int id) {
		return realtyRepository.findAllByUserId(id);
	}

	public List<Realty> getAllActiveRealtyes() {
		return realtyRepository.findAllByStatus(RealtyType.ACTIVE);
	}

	@Cacheable(value = "provinceCount", key = "#province")
	public Long getCountByProvince(String province) {
		Logger logger = Logger.getLogger(RealtyService.class.getName());
		logger.log(Level.INFO, "province: {0}", province);

		long count = realtyRepository.countByProvince(province);
		logger.log(Level.INFO, "province count from db: {0}", count);
		return count;
	}

}
