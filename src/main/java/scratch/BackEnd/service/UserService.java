//package scratch.BackEnd.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import scratch.BackEnd.domain.KakaoProfile;
//import scratch.BackEnd.domain.OauthToken;
//import scratch.BackEnd.domain.User.User;
//import scratch.BackEnd.domain.User.UserRepository;
//
//@Service
//public class UserService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    public OauthToken getAccessToken(String code) {
//
//
//        RestTemplate rt = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", "{클라이언트 앱 키}");
//        params.add("redirect_uri", "{리다이렉트 uri}");
//        params.add("code", code);
//        params.add("client_secret", "{시크릿 키}"); // 생략 가능
//
//        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
//                new HttpEntity<>(params, headers);
//
//        ResponseEntity<String> accessTokenResponse = rt.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST,
//                kakaoTokenRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        OauthToken oauthToken = null;
//        try {
//            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return oauthToken;
//    }
//
//    public User saveUser(String token) {
//
//        //(1)
//        KakaoProfile profile = findProfile(token);
//
//        //(2)
//        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
//
//        //(3)
//        if(user == null) {
//            user = User.builder()
//                    .kakaoId(profile.getId())
//                    .kakaoProfileImg(profile.getKakao_account().getProfile().getProfile_image_url())
//                    .kakaoNickname(profile.getKakao_account().getProfile().getNickname())
//                    .kakaoEmail(profile.getKakao_account().getEmail())
//                    //(5)
//                    .userRole("ROLE_USER").build();
//
//            userRepository.save(user);
//        }
//
//        return user;
//    }
//
//
//    //(1-1)
//    public KakaoProfile findProfile(String token) {
//
//        RestTemplate rt = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + token); //(1-4)
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
//                new HttpEntity<>(headers);
//
//        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
//        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoProfileRequest,
//                String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        KakaoProfile kakaoProfile = null;
//        try {
//            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        return kakaoProfile;
//    }
//}