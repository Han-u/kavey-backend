package scratch.BackEnd.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.User;

import java.util.List;


@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    List<Survey> findAll();
    List<Survey> findByUser(User user);


}
