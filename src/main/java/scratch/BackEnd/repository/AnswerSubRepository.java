package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.AnswerSub;
import scratch.BackEnd.domain.Attend;

import java.util.List;
import java.util.Optional;

public interface AnswerSubRepository extends JpaRepository<AnswerSub, Long> {

	List<AnswerSub> findAllByAttend(Attend attend);






}