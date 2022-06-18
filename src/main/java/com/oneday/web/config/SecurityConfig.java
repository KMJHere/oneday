package com.oneday.web.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Log4j2
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
      UserDetails user = User.builder().username("kmj")
              .password("$2a$10$ZDFtW4pl8g.X93NgxbM27.ehqj9MRnYjr.8hZnIZsmZuNMCWuKafS")
              .roles("USER")
              .build();

        return new InMemoryUserDetailsManager(user);
    }

    // [2022-06-18 김민정 수정 Start]
    /* WebSecurityConfigurerAdapter deprecated 이유로 수정
    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 사용자 계정 kmj
        auth.inMemoryAuthentication().withUser("kmj")
                // pw 1111 인코딩한 결과 > $2a$10$ZDFtW4pl8g.X93NgxbM27.ehqj9MRnYjr.8hZnIZsmZuNMCWuKafS
                .password("$2a$10$ZDFtW4pl8g.X93NgxbM27.ehqj9MRnYjr.8hZnIZsmZuNMCWuKafS")
                .roles("USER");
    }
    */

    // WebSecurityConfigurerAdapter > SecurityFilterChain 빈 등록 방식으로 변경
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/ex*").permitAll()
                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin(); // 인가 or 인증에 문제 시 로그인 화면 반환
        http.csrf().disable(); // csrf 토큰 비활성화
        http.logout(); // invalidatedHttpSession() deleteCookies() 쿠키나 세션을 무효화 시키는 설정 추가 가능

        return http.build();
    }
    // [2022-06-18 김민정 수정 End]
}
