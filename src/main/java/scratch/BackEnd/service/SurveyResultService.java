package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.repository.*;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.type.QuestionType;

import java.util.*;
import java.util.stream.Collectors;

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

	public ResponseAllAnswerDto getAllAnswer(Long surveyId) {
		Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));
		List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAllBySurvey(survey);

		List<ResponseQuestionAnswerDto> dtos = surveyQuestions.stream().map(question -> {
			List<AnswerMulti> answerMultis = answerMultiRepository.findBySurveyQuestion(question);
			List<AnswerSub> answerSubs = answerSubRepository.findBySurveyQuestion(question);
			return new ResponseQuestionAnswerDto(question, answerMultis, answerSubs);
		}).collect(Collectors.toList());

		return new ResponseAllAnswerDto(survey, dtos);
	}

	public ResponseSurveyResultDto getSurveyResult(Long surveyId) {

		Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));
		List<Attend> attendList = surveyAttendRepository.findBySurvey_SurveyId(surveyId);
		int attendCount = attendList.size();

		// group by로 해결할 수 있으니 나중에 고민해봄

		List<ResponseQuestionResultDto> responseQuestionResultList = new ArrayList<>();
		List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
		for (SurveyQuestion question : surveyQuestions) {
			switch (question.getQuestionType()) {
				case TEXT:
					List<AnswerSub> answerSubs = answerSubRepository.findBySurveyQuestion(question);
					responseQuestionResultList.add(new ResponseQuestionResultDto(question.getQuestionId(), question.getQuestionType(), answerSubs.size(), countSubResult(answerSubs)));
					break;
				default:
					List<AnswerMulti> answerMultis = answerMultiRepository.findBySurveyQuestion(question);
					responseQuestionResultList.add(new ResponseQuestionResultDto(question.getQuestionId(), question.getQuestionType(), answerMultis.size(), countMultiResult(question, answerMultis)));
					break;
			}
		}

		ResponseSurveyResultDto responseSurveyResultDto = new ResponseSurveyResultDto(surveyId, attendCount, responseQuestionResultList);
		return responseSurveyResultDto;
	}
	public List<AnswerCountDto> countSubResult(List<AnswerSub> answerSubs){
		List<AnswerCountDto> answerCountList = new ArrayList<>();
		for (AnswerSub answerSub : answerSubs) {
		    answerCountList.add(new AnswerCountDto(answerSub.getValue(), 1));
		}
		return answerCountList;
	}
	public List<AnswerCountDto> countMultiResult(SurveyQuestion question, List<AnswerMulti> answerMultis){
		List<AnswerCountDto> answerCountList = new ArrayList<>();
		List<QuestionOption> questionOptions = questionOptionRepository.findBySurveyQuestion(question);

		Map<Long, Integer> frequencyMap = new HashMap<>();
		for (QuestionOption i : questionOptions) frequencyMap.put(i.getOptionId(), 0);

		for (AnswerMulti answerMulti : answerMultis) {
            Integer count = frequencyMap.get(answerMulti.getQuestionOption().getOptionId());
            if (count == null) {
                frequencyMap.put(answerMulti.getQuestionOption().getOptionId(), 1);
            } else {
                frequencyMap.put(answerMulti.getQuestionOption().getOptionId(), count + 1);
            }
		}

		for (Map.Entry<Long, Integer> entry : frequencyMap.entrySet()) {
			Long key = entry.getKey();
			Integer value = entry.getValue();
			answerCountList.add(new AnswerCountDto(key.toString(), value));
		}


		return answerCountList;

	}
}
