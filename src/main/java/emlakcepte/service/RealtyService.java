package emlakcepte.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

@Service
public class RealtyService {

	private static final int MAX_INDIVICUAL_REALTY_SIZE = 5;

	@Autowired
	private UserService userService;

	@Autowired
	private RealtyRepository realtyRepository;

	@Autowired
	private BannerServiceClient bannerServiceClient;

	private Logger logger = Logger.getLogger(RealtyService.class.getName());

	public Realty create(RealtyRequest realtyRequest) {

		User foundUser = userService.getById(realtyRequest.getUserId())
				.orElseThrow(() -> new UserNotFoundException("kullanıcı bulunamadı"));

		if (UserType.INDIVIDUAL.equals(foundUser.getType())) {
			validateIndividualRealtySize(foundUser);
		}

		Realty realty = convert(realtyRequest, foundUser);

		Banner bannerRequest = new Banner(String.valueOf(realty.getNo()), 1, "123123", "banner açıklaması");

		BannerResponse bannerResponse = bannerServiceClient.create(bannerRequest);

		if (!HttpStatus.CREATED.equals(bannerResponse.getHttpStatus())) {
			logger.log(Level.WARNING, "Banner kaydedilemedi!");
			throw new RuntimeException("Banner kaydedilemedi!");
		}

		return realtyRepository.save(realty);

	}

	private void validateIndividualRealtySize(User foundUser) {

		List<Realty> realtyList = realtyRepository.findAllByUserId(foundUser.getId());

		if (MAX_INDIVICUAL_REALTY_SIZE < realtyList.size()) {

			logger.log(Level.INFO, "Bireysel kullanıcı en fazla 5 ilan girebilir. userID : {0}", foundUser.getId());

			throw new EmlakCepteException("Bireysel kullanıcı en fazla 5 ilan girebilir");
		}

	}

	private Realty convert(RealtyRequest realtyRequest, User foundUser) {
		Realty realty = new Realty();
		realty.setNo(realtyRequest.getNo());
		realty.setCreateDate(LocalDateTime.now());
		realty.setStatus(RealtyType.ACTIVE);
		realty.setTitle(realtyRequest.getTitle());
		realty.setProvince(realtyRequest.getProvince());
		realty.setUser(foundUser);
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
