package kr.edu.nynoa.manager;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// @Component, @Controller, @Service, @Repository, @Configuration, @Bean ....
@Component
public class SessionManager {
    private static final String SESSION_COOKIE_NAME = "mySessionId";

    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    // session 생성
    public void createSession(Object value, HttpServletResponse response) {
        // session id 생성: random UUID / id를 key로, value를 value로 하도록 값 저장.
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // cookie 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);

        // 응답(response)에 cookie 담기.
        response.addCookie(mySessionCookie);
    }

    // session 조회
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = this.findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    // session 만료
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = this.findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
