package site.ogobi.ogobi.base.security;

import java.util.Map;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProviderType();
    String getEmail();
}

class KakaoUserInfo implements OAuth2UserInfo {

    private final String id;
    private final Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes, String id) {
        this.kakaoAccount = attributes;
        this.id = id;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getProviderType() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return String.valueOf(kakaoAccount.get("email"));
    }
}