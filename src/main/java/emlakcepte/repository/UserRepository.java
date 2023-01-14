package emlakcepte.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import emlakcepte.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	// select * from Users where email = ?
	// @Query(value = "sql",nativeQuery = true) native sql scripti yazmanız gerekirse
	Optional<User> findByEmail(String email);

}
