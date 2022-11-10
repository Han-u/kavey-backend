package scratch.BackEnd.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoid(Long kakaoid);
    User findByEmail(String email);
    User findByNickname(String nickname);
}