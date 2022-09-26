package com.oneday.web.config;

import com.oneday.web.security.handler.ClubLoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Log4j2
public class SecurityConfig {
    @Autowired
    UserDetailsService userDetailsService;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    // InMemory로 테스트 사용자 생성
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
      UserDetails user = User.builder().username("kmj")
              .password("$2a$10$ZDFtW4pl8g.X93NgxbM27.ehqj9MRnYjr.8hZnIZsmZuNMCWuKafS")
              .roles("USER")
              .build();

        return new InMemoryUserDetailsManager(user);
    }
     */

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
        /*
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/ex*").permitAll()
                .antMatchers("/sample/member").hasRole("USER");
         */

        http.formLogin(); // 인가 or 인증에 문제 시 로그인 화면 반환
        http.csrf().disable(); // csrf 토큰 비활성화

        http.logout(); // invalidatedHttpSession() deleteCookies() 쿠키나 세션을 무효화 시키는 설정 추가 가능

        // http.oauth2Login();
        // 소셜 로그인 처리 추가
        http.oauth2Login().successHandler(successHandler());

        // remember me 설정(초단위로 설정), 7일, 소셜 로그인은 x
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService);

        return http.build();
    }
    // [2022-06-18 김민정 수정 End]

    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }
}
