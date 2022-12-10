package com.emlakcepte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emlakcepte.model.Realty;
import com.emlakcepte.model.User;
import com.emlakcepte.model.enums.RealtyType;
import com.emlakcepte.model.enums.UserType;
import com.emlakcepte.repository.RealtyRepository;

@Service
public class RealtyService {

	private RealtyRepository realtyDao = new RealtyRepository();

	//@Autowired // injection
	private UserService userService;

	public void createRealty(Realty realty) {

		// userService.printAllUser();

		if (UserType.INDIVIDUAL.equals(realty.getUser().getType())) {
			// en fazla 5 ilan girebilir.

			System.out.println("Bireysel kullanıclar en fazla 5 ilan girebilir.");
		}

		realtyDao.saveRealty(realty);
		System.out.println("createRealty :: " + realty.getTitle());
	}

	public List<Realty> getAll() {
		return realtyDao.findAll();
	}

	public void printAll(List<Realty> realtList) {
		realtList.forEach(realty -> System.out.println(realty));
	}

	public void getAllByProvince(String province) {

		getAll().stream().filter(realty -> realty.getProvince().equals(province))
				// .count();
				.forEach(realty -> System.out.println(realty));

	}

	public List<Realty> getAllByUserName(User user) {
		return getAll().stream().filter(realty -> realty.getUser().getMail().equals(user.getMail())).toList();
	}

	public List<Realty> getActiveRealtyByUserName(User user) {

		return getAll().stream().filter(realty -> realty.getUser().getName().equals(user.getName()))
				.filter(realty -> RealtyType.ACTIVE.equals(realty.getStatus())).toList();

	}

}
