package scratch.BackEnd.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import scratch.BackEnd.config.jwt.JwtProperties;
import scratch.BackEnd.dto.kakaoLoginDto.KakaoProfile;
import scratch.BackEnd.dto.kakaoLoginDto.KakaoToken;
import scratch.BackEnd.domain.User;
import scratch.BackEnd.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserService {

    final UserRepository userRepository;

    private final String client_id = "1dc9c6c354c8d2390b7d0299842b6fae";
    private final String redirect_uri = "http://localhost:3000/login";
    //    private final String client_secret = "xxx";

    //생성자 주입 (NO 필드 인젝션)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //카카오로 부터 엑세스 토큰을 받는 함수
    public KakaoToken getAccessToken(String code) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), KakaoToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;
    }

    // 카카오 로그인 사용자 강제 회원가입
    public String SaveUserAndGetToken(String access_token) {

        KakaoProfile profile = findProfile(access_token); // 사용자 정보 받아오기
        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());

        // 첫 이용자 강제 회원가입
        if(user == null) {
            user = User.builder()
                    .kakaoid(profile.getId())
                    .nickname(profile.getKakao_account().getProfile().getNickname())
                    .email(profile.getKakao_account().getEmail())
                    .build();

            userRepository.save(user);
        }

        return createToken(user);
    }

    public String createToken(User user) {
        // Jwt 생성 후 헤더에 추가해서 보내줌
        String jwtToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))

                .withClaim("id", user.getKakaoid())
                .withClaim("nickname", user.getNickname())

                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return jwtToken;
    }

    // 사용자 정보 가져오기
    public KakaoProfile findProfile(String token) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public User getUser(HttpServletRequest request) {

        Long kakaoid = (Long) request.getAttribute("kakaoid");
        System.out.println(kakaoid);

        // db 에서 사용자 정보 가져와서 객체에 담기
        User user = userRepository.findByKakaoid(kakaoid)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + kakaoid));

        return user;
    }
}