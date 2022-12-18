package emlakcepte.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import emlakcepte.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// select * from Users where email = ?
	// @Query(value = "sql",nativeQuery = true) native sql scripti yazmanız gerekirse
	User findByEmail(String email);

}
