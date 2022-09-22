package com.oneday.web.security.service;

import com.oneday.web.entity.ClubMember;
import com.oneday.web.repository.ClubMemberRepository;
import com.oneday.web.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {
    private final ClubMemberRepository clubMemberRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername " + username);

        // ClubUserDetailsService와  ClubMemberRepository 연동
        Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false); // username = email

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }

        ClubMember clubMember = result.get();

        log.info("Here User");
        log.info(clubMember);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                // 스프링 시큐리티에서 사용하는 SimpleGrantedAuthority 변환("ROLE_" 접두어가 붙어야 함)
                clubMember.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet())
        );

        clubAuthMemberDTO.setName(clubAuthMemberDTO.getName());
        clubAuthMemberDTO.setFromSocial(clubAuthMemberDTO.isFromSocial());

        return clubAuthMemberDTO;
    }
}
