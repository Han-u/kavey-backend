package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.QuestionOption;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.Optional;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    Optional<QuestionOption> findByOptionIdAndSurveyQuestion(Long id, SurveyQuestion question);
}