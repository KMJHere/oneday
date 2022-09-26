package com.oneday.web.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
    // 문자열로 패턴을 받는 생성자 반드시 필요
    public ApiLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    // attemptAuthentication() 반드시 필요
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("------------ApiLoginFilter------------");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        log.info("여기까지 왔니 뭐가 들었니?" + email + " " + pw);

        // Authentication 타입의 객체를 만들어서 파라미터로 전달 대신 UsernamePasswordAuthenticationToken 이용 인증 처리 구현
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pw);

        // authenticate 메서드는 파라미터, 리턴 타입 모두 Authentication 타입!
        return getAuthenticationManager().authenticate(authToken);
        /*
        if(email == null) {
            throw new BadCredentialsException("email cannot be null");
        }

        return null;
         */


    }
}
