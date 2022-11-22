package scratch.BackEnd.dto.kakaoLoginDto;

import lombok.Data;

// 인가코드를 가지고 카카오로 부터 받은 토큰을 담을 객체
@Data
public class KakaoToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private int refresh_token_expires_in;

}