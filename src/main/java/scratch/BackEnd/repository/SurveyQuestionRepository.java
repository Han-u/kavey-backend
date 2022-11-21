package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.Optional;


public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    Optional<SurveyQuestion> findByQuestionIdAndSurvey(Long questionId, Survey survey);

}