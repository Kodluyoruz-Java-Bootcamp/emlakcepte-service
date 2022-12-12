package emlakcepte.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import emlakcepte.model.Realty;

@Repository
public class RealtyRepository {

	private static List<Realty> realtyList = new ArrayList<>();

	public void saveRealty(Realty realty) {
		realtyList.add(realty);
	}

	public List<Realty> findAll() {
		return realtyList;
	}

}
