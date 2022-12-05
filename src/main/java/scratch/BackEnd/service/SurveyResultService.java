package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.ResponseUserAnswerDto;
import scratch.BackEnd.dto.ResponseUserAnswerTotalDto;
import scratch.BackEnd.repository.*;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.type.QuestionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class SurveyResultService {
	private final SurveyRepository surveyRepository;
	private final SurveyQuestionRepository surveyQuestionRepository;
	private final QuestionOptionRepository questionOptionRepository;
	private final UserRepository userRepository;
	private final SurveyAttendRepository surveyAttendRepository;
	private final AnswerSubRepository answerSubRepository;
	private final AnswerMultiRepository answerMultiRepository;


	private QuestionType findQuestionType(List<SurveyQuestion> surveyQuestions, long questionId) {
		for (SurveyQuestion surveyQuestion : surveyQuestions){
			if(surveyQuestion.getQuestionId() == questionId)
				return surveyQuestion.getQuestionType();
		}
		return QuestionType.TEXT;
	}
	public ResponseUserAnswerTotalDto getUserAnswer(Long surveyId, Long attendId) {
		Attend attend = surveyAttendRepository.findById(attendId).orElseThrow(() -> new IllegalArgumentException("해당 참여정보는 없습니다. attend id = " + attendId));
		if(attend.getStatus() != AttendStatus.RESPONSE){
			throw new IllegalArgumentException("답변하지 않은 유저입니다.");
		}
		Survey survey = surveyRepository.findById(attend.getSurvey().getSurveyId()).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));
		User user = userRepository.findById(attend.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + attend.getUser().getId() ));

		List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAllBySurvey(survey);

		List<AnswerSub> answerSubs = answerSubRepository.findAllByAttend(attend);
		List<AnswerMulti> answerMultis = answerMultiRepository.findAllByAttend(attend);



		List<ResponseUserAnswerDto> responseUserAnswerDtos = new ArrayList<>();
		for (AnswerSub answerSub : answerSubs) {
			long questionId = answerSub.getSurveyQuestion().getQuestionId();
		    responseUserAnswerDtos.add(new ResponseUserAnswerDto(user.getId(), answerSub, findQuestionType(surveyQuestions, questionId)));
		}
		for (AnswerMulti answerMulti  : answerMultis) {
			responseUserAnswerDtos.add(new ResponseUserAnswerDto(user.getId(), answerMulti, findQuestionType(surveyQuestions,answerMulti.getSurveyQuestion().getQuestionId())));
		}

		Collections.sort(responseUserAnswerDtos, Comparator.comparing(ResponseUserAnswerDto::getQuestionId));

		ResponseUserAnswerTotalDto userAnswer = new ResponseUserAnswerTotalDto(user.getId(), surveyId,attendId, responseUserAnswerDtos);

		return userAnswer;
	}
}
