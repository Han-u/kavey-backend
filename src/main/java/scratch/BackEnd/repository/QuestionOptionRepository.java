package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.QuestionOption;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
}