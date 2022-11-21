package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.QuestionType;

public interface QuestionTypeRepository extends JpaRepository<QuestionType, Long> {
}