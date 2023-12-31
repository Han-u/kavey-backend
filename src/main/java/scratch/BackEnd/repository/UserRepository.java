package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);
    Optional<User> findByKakaoid(Long kakaoid);
    User findByEmail(String email);
}