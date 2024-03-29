package com.oneday.web.security.service;

import com.oneday.web.entity.ClubMember;
import com.oneday.web.entity.ClubMemberRole;
import com.oneday.web.repository.ClubMemberRepository;
import com.oneday.web.security.dto.ClubAuthMemberDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class ClubOAuthUserDetailsService extends DefaultOAuth2UserService {
    private final ClubMemberRepository clubMemberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("---------------Start---------------");
        log.info("userRequest: " + userRequest);

        // userRequest 정보 확인
        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==============================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v); // sub, picture, email, email_verified ..등이 출력
        });

        String email = null;

        // 구글 이용하는 경우 chk
        if(clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL: " + email);

        /*
        ClubMember clubMember = saveSocialMember(email);

        return oAuth2User;
        */

        ClubMember clubMember = saveSocialMember(email);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                true,
                clubMember.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name())

                ).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        clubAuthMemberDTO.setName(clubMember.getName());

        return clubAuthMemberDTO;
    }

    private ClubMember saveSocialMember(String email) {
        // 기존에 동일한 이메일로 가입한 회원이 있는 경우 그대로 조회만..
        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        if(result.isPresent()) {
            return result.get();
        }

        // 없을 경우 회원 추가, 이름: 이메일 주소 / 패스워드: 1111
        ClubMember clubMember = ClubMember.builder().email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        clubMemberRepository.save(clubMember);

        return clubMember;
    }
}
