package scratch.BackEnd.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import scratch.BackEnd.config.jwt.JwtProperties;
import scratch.BackEnd.dto.kakaoLoginDto.KakaoToken;
import scratch.BackEnd.service.UserService;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** 인가 코드로 엑세스 토큰 발급 -> 사용자 정보 조회 -> DB 저장 -> jwt 토큰 발급 -> 프론트에 토큰 전달 */
    // 프론트에서 인가코드를 받는 url
    @GetMapping("/oauth/token")
    public ResponseEntity getLogin(@RequestParam("code") String code) {

        // 넘어온 인가 코드를 통해 access_token 발급
        KakaoToken oauthToken = userService.getAccessToken(code);

        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장
        String jwtToken = userService.SaveUserAndGetToken(oauthToken.getAccess_token());

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {

        User user = userService.getUser(request);

        return ResponseEntity.ok().body(user);
    }
}

}