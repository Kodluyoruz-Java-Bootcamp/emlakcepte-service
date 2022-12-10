package com.emlakcepte.repository;

import java.util.ArrayList;
import java.util.List;

import com.emlakcepte.model.Realty;

public class RealtyRepository {

	private static List<Realty> realtyList = new ArrayList<>();

	public void saveRealty(Realty realty) {
		realtyList.add(realty);
	}

	public List<Realty> findAll() {
		return realtyList;
	}

}
