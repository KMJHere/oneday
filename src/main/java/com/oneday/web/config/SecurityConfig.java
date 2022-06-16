package com.oneday.web.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 사용자 계정 kmj
        auth.inMemoryAuthentication().withUser("kmj")
                // pw 1111 인코딩한 결과 > $2a$10$ZDFtW4pl8g.X93NgxbM27.ehqj9MRnYjr.8hZnIZsmZuNMCWuKafS
                .password("$2a$10$ZDFtW4pl8g.X93NgxbM27.ehqj9MRnYjr.8hZnIZsmZuNMCWuKafS")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/ex*").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin(); // 인가 or 인증에 문제 시 로그인 화면 반환
        http.csrf().disable(); // csrf 토큰 비활성화
        http.logout(); // invalidatedHttpSession() deleteCookies() 쿠키나 세션을 무효화 시키는 설정 추가 가능

    }
}
